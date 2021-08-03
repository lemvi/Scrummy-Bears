package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.LoginAttemptDTO;
import academy.everyonecodes.java.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LoginService {

	// TODO: Replace with Value from yml file
	public static final int MAX_FAILED_ATTEMPTS = 5;

	@Autowired
	private UserRepository userRepository;

	public void increaseFailedAttempts(User user) {
		int newFailAttempts = user.getFailedAttempt() + 1;
		userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
	}

	public void resetFailedAttempts(String email) {
		userRepository.updateFailedAttempts(0, email);
	}

	public void sendLoginAbuseEmail() {

	}

	// TODO: Delete dependency on Dummy method
	public User loginUser(LoginAttemptDTO loginAttemptDTO) {
		return null;
	}
}
