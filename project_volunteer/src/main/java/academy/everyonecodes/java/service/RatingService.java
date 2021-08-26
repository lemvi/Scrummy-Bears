package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService
{

    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final ActivityStatusService activityStatusService;
    private final EmailServiceImpl emailService;
    private final String ratedSubject;
    private final String ratedText;

    public RatingService(RatingRepository ratingRepository,
                         UserService userService,
                         ActivityService activityService,
                         ActivityStatusService activityStatusService,
                         EmailServiceImpl emailService,
                         @Value("${ratingEmail.subject}") String ratedSubject,
                         @Value("${ratingEmail.text}") String ratedText)
    {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.activityService = activityService;
        this.activityStatusService = activityStatusService;
        this.emailService = emailService;
        this.ratedSubject = ratedSubject;
        this.ratedText = ratedText;
    }

    public double calculateAverageUserRating(Long userId)
    {
        List<Rating> ratings = ratingRepository.findByUser_Id(userId);
        return ratings.stream()
                .map(Rating::getRating)
                .mapToDouble(rating -> rating)
                .average()
                .orElse(Double.NaN);
    }

    public Optional<Rating> findByActivityAndUser(Activity activity, User user) {
        return ratingRepository.findByActivityAndUser(activity, user);
    }

    private Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating rateUserForActivity(Rating rating, Long activityId)
    {
        Activity activity = activityService.findActivityById(activityId);
        ActivityStatus activityStatus = getActivityStatus(activityId);

        if (!activityStatus.getStatus().equals(Status.COMPLETED)) {
            ExceptionThrower.badRequest(ErrorMessage.ACTIVITY_NOT_COMPLETED_YET);
        }

        User user = getCurrentlyLoggedInUser();
        User userToRate = getUserToRate(activity, user);

        rating.setActivity(activity);
        rating.setUser(userToRate);
        rating.setActivityId(activityId);
        rating.setUserId(userToRate.getId());

        sendEmailToRatedUser(userToRate, activity, rating);

        return saveRating(rating);
    }


    private User getUserToRate(Activity activity, User user) {
        var oUserToRate = determineUserToRate(checkUsersInvolvementInActivity(user, activity), activity);
        if (oUserToRate.isEmpty()) {
            ExceptionThrower.badRequest(ErrorMessage.USER_NOT_INVOLVED_IN_ACTIVITY);
        }
        return oUserToRate.get();
    }

    private User getCurrentlyLoggedInUser() {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        var oUser = userService.findByUsername(currentPrincipalName);
        if (oUser.isEmpty()) {
            ExceptionThrower.badRequest(ErrorMessage.USERNAME_NOT_FOUND);
        }

        User user = oUser.get();
        return user;
    }

    private ActivityStatus getActivityStatus(Long activityId) {
        var oActivityStatus = activityStatusService.findByActivity_id(activityId);
        if (oActivityStatus.isEmpty()) {
            ExceptionThrower.badRequest(ErrorMessage.NO_STATUS_FOUND);
        }

        ActivityStatus activityStatus = oActivityStatus.get();
        return activityStatus;
    }

    private String checkUsersInvolvementInActivity(User user, Activity activity) {
        var participants = activity.getParticipants();
        var organizer = activity.getOrganizer();
        if (participants.contains(user)) {
            return "participant";
        }
        if (organizer.equals(user)) {
            return "organizer";
        }
        return "no involvement";
    }

    private Optional<User> determineUserToRate(String involvementString, Activity activity) {
        if (involvementString.equals("participant")) {
            return Optional.ofNullable(activity.getOrganizer());
        }
        if (involvementString.equals("organizer")) {
            return activity.getParticipants().stream().findFirst();
        }
        return Optional.empty();
    }


    private void sendEmailToRatedUser(User ratedUser, Activity activity, Rating rating) {
        String text = "\n Activity Id: " + activity.getId() +
                "\n Activity title: " + activity.getTitle() +
                "\n Rating: " + rating.getRating() +
                "\n Feedback: " + rating.getFeedback();
        emailService.sendSimpleMessage(ratedUser.getEmailAddress(), ratedSubject, ratedText + text);
    }

}
