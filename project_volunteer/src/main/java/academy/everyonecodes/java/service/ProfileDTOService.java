package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ProfileDTOs.ProfileSkillRatingDTO;
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
    private final ProfileViewer profileViewer;

    public ProfileDTOService(UserRepository userRepository, UserToProfileDTOTranslator userToProfileDTOTranslator, @Value("${security.maxIdSum}") int maxIdSum, @Value("${security.minIdSum}") int minIdSum, UserService userService, @Value("${errorMessages.usernameNotFound}") String usernameNotFound, ProfileViewer profileViewer)
    {
        this.userRepository = userRepository;
        this.userToProfileDTOTranslator = userToProfileDTOTranslator;
        this.maxIdSum = maxIdSum;
        this.minIdSum = minIdSum;
        this.userService = userService;
        this.usernameNotFound = usernameNotFound;
        this.profileViewer = profileViewer;
    }
//TODO SKILL & RATING ADDED -> CHECK IF WORKING WHEN MERGED
    public ProfileSkillRatingDTO viewProfile(String username)
    {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null)
            userService.throwBadRequest(usernameNotFound);

        Set<Role> roles = user.getRoles();

        boolean hasMaximumAmountOfRoles = userService.getRoleIdSum(roles) == maxIdSum;
        Skill skill = profileViewer.getSkill(username).orElse(null);
        Rating rating = profileViewer.getRating(username).orElse(null);

        if (hasMaximumAmountOfRoles && roles.size() == minIdSum)
        {
            return new ProfileSkillRatingDTO(userToProfileDTOTranslator.toCompanyProfileDTO(user), skill, rating);
        } else if (hasMaximumAmountOfRoles)
        {
            return new ProfileSkillRatingDTO(userToProfileDTOTranslator.toIndividualProfileDTO(user), skill, rating);
        }

        return new ProfileSkillRatingDTO(userToProfileDTOTranslator.toVolunteerProfileDTO(user), skill, rating);
    }
}
