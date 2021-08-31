package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.dtos.OrganizationProfileDTO;
import academy.everyonecodes.java.data.dtos.IndividualProfileDTO;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.BadgesRepository;
import academy.everyonecodes.java.data.repositories.LevelRepository;
import org.springframework.stereotype.Service;

@Service
public class UserToProfileDTOTranslator
{
    private final AgeCalculator ageCalculator;

    private final RatingService ratingService;
    private final SkillService skillService;
    private final BadgesRepository badgesRepository;
    private final LevelRepository levelRepository;


    public UserToProfileDTOTranslator(AgeCalculator ageCalculator, RatingService ratingService, SkillService skillService, BadgesRepository badgesRepository, LevelRepository levelRepository)
    {
        this.ageCalculator = ageCalculator;
        this.ratingService = ratingService;
        this.skillService = skillService;
        this.badgesRepository = badgesRepository;
        this.levelRepository = levelRepository;
    }


    protected OrganizationProfileDTO toOrganizationProfileDTO(User user)
    {
        return new OrganizationProfileDTO(
                user.getUsername(),
                user.getPostalCode(),
                user.getCity(),
                user.getStreet(),
                user.getStreetNumber(),
                user.getEmailAddress(),
                user.getTelephoneNumber(),
                user.getDescription(),
                user.getRoles(),
                ratingService.calculateAverageUserRating(user.getId()),
                user.getOrganizationName()
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
                ratingService.calculateAverageUserRating(user.getId()),
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
                ratingService.calculateAverageUserRating(user.getId()),
                user.getFirstNamePerson() + " " + user.getLastNamePerson(),
                ageCalculator.calculate(user),
                skillService.collect(user),
                badgesRepository.findByUser(user),
                LevelService.getCurrentUserLevel(user)
        );
    }
}
