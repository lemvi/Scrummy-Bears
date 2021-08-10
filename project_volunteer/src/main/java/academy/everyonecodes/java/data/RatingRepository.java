package academy.everyonecodes.java.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
	List<Rating> findByUser_Id(Long id);
}
