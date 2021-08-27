package academy.everyonecodes.java.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginEndpoint {

	@GetMapping
	@Secured({"ROLE_VOLUNTEER", "ROLE_ORGANIZATION", "ROLE_INDIVIDUAL"})
	public String getMessage() {
		//TODO: Call method to return profile
		return "You were logged in";
	}
}
