package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.service.ActivityService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class ActivityEndpoint
{
    private final ActivityService activityService;
    public ActivityEndpoint(ActivityService activityService)
    {
        this.activityService = activityService;
    }

    @PostMapping("/activities")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity postActivity(@RequestBody Draft draft)
    {
        return activityService.postActivity(draft);
    }

    @GetMapping("/activities")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<Activity> getActivitiesOfOrganizer()
    {
        return activityService.getActivitiesOfOrganizer();
    }

    @PostMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Draft postDraft(@RequestBody Draft draft)
    {
        return activityService.postDraft(draft);
    }

    @GetMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<Draft> getDraftsOfOrganizer()
    {
        return activityService.getDraftsOfOrganizer();
    }

    @PutMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Draft editDraft(@RequestBody Draft draft)
    {
        return activityService.editDraft(draft).orElse(null);
    }

    @PutMapping("/drafts/{draftId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity saveDraftAsActivity(@PathVariable Long draftId)
    {
        return activityService.saveDraftAsActivity(draftId).orElse(null);
    }

    @PutMapping("/activities/complete/{activityId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    ActivityStatus completeActivity(@PathVariable Long activityId, @RequestBody @Valid Rating rating) {
        return activityService.completeActivity(activityId, rating).orElse(null);
    }
}
