package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query(value = "SELECT * FROM project_volunteer.skill where MATCH(skill) against (:text IN NATURAL LANGUAGE MODE);", nativeQuery = true)
    List<Skill> findFullTextSearchByText(@Param("text") String text);
    List<Skill> findBySkillContainingIgnoreCase(String skill);

}
