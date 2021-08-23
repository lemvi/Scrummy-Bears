package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.OrganizationDTO;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class UserEndpoint
{
    private final UserService userService;

    public UserEndpoint(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping(params = "individual")
    User saveIndividual(@RequestParam String individual, @RequestBody @Valid IndividualVolunteerDTO individualVolunteerDTO)
    {
        return userService.translateIndividualVolunteerDTOAndSaveUser(individualVolunteerDTO);
    }

    @PostMapping(params = "organization")
    User saveOrganization(@RequestParam String organization, @RequestBody @Valid OrganizationDTO organizationDTO)
    {
        return userService.translateOrganizationDTOAndSaveUser(organizationDTO);
    }
}
