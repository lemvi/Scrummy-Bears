package academy.everyonecodes.java.data.repositories;

import academy.everyonecodes.java.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);


    @Query(value = "SELECT * FROM project_volunteer.user where MATCH(description) against (:text IN NATURAL LANGUAGE MODE);", nativeQuery = true)
    List<User> findFullTextSearchByText(@Param("text") String text);
    List<User> findByDescriptionContainingIgnoreCase(String description);

}
