package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ActivityCompletionService {
    private final ActivityService activityService;
    private final RatingService ratingService;
    private final ActivityStatusService activityStatusService;
    private final EmailServiceImpl emailServiceImpl;
    private final String activitycompletedmessage;


    public ActivityCompletionService(ActivityService activityService, RatingService ratingService, ActivityStatusService activityStatusService, EmailServiceImpl emailServiceImpl, @Value(("${activityCompletedEmail.subjectAndText}")) String activitycompletedmessage) {
        this.activityService = activityService;
        this.ratingService = ratingService;
        this.activityStatusService = activityStatusService;
        this.emailServiceImpl = emailServiceImpl;
        this.activitycompletedmessage = activitycompletedmessage;
    }

    public Optional<ActivityStatus> completeActivity(Long activityId, Rating rating) {
        // Get Activity, return Empty Optional if not found

        Activity activity = activityService.findActivityById(activityId);

        User organizer = activity.getOrganizer();
        // Check if the logged in User is Owner of the Activity
        if (!organizer.getUsername().equals(getAuthenticatedName())) {
            ExceptionThrower.badRequest(ErrorMessage.USER_NOT_AUTHORIZED_TO_COMPLETE_ACTIVITY);
            //return Optional.empty();
        }

        Optional<ActivityStatus> optActivityStatus = activityStatusService.getActivityStatus(activityId);
        if (optActivityStatus.isPresent() && optActivityStatus.get().getStatus().equals(Status.COMPLETED)) {
            ExceptionThrower.badRequest(ErrorMessage.ACTIVITY_ALREADY_COMPLETED);
            //return Optional.empty();
        }

        // Check if Activity even had Participants
        if (activity.getParticipants().isEmpty()) {
            ExceptionThrower.badRequest(ErrorMessage.NO_PARTICIPANTS_FOR_ACTIVITY);
           // return Optional.empty();
        }

        // Set Activity to Completed
        ActivityStatus activityStatus = activityStatusService.changeActivityStatus(activity, Status.COMPLETED);

        // Check if Rating is done (or maybe sent with method), if both not, return Empty Optional (Maybe with Message?)
        // Save Rating and Feedback
        Rating ratingDone = ratingService.rateUserForActivity(rating, activityId);


        // Send Email including Rating and if there Feedback -> done in ratingService.rateUserForActivity(rating);?
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
        emailServiceImpl.sendSimpleMessage(emailParticipant, title, completeText);
        //emailService.sendMessageWithAttachment(emailParticipant, title, completeText, "project_volunteer/src/main/resources/Scrummy Bears Logo.jpg");

        return Optional.of(activityStatus);
    }
    private String getAuthenticatedName()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
