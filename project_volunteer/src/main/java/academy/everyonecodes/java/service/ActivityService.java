package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.ErrorMessage;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.DraftRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService
{
    private final ActivityRepository activityRepository;
    private final ActivityDraftTranslator activityDraftTranslator;
    private final DraftRepository draftRepository;
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final String subject;
    private final String text;

    public ActivityService(ActivityRepository activityRepository, ActivityDraftTranslator activityDraftTranslator, DraftRepository draftRepository, UserRepository userRepository, EmailServiceImpl emailService, @Value("${activityDeletedEmail.subject}") String subject, @Value("${activityDeletedEmail.text}") String text)
    {
        this.activityRepository = activityRepository;
        this.activityDraftTranslator = activityDraftTranslator;
        this.draftRepository = draftRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.subject = subject;
        this.text = text;
    }

    public List<Activity> getAllActivities()
    {
        return activityRepository.findAll();
    }

    public List<Activity> getActivitiesOfOrganizer(String organizerUsername)
    {
        return activityRepository.findByOrganizer_Username(organizerUsername);
    }

    public Activity findActivityById(Long id)
    {
        Optional<Activity> oActivity = activityRepository.findById(id);
        Activity activity = new Activity();
        if (oActivity.isEmpty())
            ExceptionThrower.badRequest(ErrorMessage.NO_MATCHING_ACTIVITY_FOUND);
        else
            activity = oActivity.get();
        return activity;
    }

    public Activity postActivity(Draft draft)
    {
        return saveActivity(draft);
    }

    public Activity editActivity(Activity activityNew)
    {
        Activity activityOld = findActivityById(activityNew.getId());
        authenticateLoggedInUserEqualsObjectOwner(activityOld.getOrganizer().getUsername());
        if (!activityOld.getParticipants().isEmpty() || !activityOld.getApplicants().isEmpty())
            ExceptionThrower.badRequest(ErrorMessage.EDIT_ACTIVITY_WITH_APPLICANTS_OR_PARTICIPANTS_NOT_POSSIBLE);
        return postActivity(activityDraftTranslator.toDraft(activityNew));
    }

    public void deleteActivity(Long activityId)
    {
        Activity activity = findActivityById(activityId);
        authenticateLoggedInUserEqualsObjectOwner(activity.getOrganizer().getUsername());
        if (activity.getParticipants().isEmpty())
        {
            activity.getApplicants().stream()
                    .map(User::getEmailAddress)
                    .forEach(e -> emailService.sendSimpleMessage(e, subject, text + activity.getTitle()));
            activityRepository.deleteById(activityId);
        } else
            ExceptionThrower.badRequest(ErrorMessage.DELETE_ACTIVITY_WITH_PARTICIPANTS_NOT_POSSIBLE);
    }

    public List<Draft> getDraftsOfOrganizer()
    {
        return draftRepository.findByOrganizerUsername(getAuthenticatedName());
    }

    public Draft findDraftById(Long id)
    {
        Optional<Draft> oDraft = draftRepository.findById(id);
        Draft draft = new Draft();
        if (oDraft.isEmpty())
            ExceptionThrower.badRequest(ErrorMessage.NO_MATCHING_DRAFT_FOUND);
        else
            draft = oDraft.get();
        authenticateLoggedInUserEqualsObjectOwner(draft.getOrganizerUsername());
        return draft;
    }

    public Draft postDraft(Draft draft)
    {
        draft.setOrganizerUsername(getAuthenticatedName());
        return draftRepository.save(draft);
    }

    public Draft editDraft(Draft draftNew)
    {
        findDraftById(draftNew.getId());
        return postDraft(draftNew);
    }

    public Activity saveDraftAsActivity(Long draftId)
    {
        Draft draft = findDraftById(draftId);
        return saveActivity(draft);
    }

    public void deleteDraft(Long draftId)
    {
        findDraftById(draftId);
        draftRepository.deleteById(draftId);
    }

    private Activity saveActivity(Draft draft)
    {
        User user = new User();
        Activity activity = activityDraftTranslator.toActivity(draft);
        Optional<User> oUser = userRepository.findByUsername(getAuthenticatedName());
        if (oUser.isEmpty())
            ExceptionThrower.badRequest(ErrorMessage.LOGGED_IN_USER_NOT_MATCHING_REQUEST);
        else
            user = oUser.get();

        activity.setOrganizer(user);
        activity.setApplicants(new HashSet<>());
        activity.setParticipants(new HashSet<>());
        if (activity.isOpenEnd())
            activity.setEndDateTime(activity.getStartDateTime());
        else
        {
            if (!validateStartDateBeforeEndDate(activity))
                ExceptionThrower.badRequest(ErrorMessage.END_DATE_BEFORE_START_DATE);
        }
        draftRepository.delete(draft);
        return activityRepository.save(activity);
    }

    private boolean validateStartDateBeforeEndDate(Activity activity)
    {
        return activity.getStartDateTime().isBefore(activity.getEndDateTime())
                && !activity.getStartDateTime().equals(activity.getEndDateTime());
    }

    private String getAuthenticatedName()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void authenticateLoggedInUserEqualsObjectOwner(String organizerUsername)
    {
        if (!getAuthenticatedName().equals(organizerUsername))
            ExceptionThrower.badRequest(ErrorMessage.LOGGED_IN_USER_NOT_MATCHING_REQUEST);
    }
}
