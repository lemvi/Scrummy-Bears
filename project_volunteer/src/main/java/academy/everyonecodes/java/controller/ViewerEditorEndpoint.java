package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/account")
public class ViewerEditorEndpoint {
    private final academy.everyonecodes.java.Service.ViewerEditorService viewerEditorService;

    public ViewerEditorEndpoint(academy.everyonecodes.java.Service.ViewerEditorService viewerEditorService) {
        this.viewerEditorService = viewerEditorService;
    }

    @PostMapping
    UserDTO editAccountInfo(@RequestBody UserDTO userDTO) {
        Optional<UserDTO> oUser= viewerEditorService.editAccountInfo(userDTO);
        return oUser.orElse(null);
    }
    @GetMapping
    UserDTO getAccountInfo() {
        return viewerEditorService.getAccountInfo();
    }
}

