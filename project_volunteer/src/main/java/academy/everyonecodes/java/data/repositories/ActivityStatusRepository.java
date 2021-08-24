package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityStatusRepository extends JpaRepository<ActivityStatus, Long> {
}
