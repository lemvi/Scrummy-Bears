package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class UserEndpoint {
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/individual")
    User saveIndividual(@RequestBody @Valid IndividualVolunteerDTO individualVolunteerDTO) {
        return userService.translateIndividualVolunteerDTOAndSaveUser(individualVolunteerDTO);
    }
}
