package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.DraftRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final RatingService ratingService;
    private final String noMatchingActivityFound;
    private final String userNotAuthorizedToCompleteActivity;
    private final String endDateBeforeStartDate;
    private final String activitycompletedmessage;
    private final String activityAlreadyCompleted;
    private final String noParticipantsForActivity;
    private final ActivityStatusService activityStatusService;


    public ActivityService(ActivityRepository activityRepository, ActivityDraftTranslator activityDraftTranslator, DraftRepository draftRepository, UserRepository userRepository, UserService userService, RatingService ratingService, @Value("${errorMessages.noMatchingActivityFound}") String noMatchingActivityFound, @Value("${errorMessages.userNotAuthorizedToCompleteActivity}") String userNotAuthorizedToCompleteActivity, @Value("${errorMessages.endDateBeforeStartDate}") String endDateBeforeStartDate, @Value("${message.activitycompleted}") String activitycompletedmessage, @Value("${errorMessages.activityAlreadyCompleted}") String activityAlreadyCompleted, @Value("${errorMessages.noParticipantsForActivity}")String noParticipantsForActivity, ActivityStatusService activityStatusService)
    {
        this.activityRepository = activityRepository;
        this.activityDraftTranslator = activityDraftTranslator;
        this.draftRepository = draftRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.ratingService = ratingService;
        this.noMatchingActivityFound = noMatchingActivityFound;
        this.userNotAuthorizedToCompleteActivity = userNotAuthorizedToCompleteActivity;
        this.endDateBeforeStartDate = endDateBeforeStartDate;
        this.activitycompletedmessage = activitycompletedmessage;
        this.activityAlreadyCompleted = activityAlreadyCompleted;
        this.noParticipantsForActivity = noParticipantsForActivity;
        this.activityStatusService = activityStatusService;
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

    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
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

    public Optional<ActivityStatus> completeActivity(Long activityId, Rating rating) {
        // Get Activity, return Empty Optional if not found
        Optional<Activity> oActivity = activityRepository.findById(activityId);
        if (oActivity.isEmpty()) {
            userService.throwBadRequest(noMatchingActivityFound);
            return Optional.empty();
        }
        Activity activity = oActivity.get();
        User organizer = activity.getOrganizer();
        // Check if the logged in User is Owner of the Activity
        if (!organizer.getUsername().equals(getAuthenticatedName())) {
            userService.throwBadRequest(userNotAuthorizedToCompleteActivity);
            return Optional.empty();
        }

        Optional<ActivityStatus> optActivityStatus = activityStatusService.getActivityStatus(activityId);
        if (optActivityStatus.isPresent() && optActivityStatus.get().getStatus().equals(Status.COMPLETED)) {
            userService.throwBadRequest(activityAlreadyCompleted);
            return Optional.empty();
        }

        // Check if Activity even had Participants
        if (activity.getParticipants().isEmpty()) {
            userService.throwBadRequest(noParticipantsForActivity);
            return Optional.empty();
        }

        // Check if Rating is done (or maybe sent with method), if both not, return Empty Optional (Maybe with Message?)
        // Save Rating and Feedback
        Rating ratingDone = ratingService.rateUserForActivity(rating, activityId);

        // Set Activity to Completed
        ActivityStatus activityStatus = activityStatusService.changeActivityStatus(activity, Status.COMPLETED);

        // Send Email including Rating and if there Feedback -> done in ratingService.rateUserForActivity(rating);?
        // TODO: Link to refactored Email Method
        User participant = new ArrayList<>(activity.getParticipants()).get(0);
        String[] activityCompletedMessageArray = activitycompletedmessage.split(";");
        String emailParticipant = participant.getEmailAddress();
        String title = activityCompletedMessageArray[0] + activity.getTitle();
        String text = activityCompletedMessageArray[1] + participant.getUsername() + activityCompletedMessageArray[2] + activity.getTitle() + activityCompletedMessageArray[3] + ratingDone.getRating();
        String feedback = activityCompletedMessageArray[4] + ratingDone.getFeedback();
        String completeText = text;
        if ((null != ratingDone.getFeedback()) && (!ratingDone.getFeedback().isEmpty())) {
            completeText = text + "\n" + feedback;
        }

        return Optional.of(activityStatus);
    }
}
