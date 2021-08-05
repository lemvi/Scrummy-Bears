package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/account")
public class ViewerEditorEndpoint {
    private final ViewerEditorService viewerEditorService;

    public ViewerEditorEndpoint(ViewerEditorService viewerEditorService) {
        this.viewerEditorService = viewerEditorService;
    }

    @PutMapping("/{username}")
    UserDTO editAccountInfo(@PathVariable String username, @RequestBody UserDTO userDTO) {
        Optional<UserDTO> oUser= viewerEditorService.editAccountInfo(username, userDTO);
        return oUser.orElse(null);
    }
    @GetMapping("/{username}")
    UserDTO getAccountInfo(@PathVariable String username) {
        return viewerEditorService.getAccountInfo(username).orElse(null);
    }
}

