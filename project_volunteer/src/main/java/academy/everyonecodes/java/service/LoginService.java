package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class LoginService {

	@Value("${constants.how-many-failed-logins-cause-email-warning}")
	private int MAX_FAILED_ATTEMPTS;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InvalidLoginCountRepository invalidLoginCountRepository;

	@Autowired
	private EmailServiceImpl emailService;

	public void increaseFailedAttempts(String username) {
		Optional<User> optUser = userRepository.findByUsername(username);
		if (optUser.isPresent()) {
			// Initializing invalidLoginCount to be able to change it in loop later
			InvalidLoginCount invalidLoginCount = null;

			User user = optUser.get();
			Optional<InvalidLoginCount> optInvalidLoginCount = invalidLoginCountRepository.findById(user.getId());

			if (optInvalidLoginCount.isEmpty()) {
				invalidLoginCount = createInvalidLoginCount(user);
			} else {
				invalidLoginCount = optInvalidLoginCount.get();
			}

			int newInvalidAttempts = invalidLoginCount.getInvalidAttempts() + 1;
			if (newInvalidAttempts == 5) {
				// TODO: Check What to do with exception
				try {
					sendTooManyFailedLoginAttemptsEmail(user.getEmailAddress());
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}

			invalidLoginCount.setInvalidAttempts(newInvalidAttempts);
			invalidLoginCountRepository.save(invalidLoginCount);
		}
	}

	public void resetFailedAttemptsIfNecessary() {
		// TODO: Link with logged in User
		// TODO: Remove following line after testing
		System.out.println("Checking Login attempts and resetting.");
	}

	private void resetFailedAttempts(InvalidLoginCount invalidLoginCount) {
		// TODO: Remove following line after testing
		System.out.println("Reset Failed Attempts");

		invalidLoginCount.setInvalidAttempts(0);
		invalidLoginCountRepository.save(invalidLoginCount);
	}

	private void sendTooManyFailedLoginAttemptsEmail(String emailAdress) throws MessagingException {
		emailService.sendMessageWithAttachment(emailAdress);
	}

	private InvalidLoginCount createInvalidLoginCount(User user) {
		return invalidLoginCountRepository.save(new InvalidLoginCount(user, 0));
	}
}
