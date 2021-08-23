package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.DraftRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
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
    private final UserService userService;

    private final String endDateBeforeStartDate;

    public ActivityService(ActivityRepository activityRepository, ActivityDraftTranslator activityDraftTranslator, DraftRepository draftRepository, UserRepository userRepository, UserService userService, @Value("${errorMessages.endDateBeforeStartDate}") String endDateBeforeStartDate)
    {
        this.activityRepository = activityRepository;
        this.activityDraftTranslator = activityDraftTranslator;
        this.draftRepository = draftRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.endDateBeforeStartDate = endDateBeforeStartDate;
    }

    public Activity postActivity(Draft draft)
    {
        return saveActivity(draft);
    }

    public Draft postDraft(Draft draft)
    {
        draft.setOrganizer(getAuthenticatedName());
        return draftRepository.save(draft);
    }

    public List<Draft> getDraftsOfOrganizer()
    {
        return draftRepository.findByOrganizer(getAuthenticatedName());
    }

    public Optional<Draft> editDraft(Draft draft)
    {
        Optional<Draft> oDraft = draftRepository.findById(draft.getId());
        if (oDraft.isPresent())
        {
            return Optional.of(postDraft(draft));
        }
        return Optional.empty();
    }

    public Optional<Activity> saveDraftAsActivity(Long draftId)
    {
        Optional<Draft> oDraft = draftRepository.findById(draftId);
        if (oDraft.isPresent())
            return Optional.of(saveActivity(oDraft.get()));
        return Optional.empty();
    }

    public List<Activity> getActivitiesOfOrganizer()
    {
        return activityRepository.findByOrganizer_Username(getAuthenticatedName());
    }

    private Activity saveActivity(Draft draft)
    {
        Activity activity = activityDraftTranslator.toActivity(draft);
        draftRepository.delete(draft);

        Optional<User> oUser = userRepository.findByUsername(getAuthenticatedName());
        oUser.ifPresent(activity::setOrganizer);
        activity.setApplicants(new HashSet<>());
        activity.setParticipants(new HashSet<>());
        if (activity.isOpenEnd())
            activity.setEndDateTime(activity.getStartDateTime());
        else {
            if (!validateStartDateBeforeEndDate(activity)) userService.throwBadRequest(endDateBeforeStartDate);
        }
        return activityRepository.save(activity);
    }

    private String getAuthenticatedName()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean validateStartDateBeforeEndDate(Activity activity)
    {
        return activity.getStartDateTime().isBefore(activity.getEndDateTime())
                && !activity.getStartDateTime().equals(activity.getEndDateTime());
    }
}
