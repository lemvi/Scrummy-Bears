package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.ProfileDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.ProfileDTOService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileDTOEndpoint
{
    private final ProfileDTOService profileDTOService;

    public ProfileDTOEndpoint(ProfileDTOService profileDTOService)
    {
        this.profileDTOService = profileDTOService;
    }

    @GetMapping("/{username}")
    @Secured({"ROLE_VOLUNTEER", "ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    public ProfileDTO get(@PathVariable String username)
    {
        return profileDTOService.get(username);
    }
}
