package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.repositories.ActivityStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityStatusService {
    private final ActivityStatusRepository activityStatusRepository;

    public ActivityStatusService(ActivityStatusRepository activityStatusRepository) {
        this.activityStatusRepository = activityStatusRepository;
    }

    public Optional<ActivityStatus> findByActivity_id(Long activityId) {
        return activityStatusRepository.findByActivity_Id(activityId);
    }
}
