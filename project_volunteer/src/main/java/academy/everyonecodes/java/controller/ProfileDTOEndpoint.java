package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.ProfileDTOs.ProfileDTO;
import academy.everyonecodes.java.service.ProfileDTOService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ProfileDTO viewProfile(@PathVariable String username)
    {
        return profileDTOService.viewProfile(username);
    }
}
