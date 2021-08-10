package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.springframework.security.access.annotation.Secured;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/account")
public class ViewerEditorEndpoint {
    private final ViewerEditorService viewerEditorService;

    public ViewerEditorEndpoint(ViewerEditorService viewerEditorService) {
        this.viewerEditorService = viewerEditorService;
    }

    @PutMapping("/{username}")
    //@PreAuthorize("hasRole('ROLE_VOLUNTEER', 'ROLE_INDIVIDUAL', 'ROLE_COMPANY')")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_VOLUNTEER","ROLE_COMPANY"})
    UserDTO editAccountInfo(@PathVariable String username, @RequestBody UserDTO userDTO, Principal principal) {
        return viewerEditorService.editAccountInfo(username, userDTO, principal).orElse(null);
    }
    @GetMapping("/{username}")
    //@PreAuthorize("hasAuthority('ROLE_COMPANY')")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_VOLUNTEER","ROLE_COMPANY"})
    UserDTO getAccountInfo(@PathVariable String username, Principal principal) {
            return viewerEditorService.getAccountInfo(username, principal).orElse(null);
    }
}

