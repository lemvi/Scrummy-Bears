package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAddress(String emailAddress);
}
