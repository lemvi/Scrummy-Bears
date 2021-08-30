package academy.everyonecodes.java.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forgottenpassword")
public class ForgottenPasswordEndpoint {

	@GetMapping("/username/{username}")
	String resetPasswordForUsername(@PathVariable String username) {
		return null;
	}

	@GetMapping("/username/{emailaddress}")
	String resetPasswordForEmailAddress(@PathVariable String emailAddress) {
		return null;
	}
}
