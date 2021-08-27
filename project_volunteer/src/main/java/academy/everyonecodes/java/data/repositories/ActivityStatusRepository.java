package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityStatusRepository extends JpaRepository<ActivityStatus, Long> {

    Optional<ActivityStatus> findByActivity(Activity activity);
    Optional<ActivityStatus> findByActivity_Id(Long activityId);

}
