package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_individualOrganization;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_volunteer;
import academy.everyonecodes.java.service.ActivityViewerService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ActivityViewerEndpoint {
    private final ActivityViewerService activityViewerService;

    public ActivityViewerEndpoint(ActivityViewerService activityViewerService) {
        this.activityViewerService = activityViewerService;
    }

    @GetMapping(value = "/{username}/activities", params = "volunteer")
    @Secured("ROLE_VOLUNTEER")
    List<ActivityViewDTO_volunteer> getMyActivities_asVolunteer(@PathVariable String username) {
        return activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username);
    }

    @GetMapping(value = "/{username}/activities/{status}", params = "volunteer")
    @Secured("ROLE_VOLUNTEER")
    List<ActivityViewDTO_volunteer> getMyActivities_asVolunteer(@PathVariable String username, @PathVariable Status status) {
        return activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username, status);
    }

    @GetMapping(value = "/{username}/activities", params = "individual")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<ActivityViewDTO_individualOrganization> getMyActivities_asIndividualOrOrganization(@PathVariable String username) {
        return activityViewerService.getListOfActivityViewDTOsForSpecificIndividualOrOrganization(username);
    }
}
