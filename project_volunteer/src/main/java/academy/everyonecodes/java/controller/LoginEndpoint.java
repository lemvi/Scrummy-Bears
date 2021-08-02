package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginEndpoint {
	private final LoginService loginService;

	public LoginEndpoint(LoginService loginService) {
		this.loginService = loginService;
	}

	// TODO: Whats really best to return here? Might be nothing since we redirect? Might be the new page? Not sure
	@PostMapping
	User loginUser() {
		return loginService.loginUser();
	}
}
