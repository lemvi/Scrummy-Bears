package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.service.StatusService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class StatusEndpoint
{
    private final StatusService statusService;

    public StatusEndpoint(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @PutMapping("/activities/{activityId/accept/{userId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity acceptVolunteer(@PathVariable Long activityId, @PathVariable Long userId)
    {
        return statusService.acceptVolunteer(activityId, userId);
    }
}
