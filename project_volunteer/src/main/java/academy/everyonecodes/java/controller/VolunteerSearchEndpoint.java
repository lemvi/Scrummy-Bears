package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.service.ActivityService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
 // TODO class is obsolete -> ActivityEndpoint includes this feature!!
@RestController
@RequestMapping("/volunteer/search")
@Secured({"ROLE_VOLUNTEER"})
public class VolunteerSearchEndpoint {

	private final ActivityService activityService;

	public VolunteerSearchEndpoint(ActivityService activityService) {
		this.activityService = activityService;
	}

	@GetMapping
	List<Activity> getAllActivities() {
		return activityService.getAllActivities();
	}
}
