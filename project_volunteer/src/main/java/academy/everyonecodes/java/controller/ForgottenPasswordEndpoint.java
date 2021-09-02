package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.PasswordDTO;
import academy.everyonecodes.java.service.ForgottenPasswordService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/forgottenpassword")
public class ForgottenPasswordEndpoint {
	private final ForgottenPasswordService forgottenPasswordService;



	public ForgottenPasswordEndpoint(ForgottenPasswordService forgottenPasswordService) {
		this.forgottenPasswordService = forgottenPasswordService;
	}



	// TODO: Implement Endpoint to Service Links

	@PostMapping("/username")
	String requestPasswordResetForUsername(HttpServletRequest request, @RequestBody String username) {
		return forgottenPasswordService.requestPasswordResetForUsername(request, username);
	}

	@GetMapping("/activate/{passwordtoken}")
	String activatePasswordToken(@PathVariable String passwordtoken) {
		return forgottenPasswordService.activatePasswordToken(passwordtoken);
	}

	@PostMapping("/change")
	String changePassword(@RequestBody @Valid PasswordDTO passwordDTO) {
		return forgottenPasswordService.changePassword(passwordDTO);
	}

}
