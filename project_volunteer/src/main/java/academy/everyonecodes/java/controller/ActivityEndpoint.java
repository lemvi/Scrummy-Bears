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

    @GetMapping("/activities")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION", "ROLE_VOLUNTEER"})
    List<Activity> getAllActivities()
    {
        return activityService.getAllActivities(false);
    }

    @GetMapping("/activities/{organizerUsername}/deleted")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Iterable<Activity> getDeletedActivities(@PathVariable String organizerUsername)
    {
        return activityService.getDeletedActivities(true, organizerUsername);
    }

    @GetMapping("/activities/{organizerUsername}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION", "ROLE_VOLUNTEER"})
    List<Activity> getActivitiesOfOrganizer(@PathVariable String organizerUsername)
    {
        return activityService.getActivitiesOfOrganizer(organizerUsername);
    }

    @GetMapping("/activities/find/{id}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION", "ROLE_VOLUNTEER"})
    Activity findActivityById(@PathVariable Long id)
    {
        return activityService.findActivityById(id);
    }

    @PostMapping("/activities/create")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity postActivity(@RequestBody Draft draft)
    {
        return activityService.postActivity(draft);
    }

    @PutMapping("/activities/edit")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity editActivity(@RequestBody Activity activityDetails)
    {
        return activityService.editActivity(activityDetails);
    }

    @DeleteMapping("/activities/{activityId}/delete")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    void deleteActivity(@PathVariable Long activityId)
    {
        activityService.deleteActivity(activityId);
    }

    @GetMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<Draft> getDraftsOfOrganizer()
    {
        return activityService.getDraftsOfOrganizer();
    }

    @GetMapping("/drafts/find/{draftId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Draft findDraftById(@PathVariable Long draftId)
    {
        return activityService.findDraftById(draftId);
    }

    @PostMapping("/drafts/create")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Draft postDraft(@RequestBody Draft draft)
    {
        return activityService.postDraft(draft);
    }

    @PutMapping("/drafts/edit")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Draft editDraft(@RequestBody Draft draftDetails)
    {
        return activityService.editDraft(draftDetails);
    }

    @PutMapping("/drafts/{draftId}/save_as_activity")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity saveDraftAsActivity(@PathVariable Long draftId)
    {
        return activityService.saveDraftAsActivity(draftId);
    }

    @DeleteMapping("/drafts/{draftId}/delete")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    void deleteDraft(@PathVariable Long draftId)
    {
        activityService.deleteDraft(draftId);
    }


}
