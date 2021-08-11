package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ProfileDTO;
import academy.everyonecodes.java.data.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserToProfileDTOTranslator
{
    private final AgeCalculator ageCalculator;

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
                user.getDescription()
                );
    }
}
