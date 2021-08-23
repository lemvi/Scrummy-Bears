package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.service.WebAppTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webapptree")
public class WebAppTreeEndpoint {

	// @Autowired
	// private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Autowired
	WebAppTreeService webAppTreeService;

	@GetMapping
	@Secured({"ROLE_INDIVIDUAL", "ROLE_VOLUNTEER", "ROLE_COMPANY"})
	public String getWebAppTree() {
		return webAppTreeService.prepareWebAppTree();
	/*	return new ResponseEntity<>(
				requestMappingHandlerMapping
						.getHandlerMethods()
						.keySet()
						.stream()
						.map(RequestMappingInfo::toString)
						.collect(Collectors.toList()),
				HttpStatus.OK
		);*/
	}
}
