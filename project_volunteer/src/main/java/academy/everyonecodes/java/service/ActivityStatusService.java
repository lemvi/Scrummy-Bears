package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.repositories.ActivityStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityStatusService {
    private final ActivityStatusRepository activityStatusRepository;

    public ActivityStatusService(ActivityStatusRepository activityStatusRepository) {
        this.activityStatusRepository = activityStatusRepository;
    }

    public ActivityStatus changeActivityStatus(Activity activity, Status status) {
        return activityStatusRepository.save(new ActivityStatus(activity.getId(), activity, status));
    }
}
