package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.service.VolunteerApplicationService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class VolunteerApplicationEndpoint {

    private final VolunteerApplicationService volunteerApplicationService;

    public VolunteerApplicationEndpoint(VolunteerApplicationService volunteerApplicationService) {
        this.volunteerApplicationService = volunteerApplicationService;
    }

    @PostMapping("activities/{activityId}/{userId}")
    @Secured({"ROLE_VOLUNTEER"})
    public String applyForActivityWithEmailAndText(@PathVariable Long activityId,
                                                   @PathVariable Long userId, @RequestBody String EmailText) {
        volunteerApplicationService.applyForActivityWithEmailAndText(activityId, userId, EmailText);
        return EmailText;
    }
}
