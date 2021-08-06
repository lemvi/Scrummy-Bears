package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.ProfileDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.ProfileDTOService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile") // TODO: URL ok?
public class ProfileDTOEndpoint
{
    private final ProfileDTOService profileDTOService;

    public ProfileDTOEndpoint(ProfileDTOService profileDTOService)
    {
        this.profileDTOService = profileDTOService;
    }

    @GetMapping // TODO: Return type?
    @Secured({"ROLE_VOLUNTEER", "ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    public ProfileDTO get(@RequestBody User user)
    {
        return profileDTOService.get(user);
    }
}
