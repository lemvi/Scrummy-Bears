package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.service.ActivityService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/activities")
public class ActivityEndpoint
{
    private final ActivityService activityService;

    public ActivityEndpoint(ActivityService activityService)
    {
        this.activityService = activityService;
    }

    @PostMapping
    @Secured({"ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    Activity postActivity(@RequestBody @Valid Activity activity)
    {
        return activityService.postActivity(activity);
    }
}
