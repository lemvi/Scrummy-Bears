package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.springframework.security.access.annotation.Secured;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class ViewerEditorEndpoint {
    private final ViewerEditorService viewerEditorService;

    public ViewerEditorEndpoint(ViewerEditorService viewerEditorService) {
        this.viewerEditorService = viewerEditorService;
    }

    @GetMapping("/{username}")
    //@PreAuthorize("hasAuthority('ROLE_ORGANIZATION')")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_VOLUNTEER", "ROLE_ORGANIZATION"})
    IndividualVolunteerDTO getAccountInfo(@PathVariable String username) {
        return viewerEditorService.getAccountInfo(username).orElse(null);
    }

    @PutMapping("/{username}")
    //@PreAuthorize("hasRole('ROLE_VOLUNTEER', 'ROLE_INDIVIDUAL', 'ROLE_ORGANIZATION')")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_VOLUNTEER", "ROLE_ORGANIZATION"})
    IndividualVolunteerDTO editAccountInfo(@PathVariable String username, @RequestBody IndividualVolunteerDTO individualVolunteerDTO) {
        return viewerEditorService.editAccountInfo(username, individualVolunteerDTO).orElse(null);
    }
}

