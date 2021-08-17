package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerSearchService {

	private final ActivityRepository activityRepository;

	public VolunteerSearchService(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	public List<Activity> getAllActivities() {
		return activityRepository.findAll();
	}
}
