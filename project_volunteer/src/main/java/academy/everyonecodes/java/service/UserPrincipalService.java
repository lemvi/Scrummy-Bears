package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
public class UserPrincipalService implements UserDetailsService {
	private final UserRepository userrepository;

	public UserPrincipalService(UserRepository userrepository) {
		this.userrepository = userrepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO: Change find method name depending on Userrepository
		final Optional<User> optUser = userRepository.findByUsername(username);

		if (optUser.isPresent()) {
			// If Password is invalid, check failed Loginattempts, Send Email if necessary?
			// TODO: Check return value, solution says this, IntelliJ wants UserDetails
			return optUser.get();
		}

		// Reject Login Attempt if no Username is found
		else {
			throw new UsernameNotFoundException(MessageFormat.format("No User with Username: {0} could be found.", username));
		}
	}
}
