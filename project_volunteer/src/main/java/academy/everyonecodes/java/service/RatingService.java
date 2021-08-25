package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.RatingRepository;
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
    private final String activityDoesNotExist;
    private final String usernameNotFound;
    private final String userNotInvolvedInActivity;
    private final String noStatusFound;
    private final String activityNotCompletedYet;

    public RatingService(RatingRepository ratingRepository,
                         UserService userService,
                         ActivityService activityService,
                         ActivityStatusService activityStatusService,
                         @Value("${errorMessages.activityDoesNotExist}") String activityDoesNotExist,
                         @Value("${errorMessages.usernameNotFound}") String usernameNotFound,
                         @Value("${errorMessages.userNotInvolvedInActivity}") String userNotInvolvedInActivity,
                         @Value("${errorMessages.noStatusFound}") String noStatusFound,
                         @Value("${errorMessages.activityNotCompletedYet}") String activityNotCompletedYet)
    {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.activityService = activityService;
        this.activityStatusService = activityStatusService;
        this.activityDoesNotExist = activityDoesNotExist;
        this.usernameNotFound = usernameNotFound;
        this.userNotInvolvedInActivity = userNotInvolvedInActivity;
        this.noStatusFound = noStatusFound;
        this.activityNotCompletedYet = activityNotCompletedYet;
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

    public Rating rateUserForActivity(Rating rating, Long activityId)
    {
        Activity activity = getActivity(activityId);
        ActivityStatus activityStatus = getActivityStatus(activityId);

        if (!activityStatus.getStatus().equals(Status.COMPLETED)) {
            userService.throwBadRequest(activityNotCompletedYet);
        }


        User user = getCurrentlyLoggedInUser();
        User userToRate = getUserToRate(activity, user);

//        rating.setActivity(activity);
//        rating.setUser(userToRate);
        rating.setActivityId(activityId);
        rating.setUserId(userToRate.getId());

        return saveRating(rating);
    }

    private User getUserToRate(Activity activity, User user) {
        var oUserToRate = determineUserToRate(checkUsersInvolvementInActivity(user, activity), activity);
        if (oUserToRate.isEmpty()) {
            userService.throwBadRequest(userNotInvolvedInActivity);
        }
        return oUserToRate.get();
    }

    private User getCurrentlyLoggedInUser() {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        var oUser = userService.findByUsername(currentPrincipalName);
        if (oUser.isEmpty()) {
            userService.throwBadRequest(usernameNotFound);
        }

        User user = oUser.get();
        return user;
    }

    private Activity getActivity(Long activityId) {
        var oActivity = activityService.findById(activityId);
        if (oActivity.isEmpty()) {
            userService.throwBadRequest(activityDoesNotExist + activityId);
        }

        Activity activity = oActivity.get();
        return activity;
    }

    private ActivityStatus getActivityStatus(Long activityId) {
        var oActivityStatus = activityStatusService.findByActivity_id(activityId);
        if (oActivityStatus.isEmpty()) {
            userService.throwBadRequest(noStatusFound);
        }

        ActivityStatus activityStatus = oActivityStatus.get();
        return activityStatus;
    }

    public Rating saveRating(Rating rating) {
//        rating.setUserId(user.getId());
//        rating.setActivityId(activity.getId());
//        rating.setUser(user);
//        rating.setActivity(activity);
        return ratingRepository.save(rating);
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

}
