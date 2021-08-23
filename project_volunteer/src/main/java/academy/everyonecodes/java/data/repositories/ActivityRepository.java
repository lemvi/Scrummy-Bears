package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByOrganizer_Username(String username);

    @Query(value = "SELECT * FROM project_volunteer.activity where MATCH(title, description, recommended_skills) against (:text IN NATURAL LANGUAGE MODE);", nativeQuery = true)
    List<Activity> findFullTextSearchByText(@Param("text") String text);

    List<Activity> findByTitleContainingIgnoreCase(String title);
    List<Activity> findByDescriptionContainingIgnoreCase(String description);
    List<Activity> findByRecommendedSkillsContainingIgnoreCase(String recommendedSkills);
}
