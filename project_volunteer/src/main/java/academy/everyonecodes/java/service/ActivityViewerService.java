package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_individualOrganization;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_volunteer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<ActivityViewDTO_volunteer> getListOfActivityViewDTOsForSpecificVolunteer(String username)
    {
        checkIfLoggedInUserIsMatchingRequest(username);
        User user = getUserFromDB(username);
        List<Activity> activities = getAllActivitiesForSpecificVolunteer(user);
        return activities.stream()
                .map(activity -> activityViewDTOCreator.createActivityViewDTO_forVolunteer(activity, user))
                .collect(Collectors.toList());
    }

    public List<ActivityViewDTO_individualOrganization> getListOfActivityViewDTOsForSpecificIndividualOrOrganization(String username) {
        checkIfLoggedInUserIsMatchingRequest(username);
        User user = getUserFromDB(username);
        List<ActivityDraft> activities = getAllActivitiesAndDraftsForIndividualOrOrganization(user);
        return activities.stream()
                .map(activity -> activityViewDTOCreator.createActivityViewDTO_forIndividualOrOrganization(activity, user))
                .collect(Collectors.toList());
    }

    private User getUserFromDB(String username) {
        Optional<User> oUser = userService.findByUsername(username);
        if (oUser.isEmpty())
            ExceptionThrower.badRequest(ErrorMessage.USERNAME_NOT_FOUND);
        User user = oUser.get();
        return user;
    }

    private void checkIfLoggedInUserIsMatchingRequest(String username) {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();;
        if (!currentPrincipalName.equals(username))
            ExceptionThrower.badRequest(ErrorMessage.LOGGED_IN_USER_NOT_MATCHING_REQUEST);
    }

    private List<ActivityDraft> getAllActivitiesAndDraftsForIndividualOrOrganization(User user) {
        List<ActivityDraft> activityDraftList = new ArrayList<>();
        getAllActivitiesForSpecificIndividualOrOrganization(user).forEach(activity -> activityDraftList.add(activity));
        getAllDraftsOfOrganizer().forEach(draft -> activityDraftList.add(draft));
        return activityDraftList;
    }


    public List<Activity> getAllActivitiesForSpecificVolunteer(User user)
    {
        return  getAllActivities().stream()
                .filter(activity -> activity.getApplicants().contains(user) || activity.getParticipants().contains(user))
                .collect(Collectors.toList());
    }

    public List<Activity> getAllActivitiesForSpecificIndividualOrOrganization(User user)
    {
        return  getAllActivities().stream()
                .filter(activity -> activity.getOrganizer().equals(user))
                .collect(Collectors.toList());
    }

    private List<Activity> getAllActivities() {
        return activityService.getAllActivities(false);
    }

    private List<Draft> getAllDraftsOfOrganizer() {
        return activityService.getDraftsOfOrganizer();
    }

    public List<ActivityViewDTO_volunteer> getListOfActivityViewDTOsForSpecificVolunteer(String username, Status status)
    {
        List<ActivityViewDTO_volunteer> activityViewDTOVolunteers = getListOfActivityViewDTOsForSpecificVolunteer(username);
        return activityViewDTOVolunteers.stream()
                .filter(activityViewDTO -> activityViewDTO.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
