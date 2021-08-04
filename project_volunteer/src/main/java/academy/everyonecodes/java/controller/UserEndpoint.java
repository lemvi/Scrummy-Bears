package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@Validated
public class UserEndpoint {
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    User saveUser(@RequestBody User user) {
        return userService.save(user);
    }
}
