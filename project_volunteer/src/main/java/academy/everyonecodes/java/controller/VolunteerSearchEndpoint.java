package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.service.VolunteerSearchService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/volunteer/search")
@Secured({"ROLE_VOLUNTEER"})
public class VolunteerSearchEndpoint {

	private final VolunteerSearchService volunteerSearchService;

	public VolunteerSearchEndpoint(VolunteerSearchService volunteerSearchService) {
		this.volunteerSearchService = volunteerSearchService;
	}

	@GetMapping
	List<Activity> getAllActivities() {
		return volunteerSearchService.getAllActivities();
	}
}
