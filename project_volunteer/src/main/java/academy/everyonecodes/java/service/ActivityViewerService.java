package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ErrorMessage;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO;
import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityViewerService
{
    private final ActivityViewDTOCreator activityViewDTOCreator;
    private final ActivityService activityService;
    private final UserService userService;

    public ActivityViewerService(ActivityViewDTOCreator activityViewDTOCreator,
                                 ActivityService activityService, UserService userService)
    {
        this.activityViewDTOCreator = activityViewDTOCreator;
        this.activityService = activityService;
        this.userService = userService;
    }

    public List<ActivityViewDTO> getListOfActivityViewDTOsForSpecificVolunteer(String username)
    {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!currentPrincipalName.equals(username))
            ExceptionThrower.badRequest(ErrorMessage.LOGGED_IN_USER_NOT_MATCHING_REQUEST);
        Optional<User> oUser = userService.findByUsername(username);
        if (oUser.isEmpty())
            ExceptionThrower.badRequest(ErrorMessage.USERNAME_NOT_FOUND);
        User user = oUser.get();
        List<Activity> activities = getAllActivitiesForSpecificVolunteer(user);
        return activities.stream()
                .map(activity -> activityViewDTOCreator.createActivityViewDTO_forVolunteer(activity, user))
                .collect(Collectors.toList());
    }

    public List<Activity> getAllActivitiesForSpecificVolunteer(User user)
    {
        List<Activity> activities = activityService.getAllActivities();
        return activities.stream()
                .filter(activity -> activity.getApplicants().contains(user) || activity.getParticipants().contains(user))
                .collect(Collectors.toList());

    }

    public List<ActivityViewDTO> getListOfActivityViewDTOsForSpecificVolunteer_pending(String username)
    {
        List<ActivityViewDTO> activityViewDTOS = getListOfActivityViewDTOsForSpecificVolunteer(username);
        return activityViewDTOS.stream()
                .filter(activityViewDTO -> activityViewDTO.getStatus().equals(Status.PENDING))
                .collect(Collectors.toList());
    }

    public List<ActivityViewDTO> getListOfActivityViewDTOsForSpecificVolunteer_completed(String username)
    {
        List<ActivityViewDTO> activityViewDTOS = getListOfActivityViewDTOsForSpecificVolunteer(username);
        return activityViewDTOS.stream()
                .filter(activityViewDTO -> activityViewDTO.getStatus().equals(Status.COMPLETED))
                .collect(Collectors.toList());
    }

    public List<ActivityViewDTO> getListOfActivityViewDTOsForSpecificVolunteer_active(String username)
    {
        List<ActivityViewDTO> activityViewDTOS = getListOfActivityViewDTOsForSpecificVolunteer(username);
        return activityViewDTOS.stream()
                .filter(activityViewDTO -> activityViewDTO.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());

    }
}
