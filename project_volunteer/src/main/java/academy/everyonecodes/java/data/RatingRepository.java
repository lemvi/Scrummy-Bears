package academy.everyonecodes.java.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
	List<Rating> findByUser_Id(Long id);

	Optional<Rating> findByEventIdAndUser(Long eventId, User user);
}