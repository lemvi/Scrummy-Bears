package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final IndividualVolunteerDTOTranslator individualVolunteerDTOTranslator;

    public UserService(UserRepository userRepository,
                       RoleService roleService,
                       PasswordEncoder passwordEncoder, IndividualVolunteerDTOTranslator individualVolunteerDTOTranslator) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.individualVolunteerDTOTranslator = individualVolunteerDTOTranslator;
    }

    public User translateDtoAndSaveUser(IndividualVolunteerDTO individualVolunteerDTO) {
        return save(individualVolunteerDTOTranslator.translateDTOtoUser(individualVolunteerDTO));
    }

    public User save(User user) {
        user.setPassword(encryptPasswordFromUser(user));
        if (user.getRoles() == null)
            return null;
        Set<Role> roles = user.getRoles().stream()
                .map(Role::getRole)
                .map(roleService::findByRole)
                .collect(Collectors.toSet());
        if (roles.contains(null))
            return null;
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private String encryptPasswordFromUser(@Valid User user) {
        return passwordEncoder.encode(user.getPassword());
    }
}
