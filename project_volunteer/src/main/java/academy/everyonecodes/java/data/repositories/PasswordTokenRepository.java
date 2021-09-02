package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
	Optional<PasswordToken> findByToken(String token);

	Optional<PasswordToken> findByUser_usernameAndValidAndActive(String username, boolean valid, boolean active);

	List<PasswordToken> findByUser_usernameAndValid(String username, boolean valid);
}
