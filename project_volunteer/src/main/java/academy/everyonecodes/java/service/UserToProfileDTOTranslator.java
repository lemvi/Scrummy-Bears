package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ProfileDTO;
import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.data.RatingRepository;
import academy.everyonecodes.java.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class UserToProfileDTOTranslator
{
    private final AgeCalculator ageCalculator;

    @Autowired
    RatingRepository ratingRepository;

    public UserToProfileDTOTranslator(AgeCalculator ageCalculator)
    {
        this.ageCalculator = ageCalculator;
    }

    public ProfileDTO toDTO(User user)
    {
        return new ProfileDTO(
                user.getUsername(),
                user.getFirstNamePerson() + " " + user.getLastNamePerson(),
                user.getCompanyName(),
                ageCalculator.calculate(user),
                user.getDescription(),
                aggregateRating(user.getId())
                );
    }

    private double aggregateRating(Long userId) {
        List<Rating> ratings = ratingRepository.findByUser_Id(userId);
        return ratings.stream()
                .map(Rating::getRating)
                .mapToDouble(rating -> rating)
                .average()
                .orElse(Double.NaN);
    }
}
