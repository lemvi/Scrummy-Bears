package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingCalculator {

	@Autowired
	RatingRepository ratingRepository;

	public double aggregateRating(Long userId) {
		List<Rating> ratings = ratingRepository.findByUser_Id(userId);
		return ratings.stream()
				.map(Rating::getRating)
				.mapToDouble(rating -> rating)
				.average()
				.orElse(Double.NaN);
	}
}
