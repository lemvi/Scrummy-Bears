package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.IndividualProfileDTO;
import academy.everyonecodes.java.data.dtos.ProfileDTO;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.service.ProfileDTOService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Secured({"ROLE_VOLUNTEER", "ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    public ProfileDTO viewProfile(@PathVariable String username)
    {
        return profileDTOService.viewProfile(username).orElse(null);
    }

    @GetMapping("/volunteers")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    public List<ProfileDTO> viewAllProfilesOfVolunteers() {
        return profileDTOService.viewAllProfilesOfVolunteers();
    }

    @GetMapping("/individuals")
    @Secured({"ROLE_VOLUNTEER"})
    public List<ProfileDTO> viewAllProfilesOfIndividuals() {
        return profileDTOService.viewAllProfilesOfIndividuals();
    }

    @GetMapping("/organizations")
    @Secured({"ROLE_VOLUNTEER"})
    public List<ProfileDTO> viewAllProfilesOfOrganizations() {
        return profileDTOService.viewAllProfilesOfOrganizations();
    }

    @GetMapping("/organizers")
    @Secured({"ROLE_VOLUNTEER"})
    public List<ProfileDTO> viewAllProfilesOfOrganizers() {
        return profileDTOService.viewAllProfilesOfOrganizers();
    }
}
