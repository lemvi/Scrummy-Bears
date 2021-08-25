package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ErrorMessage;
import academy.everyonecodes.java.data.dtos.ProfileDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ProfileDTOService
{
    private final UserRepository userRepository;
    private final UserToProfileDTOTranslator userToProfileDTOTranslator;
    private final int maxIdSum;
    private final int minIdSum;
    private final UserService userService;


    public ProfileDTOService(UserRepository userRepository, UserToProfileDTOTranslator userToProfileDTOTranslator, @Value("${security.maxIdSum}") int maxIdSum, @Value("${security.minIdSum}") int minIdSum, UserService userService)
    {
        this.userRepository = userRepository;
        this.userToProfileDTOTranslator = userToProfileDTOTranslator;
        this.maxIdSum = maxIdSum;
        this.minIdSum = minIdSum;
        this.userService = userService;
    }

    public Optional<ProfileDTO> viewProfile(String username)
    {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            ExceptionThrower.badRequest(ErrorMessage.USERNAME_NOT_FOUND);
            return Optional.empty();
        }


        Set<Role> roles = user.getRoles();
        Long roleIdSum = userService.getRoleIdSum(roles);
        boolean hasMaximumAmountOfRoles = roleIdSum == maxIdSum;

        if (hasMaximumAmountOfRoles && roles.size() == minIdSum)
        {
            return Optional.of(userToProfileDTOTranslator.toOrganizationProfileDTO(user));
        } else if (roles.stream().map(Role::getId).anyMatch(r-> r == minIdSum))
        {
            return Optional.of(userToProfileDTOTranslator.toVolunteerProfileDTO(user));
        }

        return Optional.of(userToProfileDTOTranslator.toIndividualProfileDTO(user));
    }
}
