package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ProfileDTO;
import academy.everyonecodes.java.data.ProfileDTOs.CompanyProfileDTO;
import academy.everyonecodes.java.data.ProfileDTOs.IndividualProfileDTO;
import academy.everyonecodes.java.data.ProfileDTOs.VolunteerProfileDTO;
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

    protected ProfileDTO toDTO(User user)
    {
        return new ProfileDTO(
                user.getUsername(),
                user.getFirstNamePerson() + " " + user.getLastNamePerson(),
                user.getCompanyName(),
                ageCalculator.calculate(user),
                user.getDescription()
                );
    }

    protected CompanyProfileDTO toCompanyProfileDTO(User user)
    {
        return new CompanyProfileDTO(
                user.getUsername(),
                user.getPostalCode(),
                user.getCity(),
                user.getStreet(),
                user.getStreetNumber(),
                user.getEmailAddress(),
                user.getTelephoneNumber(),
                user.getDescription(),
                user.getRoles(),
                user.getCompanyName()
        );
    }

    protected IndividualProfileDTO toIndividualProfileDTO(User user)
    {
        return new IndividualProfileDTO(
                user.getUsername(),
                user.getPostalCode(),
                user.getCity(),
                user.getStreet(),
                user.getStreetNumber(),
                user.getEmailAddress(),
                user.getTelephoneNumber(),
                user.getDescription(),
                user.getRoles(),
                user.getFirstNamePerson() + " " + user.getLastNamePerson(),
                ageCalculator.calculate(user)
        );
    }

    protected VolunteerProfileDTO toVolunteerProfileDTO(User user)
    {
        return new VolunteerProfileDTO(
                user.getUsername(),
                user.getPostalCode(),
                user.getCity(),
                user.getStreet(),
                user.getStreetNumber(),
                user.getEmailAddress(),
                user.getTelephoneNumber(),
                user.getDescription(),
                user.getRoles(),
                user.getFirstNamePerson() + " " + user.getLastNamePerson(),
                ageCalculator.calculate(user)
        );
    }
}
