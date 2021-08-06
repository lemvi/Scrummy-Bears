package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class ViewerEditorEndpoint {
    private final ViewerEditorService viewerEditorService;

    public ViewerEditorEndpoint(ViewerEditorService viewerEditorService) {
        this.viewerEditorService = viewerEditorService;
    }

    @PutMapping("/{username}")
    @Secured({"ROLE_VOLUNTEER", "ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    UserDTO editAccountInfo(@PathVariable String username, @RequestBody UserDTO userDTO, Principal principal) {
        Optional<UserDTO> oUser= viewerEditorService.editAccountInfo(username, userDTO, principal);
        return oUser.orElse(null);
    }
    @GetMapping("/{username}")
    @Secured({"ROLE_VOLUNTEER", "ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    UserDTO getAccountInfo(@PathVariable String username) {
        return viewerEditorService.getAccountInfo(username).orElse(null);
    }
}

