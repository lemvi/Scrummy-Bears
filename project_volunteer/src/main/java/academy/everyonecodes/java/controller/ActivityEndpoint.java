package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.service.ActivityService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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

    @GetMapping("/activities")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION", "ROLE_VOLUNTEER"})
    List<Activity> getAllActivities()
    {
        return activityService.getAllActivities();
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

    @PostMapping("/activities")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity postActivity(@RequestBody Draft draft)
    {
        return activityService.postActivity(draft);
    }

    @PutMapping("/activities")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity editActivity(@RequestBody Activity activity)
    {
        return activityService.editActivity(activity);
    }

    @DeleteMapping("/activities/{activityId}")
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

    @GetMapping("/drafts/{id}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Draft findDraftById(@PathVariable Long id)
    {
        return activityService.findDraftById(id);
    }

    @PostMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Draft postDraft(@RequestBody Draft draft)
    {
        return activityService.postDraft(draft);
    }

    @PutMapping("/drafts")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Draft editDraft(@RequestBody Draft draft)
    {
        return activityService.editDraft(draft);
    }

    @PutMapping("/drafts/{draftId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    Activity saveDraftAsActivity(@PathVariable Long draftId)
    {
        return activityService.saveDraftAsActivity(draftId);
    }

    @DeleteMapping("/drafts/{draftId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    void deleteDraft(@PathVariable Long draftId)
    {
        activityService.deleteDraft(draftId);
    }

    @PutMapping("/activities/complete/{activityId}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    ActivityStatus completeActivity(@PathVariable Long activityId, @RequestBody @Valid Rating rating) throws MessagingException {
        return activityService.completeActivity(activityId, rating).orElse(null);
    }
}
