package academy.everyonecodes.java.security;

import academy.everyonecodes.java.service.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class BadCredentialsListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
	private static final Logger LOG = LogManager.getLogger(BadCredentialsListener.class);

	@Autowired
	LoginService loginService;

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		Object userName = event.getAuthentication().getPrincipal();
		Object credentials = event.getAuthentication().getCredentials();
		LOG.debug("Failed login using USERNAME [" + userName + "]");
		LOG.debug("Failed login using PASSWORD [" + credentials + "]");

		//TODO: Remove printed Strings after testing is complete
		System.out.println("Username: " + userName + "\n" +
							"Credentials: " + credentials);
		System.out.println(userName.toString());
		loginService.increaseFailedAttempts(userName.toString());
	}
}
