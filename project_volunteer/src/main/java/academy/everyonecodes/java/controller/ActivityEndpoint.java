package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.service.ActivityService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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
    @Secured({"ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    Activity postActivity(@RequestBody Draft draft)
    {
        return activityService.postActivity(draft);
    }

    @GetMapping("/activities")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    List<Activity> getActivitiesOfOrganizer()
    {
        return activityService.getActivitiesOfOrganizer();
    }

    @PostMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    Draft postDraft(@RequestBody Draft draft)
    {
        return activityService.postDraft(draft);
    }

    @GetMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    List<Draft> getDraftsOfOrganizer()
    {
        return activityService.getDraftsOfOrganizer();
    }

    @PutMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    Draft editDraft(@RequestBody Draft draft)
    {
        return activityService.editDraft(draft).orElse(null);
    }

    @PutMapping("/drafts/{draftId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    Activity saveDraftAsActivity(@PathVariable Long draftId)
    {
        return activityService.saveDraftAsActivity(draftId).orElse(null);
    }
}
