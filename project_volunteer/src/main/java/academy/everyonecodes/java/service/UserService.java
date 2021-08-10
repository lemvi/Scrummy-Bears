package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final IndividualVolunteerDTOTranslator individualVolunteerDTOTranslator;

    private final Set<Role> roles;

    private final int maxIdSum;
    private final int minIdSum;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, IndividualVolunteerDTOTranslator individualVolunteerDTOTranslator, Set<Role> roles, @Value("${security.maxIdSum}") int maxIdSum, @Value("${security.minIdSum}") int minIdSum)
    {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.individualVolunteerDTOTranslator = individualVolunteerDTOTranslator;
        this.roles = roles;
        this.maxIdSum = maxIdSum;
        this.minIdSum = minIdSum;
    }

    public User translateIndividualVolunteerDTOAndSaveUser(IndividualVolunteerDTO individualVolunteerDTO) {
        User user = individualVolunteerDTOTranslator.toUser(individualVolunteerDTO);
        user = validateRoles(user);
        return save(user);
    }

    public User save(User user) {
        user.setPassword(encryptPasswordFromUser(user));
        return userRepository.save(user);
    }

    private String encryptPasswordFromUser(User user) {
        return passwordEncoder.encode(user.getPassword());
    }

    private User validateRoles(User user) {
        Set<Role> userRoles = user.getRoles();
        Set<String> userRolesString = convertRoleSetToStringSet(userRoles);
        Set<String> rolesString = convertRoleSetToStringSet(roles);

        if (!rolesString.containsAll(userRolesString))
            throwBadRequest();

        for (Role role : roles)
        {
            for (Role userRole : userRoles)
            {
                if (role.getRole().equals(userRole.getRole()))
                    userRole.setId(role.getId());
            }
        }

        Long userRoleSum = userRoles.stream()
                .map(Role::getId)
                .reduce(0L, Long::sum);

        if (!(minIdSum <= userRoleSum && userRoleSum <= maxIdSum))
            throwBadRequest();

        return user;
    }

    private void throwBadRequest()
    {
        throw new HttpStatusCodeException(HttpStatus.BAD_REQUEST, "BAD_REQUEST"){};
    };

    private Set<String> convertRoleSetToStringSet(Set<Role> roles)
    {
        return roles.stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());
    }
}
