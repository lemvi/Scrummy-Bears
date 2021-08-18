package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StatusHandler {
    private final ActivityService activityService;
    private final UserService userService;
    private final String userNotFoundErrorMessage;

    public StatusHandler(ActivityService activityService, UserService userService, @Value("${errorMessages.usernameNotFound}") String userNotFoundErrorMessage) {
        this.activityService = activityService;
        this.userService = userService;
        this.userNotFoundErrorMessage = userNotFoundErrorMessage;
    }

    public Status getStatusForSpecificActivityAndVolunteer(Activity activity, Long userId) {
//        Optional<Activity> optionalActivity = activityService.findById(activity.getId());
        Optional<User> optionalUser = userService.findById(userId);
//        if (optionalActivity.isEmpty()) {
//            userService.throwBadRequest("activity not found");
//        }
        if (optionalUser.isEmpty()) {
            userService.throwBadRequest(userNotFoundErrorMessage);
        }
//        Activity activity = optionalActivity.get();
        User user = optionalUser.get();

        if (checkIfCompleted(activity)) {
            return Status.COMPLETED;
        } else if (checkIfUserIsPending(activity, user)) {
            return Status.PENDING;
        } else if (checkIfUserIsParticipant(activity, user))
            return Status.ACTIVE;

        return Status.NOT_SET;
    }

    private boolean checkIfUserIsPending(Activity activity, User user) {
        return activity.getApplicants().contains(user);
    }

    private boolean checkIfCompleted(Activity activity) {
        return LocalDateTime.now().isAfter(activity.getEndDateTime()) && !activity.isOpenEnd();
    }

    private boolean checkIfUserIsParticipant(Activity activity, User user) {
        return activity.getParticipants().contains(user);
    }
}
