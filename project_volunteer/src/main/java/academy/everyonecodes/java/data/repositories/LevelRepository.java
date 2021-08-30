package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.Level;
import academy.everyonecodes.java.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long>
{
    Optional<Level> findByUser(User user);
}
