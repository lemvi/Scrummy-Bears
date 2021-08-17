package academy.everyonecodes.java.data.DTOs;

import java.util.Objects;

public class SkillDTO {
    private String skill;

    public SkillDTO() {
    }

    public SkillDTO(String skill) {
        this.skill = skill;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillDTO skillDTO = (SkillDTO) o;
        return Objects.equals(skill, skillDTO.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill);
    }
}
