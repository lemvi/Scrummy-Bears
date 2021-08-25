package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.InvalidLoginCountRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import academy.everyonecodes.java.security.BadCredentialsListener;
import academy.everyonecodes.java.security.SuccessfulLoginListener;
import academy.everyonecodes.java.security.UserPrincipal;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class LoginService {
	private static final Logger LOG_LOGIN_SUCCESS = LogManager.getLogger(SuccessfulLoginListener.class);
	private static final Logger LOG_LOGIN_FAILURE = LogManager.getLogger(BadCredentialsListener.class);

	@Value("${constants.how-many-failed-logins-cause-email-warning}")
	private int MAX_FAILED_ATTEMPTS;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private InvalidLoginCountRepository invalidLoginCountRepository;

	@Autowired
	private EmailServiceImpl emailService;

	private final String subject;
	private final String text;
	private final String pathToAttachment;

	public LoginService(@Value("${failedLoginEmail.subject}") String subject, @Value("${failedLoginEmail.text}") String text,
						@Value("${failedLoginEmail.pathToAttachment}") String pathToAttachment)
	{
		this.subject = subject;
		this.text = text;
		this.pathToAttachment = pathToAttachment;
	}

	public void runSuccessfulAuthenticationProcedure(AuthenticationSuccessEvent authenticationSuccessEvent) {
		UserPrincipal userPrincipal = (UserPrincipal) authenticationSuccessEvent.getAuthentication().getPrincipal();

		// Uncomment to get Login Attempt Details Remove after Testing
		// System.out.println("Credentials: " + authenticationSuccessEvent.getAuthentication().getCredentials());
		// System.out.println("Principal: " + authenticationSuccessEvent.getAuthentication().getPrincipal());
		// System.out.println("Authorities: " + authenticationSuccessEvent.getAuthentication().getAuthorities());
		// System.out.println("Name: " + authenticationSuccessEvent.getAuthentication().getName());
		// System.out.println("Details: " + authenticationSuccessEvent.getAuthentication().getDetails());

		if (userPrincipal != null) {
			String username = userPrincipal.getUsername();

			resetFailedAttemptsIfNecessary(username);

			LOG_LOGIN_SUCCESS.debug(username + " has successfully logged in!");
		} else {
			// Potentially Remove Log?
			LOG_LOGIN_SUCCESS.debug("Suspicious successful Login with userPrincipal being NULL");
		}
	}

	public void runFailedAuthenticationProcedure(AuthenticationFailureBadCredentialsEvent authenticationFailureEvent) {
		Object userName = authenticationFailureEvent.getAuthentication().getPrincipal();

		// Uncomment to get Login Attempt Details Remove after Testing
		// System.out.println("Credentials: " + authenticationFailureEvent.getAuthentication().getCredentials());
		// System.out.println("Principal: " + authenticationFailureEvent.getAuthentication().getPrincipal());
		// System.out.println("Authorities: " + authenticationFailureEvent.getAuthentication().getAuthorities());
		// System.out.println("Name: " + authenticationFailureEvent.getAuthentication().getName());
		// System.out.println("Details: " + authenticationFailureEvent.getAuthentication().getDetails());

		increaseFailedAttempts(userName.toString());

		// Potentially Remove Log?
		LOG_LOGIN_FAILURE.debug("Failed login using USERNAME [" + userName + "]");
	}

	private void increaseFailedAttempts(String username) {
		Optional<User> optUser = userRepository.findByUsername(username);
		if (optUser.isPresent()) {
			User user = optUser.get();
			InvalidLoginCount invalidLoginCount = getOrCreateInvalidLoginCount(user);

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

	private void resetFailedAttemptsIfNecessary(String username) {
		Optional<User> optUser = userRepository.findByUsername(username);
		if (optUser.isPresent()) {
			User user = optUser.get();
			InvalidLoginCount invalidLoginCount = getOrCreateInvalidLoginCount(user);
			if (invalidLoginCount.getInvalidAttempts() > 0) {
				resetFailedAttempts(invalidLoginCount);
			}
		}
	}

	private void resetFailedAttempts(InvalidLoginCount invalidLoginCount) {
		invalidLoginCount.setInvalidAttempts(0);
		invalidLoginCountRepository.save(invalidLoginCount);
	}

	private void sendTooManyFailedLoginAttemptsEmail(String emailAddress) throws MessagingException {
		emailService.sendMessageWithAttachment(emailAddress, subject, text, pathToAttachment);
	}

	private InvalidLoginCount createInvalidLoginCount(User user) {
		return invalidLoginCountRepository.save(new InvalidLoginCount(user, 0));
	}

	private InvalidLoginCount getOrCreateInvalidLoginCount(User user) {
		// Initializing invalidLoginCount to be able to change it in loop later
		InvalidLoginCount invalidLoginCount;

		Optional<InvalidLoginCount> optInvalidLoginCount = invalidLoginCountRepository.findById(user.getId());
		if (optInvalidLoginCount.isEmpty()) {
			invalidLoginCount = createInvalidLoginCount(user);
		} else {
			invalidLoginCount = optInvalidLoginCount.get();
		}

		return invalidLoginCount;
	}
}
