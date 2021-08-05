package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class LoginService {

	// TODO: Replace with Value from yml file
	public static final int MAX_FAILED_ATTEMPTS = 5;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InvalidLoginCountRepository invalidLoginCountRepository;

	public void increaseFailedAttempts(String username) {
		System.out.println("Trying to find user");
		Optional<User> optUser = userRepository.findByUsername(username);
		if (optUser.isPresent()) {
			InvalidLoginCount invalidLoginCount = null;

			System.out.println("User found");
			User user = optUser.get();
			Optional<InvalidLoginCount> optInvalidLoginCount = invalidLoginCountRepository.findById(user.getId());

			if (optInvalidLoginCount.isEmpty()) {
				invalidLoginCount = createInvalidLoginCount(user);
			} else {
				invalidLoginCount = optInvalidLoginCount.get();
			}
			increaseFailedAttempts(invalidLoginCount);
		}
	}

	public void increaseFailedAttempts(InvalidLoginCount invalidLoginCount) {
		// TODO: Remove following line after testing
		System.out.println("Increased Failed Attempts");

		invalidLoginCount.setInvalidAttempts(invalidLoginCount.getInvalidAttempts() + 1);
		invalidLoginCountRepository.save(invalidLoginCount);
	}

	public void resetFailedAttemptsIfNecessary() {
		// TODO: Link with logged in User
		//TODO: Remove following line after testing
		System.out.println("Checking Login attempts and resetting.");
	}

	private void resetFailedAttempts(InvalidLoginCount invalidLoginCount) {
		// TODO: Remove following line after testing
		System.out.println("Reset Failed Attempts");

		invalidLoginCount.setInvalidAttempts(0);
		invalidLoginCountRepository.save(invalidLoginCount);
	}

	public void sendTooManyFailedLoginAttemptsEmail() {
		// TODO: Replace with actual Email
		System.out.println("Sent Email");
	}

	private InvalidLoginCount createInvalidLoginCount(User user) {
		return invalidLoginCountRepository.save(new InvalidLoginCount(user, 0));
	}
}
