package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.ErrorMessage;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.DraftRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService
{
    private final ActivityRepository activityRepository;
    private final EntityManager entityManager;
    private final ActivityDraftTranslator activityDraftTranslator;
    private final DraftRepository draftRepository;
    private final UserRepository userRepository;
    private final EmailServiceImpl emailServiceImpl;
    private final String subject;
    private final String text;



    public ActivityService(ActivityRepository activityRepository, EntityManager entityManager, ActivityDraftTranslator activityDraftTranslator, DraftRepository draftRepository, UserRepository userRepository, EmailServiceImpl emailServiceImpl, @Value("${activityDeletedEmail.subject}") String subject, @Value("${activityDeletedEmail.text}") String text)
    {
        this.activityRepository = activityRepository;
        this.entityManager = entityManager;
        this.activityDraftTranslator = activityDraftTranslator;
        this.draftRepository = draftRepository;
        this.userRepository = userRepository;
         this.emailServiceImpl = emailServiceImpl;
        this.subject = subject;
        this.text = text;
    }

    public List<Activity> getAllActivities(boolean isDeleted)
    {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedActivityFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Activity> activities =  activityRepository.findAll();
        session.disableFilter("deletedActivityFilter");
        return activities;
    }

    public Iterable<Activity> getDeletedActivities(boolean isDeleted, String organizerUsername){
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedActivityFilter");
        filter.setParameter("isDeleted", isDeleted);
        Iterable<Activity> activities =  activityRepository.findByOrganizer_Username(organizerUsername);
        activities.forEach(activity -> authenticateLoggedInUserEqualsObjectOwner(organizerUsername));
        session.disableFilter("deletedActivityFilter");
        return activities;
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

    public Activity editActivity(Activity activityDetails)
    {
        Activity activityNew = findActivityById(activityDetails.getId());
        authenticateLoggedInUserEqualsObjectOwner(activityDetails.getOrganizer().getUsername());
        if (activityDetails.isDeleted())
            ExceptionThrower.badRequest(ErrorMessage.EDIT_DELETED_ACTIVITY_NOT_POSSIBLE);
        if (!activityDetails.getParticipants().isEmpty() || !activityDetails.getApplicants().isEmpty())
            ExceptionThrower.badRequest(ErrorMessage.EDIT_ACTIVITY_WITH_APPLICANTS_OR_PARTICIPANTS_NOT_POSSIBLE);

        activityNew.getId();
        activityNew.setTitle(activityDetails.getTitle());
        activityNew.setDescription(activityDetails.getDescription());
        activityNew.setRecommendedSkills(activityDetails.getRecommendedSkills());
        activityNew.setCategories(activityDetails.getCategories());
        activityNew.setStartDateTime(activityDetails.getStartDateTime());
        activityNew.setEndDateTime(activityDetails.getEndDateTime());
        activityNew.setOpenEnd(activityDetails.isOpenEnd());
        activityNew.setOrganizer(activityDetails.getOrganizer());
        activityNew.setApplicants(activityDetails.getApplicants());
        activityNew.setParticipants(activityDetails.getParticipants());
        activityNew.setDeleted(activityDetails.isDeleted());

        activityRepository.deleteById(activityDetails.getId());

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
                    .forEach(e -> emailServiceImpl.sendSimpleMessage(e, subject, text + activity.getTitle()));
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

    public Draft editDraft(Draft draftDetails)
    {
        Draft newDraft = findDraftById(draftDetails.getId());
        newDraft.setId(draftDetails.getId());
        newDraft.setTitle(draftDetails.getTitle());
        newDraft.setDescription(draftDetails.getDescription());
        newDraft.setRecommendedSkills(draftDetails.getRecommendedSkills());
        newDraft.setCategories(draftDetails.getCategories());
        newDraft.setStartDateTime(draftDetails.getStartDateTime());
        newDraft.setEndDateTime(draftDetails.getEndDateTime());
        newDraft.setOpenEnd(draftDetails.isOpenEnd());
        newDraft.setOrganizerUsername(draftDetails.getOrganizerUsername());

        return postDraft(newDraft);
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
