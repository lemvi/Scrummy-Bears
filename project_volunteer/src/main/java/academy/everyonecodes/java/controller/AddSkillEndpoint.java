package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.DTOs.SkillDTO;
import academy.everyonecodes.java.service.SkillService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addSkill")
public class AddSkillEndpoint {

        private final SkillService service;

    public AddSkillEndpoint(SkillService service) {
        this.service = service;
    }

    @PostMapping("/{username}")
    @Secured("ROLE_VOLUNTEER")
    SkillDTO addSkill(@PathVariable String username, @RequestBody SkillDTO skill) {
        return service.addSkill(username, skill).orElse(null);
    }
}
