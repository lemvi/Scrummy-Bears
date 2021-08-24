package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.Draft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DraftRepository extends JpaRepository<Draft, Long>
{
    List<Draft> findByOrganizerUsername(String organizer);
    Optional<Draft> findById (Long id);
}
