package academy.everyonecodes.java.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvalidLoginCountRepository extends JpaRepository<InvalidLoginCount, Long> {
}
