package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.service.ApplicationService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class ApplicationEndpoint {

    private final ApplicationService applicationService;

    public ApplicationEndpoint(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("activities/{activityId}/{userId}")
    @Secured({"ROLE_VOLUNTEER"})
    public String applyForActivityWithEmailAndText(@PathVariable Long activityId,
                                                   @PathVariable Long userId, @Valid @RequestBody String EmailText) {
        applicationService.applyForActivityWithEmailAndText(activityId, userId, EmailText);
        return EmailText;
    }
}
