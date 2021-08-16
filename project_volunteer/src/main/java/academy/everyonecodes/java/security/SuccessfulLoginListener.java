package academy.everyonecodes.java.security;

import academy.everyonecodes.java.service.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class SuccessfulLoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	LoginService loginService;

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
		loginService.runSuccessfulAuthenticationProcedure(authenticationSuccessEvent);
	}
}
