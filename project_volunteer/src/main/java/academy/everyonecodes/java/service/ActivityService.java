package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityService
{
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository)
    {
        this.activityRepository = activityRepository;
    }

    private Activity save(Activity activity)
    {
        return activityRepository.save(activity);
    }
    public Activity postActivity(Activity activity)
    {
        return save(activity);
    }
}
