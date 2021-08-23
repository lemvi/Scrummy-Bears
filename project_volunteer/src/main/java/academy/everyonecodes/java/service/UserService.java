package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.CompanyDTO;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoTranslator dtoTranslator;

    private final Set<Role> roles;

    private final int maxIdSum;
    private final int minIdSum;

    private final String wrongRoles;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DtoTranslator dtoTranslator, Set<Role> roles, @Value("${security.maxIdSum}") int maxIdSum, @Value("${security.minIdSum}") int minIdSum, @Value("${errorMessages.wrongRoles}") String wrongRoles)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dtoTranslator = dtoTranslator;
        this.roles = roles;
        this.maxIdSum = maxIdSum;
        this.minIdSum = minIdSum;
        this.wrongRoles = wrongRoles;
    }

    public User translateIndividualVolunteerDTOAndSaveUser(IndividualVolunteerDTO individualVolunteerDTO)
    {
        User user = dtoTranslator.IndividualVolunteerToUser(individualVolunteerDTO);
        validateRoles(user);
        return save(user);
    }

    public User translateCompanyDTOAndSaveUser(CompanyDTO companyDTO)
    {
        User user = dtoTranslator.CompanyToUser(companyDTO);
        validateRoles(user);
        return save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    private User save(User user)
    {
        user.setPassword(encryptPasswordFromUser(user));
        return userRepository.save(user);
    }

    private String encryptPasswordFromUser(User user)
    {
        return passwordEncoder.encode(user.getPassword());
    }

    private void validateRoles(User user)
    {
        Set<Role> userRoles = user.getRoles();
        Set<String> userRolesString = convertRoleSetToStringSet(userRoles);
        Set<String> rolesString = convertRoleSetToStringSet(roles);

        if (!rolesString.containsAll(userRolesString))
            throwBadRequest(wrongRoles);

        for (Role role : roles)
        {
            for (Role userRole : userRoles)
            {
                if (role.getRole().equals(userRole.getRole()))
                    userRole.setId(role.getId());
            }
        }

        Long userRoleSum = getRoleIdSum(userRoles);

        if (!(minIdSum <= userRoleSum && userRoleSum <= maxIdSum))
            throwBadRequest(wrongRoles);

        if (userRoles.size() == minIdSum && userRoleSum == maxIdSum)
        {
            if (user.getCompanyName() == null)
                throwBadRequest(wrongRoles);
        } else
        {
            if (user.getFirstNamePerson() == null)
                throwBadRequest(wrongRoles);
        }
    }

    protected void throwBadRequest(String errorMessage)
    {
        throw new HttpStatusCodeException(HttpStatus.BAD_REQUEST, errorMessage)
        {
        };
    }


    private Set<String> convertRoleSetToStringSet(Set<Role> roles)
    {
        return roles.stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());
    }

    protected Long getRoleIdSum(Set<Role> roles)
    {
        return roles.stream()
                .map(Role::getId)
                .reduce(0L, Long::sum);
    }
}
