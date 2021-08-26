package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.service.ActivityCompletionService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/activities")
public class ActivityCompletionEndpoint {
    private final ActivityCompletionService activityCompletionService;

    public ActivityCompletionEndpoint(ActivityCompletionService activityCompletionService) {
        this.activityCompletionService = activityCompletionService;
    }

    @PutMapping("/complete/{activityId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    ActivityStatus completeActivity(@PathVariable Long activityId, @RequestBody @Valid Rating rating) throws MessagingException {
        return activityCompletionService.completeActivity(activityId, rating).orElse(null);
    }
}
