package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
