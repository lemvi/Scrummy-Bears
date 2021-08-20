package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.DTOs.ProfileDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProfileDTOService
{
    private final UserRepository userRepository;
    private final UserToProfileDTOTranslator userToProfileDTOTranslator;
    private final int maxIdSum;
    private final int minIdSum;
    private final UserService userService;
    private final String usernameNotFound;
    private final RatingCalculator ratingCalculator;


    public ProfileDTOService(UserRepository userRepository, UserToProfileDTOTranslator userToProfileDTOTranslator, @Value("${security.maxIdSum}") int maxIdSum, @Value("${security.minIdSum}") int minIdSum, UserService userService, @Value("${errorMessages.usernameNotFound}") String usernameNotFound, RatingCalculator ratingCalculator)
    {
        this.userRepository = userRepository;
        this.userToProfileDTOTranslator = userToProfileDTOTranslator;
        this.maxIdSum = maxIdSum;
        this.minIdSum = minIdSum;
        this.userService = userService;
        this.usernameNotFound = usernameNotFound;
        this.ratingCalculator = ratingCalculator;
    }

    //TODO SKILL & RATING ADDED -> CHECK IF WORKING WHEN MERGED
    public ProfileDTO viewProfile(String username)
    {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null)
            userService.throwBadRequest(usernameNotFound);

        Set<Role> roles = user.getRoles();

        boolean hasMaximumAmountOfRoles = userService.getRoleIdSum(roles) == maxIdSum;

        if (hasMaximumAmountOfRoles && roles.size() == minIdSum)
        {
            return userToProfileDTOTranslator.toCompanyProfileDTO(user);
        } else if (hasMaximumAmountOfRoles || user.getRoles().contains(new Role(1L, "ROLE_VOLUNTEER")))
        {
            return userToProfileDTOTranslator.toVolunteerProfileDTO(user);

        }
        return userToProfileDTOTranslator.toIndividualProfileDTO(user);

    }
}
