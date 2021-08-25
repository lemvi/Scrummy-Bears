package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.ActivityStatusRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StatusHandler {
    private final UserService userService;
    private final ActivityStatusRepository activityStatusRepository;

    public StatusHandler(UserService userService, ActivityStatusRepository activityStatusRepository) {
        this.userService = userService;
        this.activityStatusRepository = activityStatusRepository;
    }

    public Status getStatusForSpecificActivityAndVolunteer(Activity activity, Long userId) {
        Optional<User> optionalUser = userService.findById(userId);

        if (optionalUser.isEmpty()) {
            ExceptionThrower.badRequest(ErrorMessage.USERNAME_NOT_FOUND);
        }
        User user = optionalUser.get();

        if (checkIfCompleted(activity))
            return Status.COMPLETED;
        else if (checkIfUserIsParticipantAndActivityIsActive(activity, user))
            return Status.ACTIVE;
        else if (checkIfUserIsPending(activity, user))
            return Status.PENDING;
        else if (checkIfUserHasApplied(activity, user))
            return Status.APPLIED;



        return Status.NOT_SET;
    }

    private boolean checkIfCompleted(Activity activity) {
        return getStatusOfActivity(activity).equals(Status.COMPLETED);
    }

    private boolean checkIfUserIsParticipantAndActivityIsActive(Activity activity, User user) {
        return activity.getParticipants().contains(user) &&
                (LocalDateTime.now().isAfter(activity.getStartDateTime()));
    }

    private boolean checkIfUserIsPending(Activity activity, User user) {
        return activity.getParticipants().contains(user) && LocalDateTime.now().isBefore(activity.getStartDateTime());
    }

    private boolean checkIfUserHasApplied(Activity activity, User user) {
        return activity.getApplicants().contains(user);
    }

public Status getStatusOfActivity(Activity activity) {
    return activityStatusRepository.findByActivity(activity).orElse(Status.NOT_SET);
}


}
