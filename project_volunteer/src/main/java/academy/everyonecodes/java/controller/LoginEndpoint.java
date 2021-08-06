package academy.everyonecodes.java.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginEndpoint {

	@GetMapping
	public String getMessage() {
		return "It worked";
	}
}
