package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.Skill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SkillRepository extends JpaRepository<Skill, Long> {
}
