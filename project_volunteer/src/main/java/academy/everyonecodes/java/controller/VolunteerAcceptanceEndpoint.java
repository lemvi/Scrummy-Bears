package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.service.VolunteerAcceptanceService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class VolunteerAcceptanceEndpoint
{
    private final VolunteerAcceptanceService volunteerAcceptanceService;

    public VolunteerAcceptanceEndpoint(VolunteerAcceptanceService volunteerAcceptanceService)
    {
        this.volunteerAcceptanceService = volunteerAcceptanceService;
    }

    @PutMapping("/activities/{activityId}/accept/{userId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity acceptVolunteer(@PathVariable Long activityId, @PathVariable Long userId)
    {
        return volunteerAcceptanceService.acceptVolunteer(activityId, userId);
    }
}
