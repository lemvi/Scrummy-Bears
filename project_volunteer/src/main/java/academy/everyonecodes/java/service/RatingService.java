package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService
{

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository)
    {
        this.ratingRepository = ratingRepository;
    }

    public Rating rateUserForActivity(Rating rating, Long activityId)
    {
        //authentication + implementation to be done
        return rating;
    }

    public double calculateAverageUserRating(Long userId)
    {
        List<Rating> ratings = ratingRepository.findByUser_Id(userId);
        return ratings.stream()
                .map(Rating::getRating)
                .mapToDouble(rating -> rating)
                .average()
                .orElse(Double.NaN);
    }
}
