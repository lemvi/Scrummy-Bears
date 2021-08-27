package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.repositories.ActivityStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityStatusService {
    private final ActivityStatusRepository activityStatusRepository;

    public ActivityStatusService(ActivityStatusRepository activityStatusRepository) {
        this.activityStatusRepository = activityStatusRepository;
    }

    public Optional<ActivityStatus> getActivityStatus(Long activityId) {
        return activityStatusRepository.findById(activityId);
    }
    public Optional<ActivityStatus> findByActivity_id(Long activityId) {
        return activityStatusRepository.findByActivity_Id(activityId);
    }
    public ActivityStatus changeActivityStatus(Activity activity, Status status) {
        Optional<ActivityStatus> oStatus = activityStatusRepository.findById(activity.getId());
        ActivityStatus activityStatus = new ActivityStatus();
        if (oStatus.isEmpty()){
            return activityStatusRepository.save(new ActivityStatus(activity, status));
        }
        activityStatus = oStatus.get();
        activityStatus.setStatus(status);

        return activityStatusRepository.save(activityStatus);
    }
}
