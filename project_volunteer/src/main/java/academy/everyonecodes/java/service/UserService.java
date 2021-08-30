package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ErrorMessage;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.dtos.OrganizationDTO;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    private final Long maxIdSum;
    private final Long minIdSum;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DtoTranslator dtoTranslator, Set<Role> roles,
                       @Value("${security.maxIdSum}") Long maxIdSum, @Value("${security.minIdSum}") Long minIdSum)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dtoTranslator = dtoTranslator;
        this.roles = roles;
        this.maxIdSum = maxIdSum;
        this.minIdSum = minIdSum;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findAllVolunteers() {
        return userRepository.findAll().stream()
                .filter(u -> getRoleIdSum(u.getRoles()).equals(minIdSum))
                .collect(Collectors.toList());
    }

    public List<User> findAllIndividuals() {
        return userRepository.findAll().stream()
                .filter(u -> getRoleIdSum(u.getRoles()).equals(maxIdSum - minIdSum))
                .collect(Collectors.toList());
    }

    public List<User> findAllOrganizations() {
        return userRepository.findAll().stream()
                .filter(u -> getRoleIdSum(u.getRoles()).equals(maxIdSum))
                .collect(Collectors.toList());
    }

    public List<User> findAllOrganizers() {
        ArrayList<List<User>> organizersList = new ArrayList<>();
        organizersList.add(findAllIndividuals());
        organizersList.add(findAllOrganizations());
        return organizersList.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id)
    {
        Optional<User> oUser = userRepository.findById(id);
        User user = new User();
        if (oUser.isEmpty())
            ExceptionThrower.badRequest(ErrorMessage.USERNAME_NOT_FOUND);
        return oUser;
    }

    public User translateIndividualVolunteerDTOAndSaveUser(IndividualVolunteerDTO individualVolunteerDTO)
    {
        User user = dtoTranslator.IndividualVolunteerToUser(individualVolunteerDTO);
        validateRoles(user);
        return save(user);
    }

    public User translateOrganizationDTOAndSaveUser(OrganizationDTO organizationDTO)
    {
        User user = dtoTranslator.OrganizationToUser(organizationDTO);
        validateRoles(user);
        return save(user);
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
            ExceptionThrower.badRequest(ErrorMessage.WRONG_ROLES);

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
            ExceptionThrower.badRequest(ErrorMessage.WRONG_ROLES);

        if (userRoles.size() == minIdSum && userRoleSum == maxIdSum)
        {
            if (user.getOrganizationName() == null)
                ExceptionThrower.badRequest(ErrorMessage.WRONG_ROLES);
        } else
        {
            if (user.getFirstNamePerson() == null)
                ExceptionThrower.badRequest(ErrorMessage.WRONG_ROLES);
        }
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
