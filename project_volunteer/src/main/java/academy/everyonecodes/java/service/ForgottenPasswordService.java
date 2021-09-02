package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ForgottenPasswordMessages;
import academy.everyonecodes.java.data.PasswordToken;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.PasswordDTO;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgottenPasswordService {
	private UserService userService;
	private EmailServiceImpl emailService;
	private PasswordTokenService passwordTokenService;
	private ForgottenPasswordMessages forgottenPasswordMessages;

	@Value("${email.passwordforgotten.subject}")
	private String emailSubject;

	@Value("${email.passwordforgotten.text}")
	private String emailText;

	@Value("${passwordforgotten.values.expirytime}")
	private Long expiryTimeInMinutes;



	public ForgottenPasswordService() {}

	@Autowired
	public ForgottenPasswordService(UserService userService, EmailServiceImpl emailService, PasswordTokenService passwordTokenService, ForgottenPasswordMessages forgottenPasswordMessages) {
		this.userService = userService;
		this.emailService = emailService;
		this.passwordTokenService = passwordTokenService;
		this.forgottenPasswordMessages = forgottenPasswordMessages;
	}



	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public void setEmailText(String emailText) {
		this.emailText = emailText;
	}

	public void setExpiryTimeInMinutes(Long expiryTimeInMinutes) {
		this.expiryTimeInMinutes = expiryTimeInMinutes;
	}



	public String requestPasswordResetForUsername(HttpServletRequest request, String username) {
		// Find if User Exists. If yes, create Token for User and send Token to corresponding Email Address. Never show the Token.
		Optional<User> optUser = userService.findByUsername(username);
		return processPotentialUser(request, optUser);
	}

	private String processPotentialUser(HttpServletRequest request, Optional<User> optUser) {
		if (optUser.isPresent()) {
			User user = optUser.get();
			PasswordToken passwordToken = createPasswordTokenForUser(user);
			String tokenActivationLink = createTokenActivationLink(request, passwordToken);
			String fullEmailText = emailText + "\n\n\n" + tokenActivationLink;
			emailService.sendSimpleMessage(user.getEmailAddress(), emailSubject, fullEmailText);
			return forgottenPasswordMessages.getResetEmailSuccessfully();
		} else {
			return forgottenPasswordMessages.getResetEmailNoUser();
		}
	}

	private PasswordToken createPasswordTokenForUser(User user) {
		invalidateOldTokens(user.getUsername());
		String uuid = UUID.randomUUID().toString();
		LocalDateTime expiryTime = LocalDateTime.now();
		PasswordToken passwordToken = new PasswordToken(uuid, user, expiryTime);
		passwordTokenService.save(passwordToken);
		return passwordToken;
	}

	private String createTokenActivationLink(HttpServletRequest request, PasswordToken passwordToken) {
		String url = request.getRequestURL().toString();
		String baseUrl = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
		return baseUrl + "forgottenpassword/activate/" + passwordToken.getToken();
	}

	private void invalidateOldTokens(String username) {
		List<PasswordToken> oldPasswordTokens = passwordTokenService.findByUser_usernameAndValid(username);
		oldPasswordTokens.forEach(this::invalidateToken);
	}

	private void invalidateToken(PasswordToken passwordToken) {
		passwordToken.setValid(false);
		passwordTokenService.save(passwordToken);
	}



	public String activatePasswordToken(String token) {
		Optional<PasswordToken> optPasswordToken= passwordTokenService.findByToken(token);
		if (optPasswordToken.isPresent()) {
			PasswordToken passwordToken = optPasswordToken.get();
			if (passwordToken.isValid() && getMinutesBetweenTwoTimes(passwordToken.getCreationTime(), LocalDateTime.now()) < expiryTimeInMinutes) {
				passwordToken.setActive(true);
				passwordTokenService.save(passwordToken);
				return forgottenPasswordMessages.getActivateTokenSuccessfully();
			} else {
				return forgottenPasswordMessages.getActivateTokenInvalid();
			}
		} else {
			return forgottenPasswordMessages.getActivateTokenFailure();
		}
	}

	private Long getMinutesBetweenTwoTimes(LocalDateTime creationTime, LocalDateTime now) {
		Duration duration = Duration.between(creationTime, now);
		return duration.toMinutes();
	}



	public String changePassword(PasswordDTO passwordDTO) {
		Optional<PasswordToken> optPasswordToken = passwordTokenService.findByUser_usernameAndValidAndActive(passwordDTO.getUsername());
		if (optPasswordToken.isPresent()) {
			if (passwordDTO.getNewPassword().equals(passwordDTO.getNewPasswordRepeat())) {
				PasswordToken passwordToken = optPasswordToken.get();
				User user = passwordToken.getUser();
				user.setPassword(passwordDTO.getNewPassword());
				userService.validateAndSaveUser(user);
				invalidateToken(passwordToken);
				return forgottenPasswordMessages.getChangePasswordSuccessfully();
			} else {
				return forgottenPasswordMessages.getChangePasswordNotEnteredSameTwice();
			}
		} else {
			return forgottenPasswordMessages.getChangePasswordNoValidToken();
		}
	}

	// Bonus: Invalidate based on time? (Scheduled Cron Expression)
}
