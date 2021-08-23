package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.ActivityViewDTO;
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

    @GetMapping("/{username}/activities")
    @Secured("ROLE_VOLUNTEER")
    List<ActivityViewDTO> getMyActivities_asVolunteer(@PathVariable String username) {
        return activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username);
    }

    @GetMapping("/{username}/activities/pending")
    @Secured("ROLE_VOLUNTEER")
    List<ActivityViewDTO> getMyActivities_asVolunteer_pending(@PathVariable String username) {
        return activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer_pending(username);
    }

    @GetMapping("/{username}/activities/completed")
    @Secured("ROLE_VOLUNTEER")
    List<ActivityViewDTO> getMyActivities_asVolunteer_completed(@PathVariable String username) {
        return activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer_completed(username);
    }

    @GetMapping("/{username}/activities/active")
    @Secured("ROLE_VOLUNTEER")
    List<ActivityViewDTO> getMyActivities_asVolunteer_active(@PathVariable String username) {
        return activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer_active(username);
    }
}
