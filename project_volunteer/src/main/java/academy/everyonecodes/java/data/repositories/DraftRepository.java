package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.Draft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftRepository extends JpaRepository<Draft, Long>
{
}
