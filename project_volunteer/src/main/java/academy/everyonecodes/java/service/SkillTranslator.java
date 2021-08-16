package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.SkillDTO;
import org.springframework.stereotype.Service;

@Service
public class SkillTranslator {

    public Skill translateToSkill(SkillDTO dto) {
        return new Skill(dto.getSkill());
    }

    public SkillDTO translateToSkillDTO(Skill skill) {
        return new SkillDTO(skill.getSkill());
    }
}
