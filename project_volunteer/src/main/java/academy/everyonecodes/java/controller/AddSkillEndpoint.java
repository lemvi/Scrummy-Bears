package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.SkillDTO;
import academy.everyonecodes.java.service.AddSkillService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addSkill")
public class AddSkillEndpoint {

    private final AddSkillService addSkillService;

    public AddSkillEndpoint(AddSkillService addSkillService) {
        this.addSkillService = addSkillService;
    }

    @PostMapping("/{username}")
    @Secured("ROLE_VOLUNTEER")
    SkillDTO addSkill(@PathVariable String username, @RequestBody SkillDTO skill) {
        return addSkillService.addSkill(username, skill).orElse(null);
    }
}
