package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.InvalidLoginCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvalidLoginCountRepository extends JpaRepository<InvalidLoginCount, Long> {
}
