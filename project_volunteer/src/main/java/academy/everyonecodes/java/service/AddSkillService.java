package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class AddSkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final UserAndSkillTranslator userAndSkillTranslator;

    public AddSkillService(SkillRepository skillRepository, UserRepository userRepository, UserAndSkillTranslator userAndSkillTranslator) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.userAndSkillTranslator = userAndSkillTranslator;
    }
    //TODO: if the user already has a skill, add the skill to the list with a semicolon. -> make another method for this
    //TODO: if(user. exists ) then get the skill and add to it.
    public Optional<SkillDTO> addSkill(String username, SkillDTO skill, Principal principal) {
        Optional<User> oUserToAddSkill = userRepository.findByUsername(username);
        if (oUserToAddSkill.isPresent() && username.equals(principal.getName())) {
            User userToAddSkill = oUserToAddSkill.get();
            Skill skillToAdd = userAndSkillTranslator.translateToSkill(skill);
            skillToAdd.setUser(userToAddSkill);
            skillRepository.save(skillToAdd);
            return Optional.of(userAndSkillTranslator.translateToSkillDTO(skillToAdd));
        }
        return Optional.empty();
    }
}
