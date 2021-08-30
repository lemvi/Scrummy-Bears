package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.OrganizationDTO;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest
{

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    User organization = new User(
        "organization",
        "validEncryptedPw",
        "validOrganizationName",
        "validEmail",
        Set.of(new Role(3L, "ROLE_ORGANIZATION"))
        );
    User volunteer = new User(
        "volunteer",
        "validEncryptedPw",
        "validFirst",
        "validSecond",
        "validEmail",
        Set.of(new Role(1L, "ROLE_VOLUNTEER"))
        );
    User individual = new User(
            "individual",
            "validEncryptedPw",
            "validFirst",
            "validSecond",
            "validEmail",
            Set.of(new Role(2L, "ROLE_INDIVIDUAL"))
    );
    List<User> users = List.of(organization, volunteer, individual);

    @Test
    void findAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Assert.assertEquals(users, userService.findAllUsers());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void findAllVolunteers() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Assert.assertEquals(List.of(volunteer), userService.findAllVolunteers());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void findAllIndividuals() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Assert.assertEquals(List.of(individual), userService.findAllIndividuals());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void findAllOrganizations() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Assert.assertEquals(List.of(organization), userService.findAllOrganizations());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void findAllOrganizers() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Assert.assertEquals(List.of(individual, organization), userService.findAllOrganizers());
        Mockito.verify(userRepository, times(2)).findAll();
    }

    @Test
    void findByUsername() {
        Mockito.when(userRepository.findByUsername("volunteer")).thenReturn(Optional.of(volunteer));
        Assert.assertEquals(Optional.of(volunteer), userService.findByUsername("volunteer"));
        Mockito.verify(userRepository).findByUsername("volunteer");
    }

    @Test
    void findById_usernameFound() {
        volunteer.setId(1L);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(volunteer));
        Assert.assertEquals(Optional.of(volunteer), userService.findById(1L));
        Mockito.verify(userRepository).findById(1L);
    }
    @Test
    void findById_usernameNotFound() {
        volunteer.setId(1L);
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            userService.findById(2L);
        });
        Mockito.verify(userRepository).findById(2L);
    }

    @ParameterizedTest
    @MethodSource("getValidParams")
    void translateIndividualVolunteerDTOAndSaveUserTest_validRoles(IndividualVolunteerDTO input, User expected)
    {
        String password = input.getPassword();

        when(passwordEncoder.encode(password))
                .thenReturn("encrypted");
        when(userRepository.save(expected))
                .thenReturn(expected);

        User actual = userService.translateIndividualVolunteerDTOAndSaveUser(input);

        assertEquals(expected, actual);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(expected);
    }


    @ParameterizedTest
    @MethodSource("getInvalidParams")
    void translateIndividualVolunteerDTOAndSaveUserTest_invalidRoles(IndividualVolunteerDTO input)
    {
        assertThrows(HttpStatusCodeException.class, () -> userService.translateIndividualVolunteerDTOAndSaveUser(input));
    }


    @ParameterizedTest
    @MethodSource("getValidParamsOrganization")
    void translateOrganizationDTOAndSaveUserTest_validRoles(OrganizationDTO input, User expected)
    {
        String password = input.getPassword();

        when(passwordEncoder.encode(password))
                .thenReturn("encrypted");
        when(userRepository.save(expected))
                .thenReturn(expected);

        User actual = userService.translateOrganizationDTOAndSaveUser(input);

        assertEquals(expected, actual);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(expected);
    }


    @ParameterizedTest
    @MethodSource("getInvalidParamsOrganization")
    void translateOrganizationDTOAndSaveUserTest_invalidRoles(OrganizationDTO input)
    {
        assertThrows(HttpStatusCodeException.class, () -> userService.translateOrganizationDTOAndSaveUser(input));
    }

    static Stream<Arguments> getValidParams()
    {
        String validUsername = "user";
        String validPw = "123456";
        String validEncryptedPw = "encrypted";
        String validFirst = "first";
        String validSecond = "second";
        LocalDate dateOfBirth = LocalDate.of(2020, 5, 5);
        String validEmail = "user@email.com";
        String postalCode = "postalCode";
        String city = "city";
        String street = "street";
        String streetNumber = "streetNumber";
        String telephoneNumber = "telephoneNumber";
        String description = "description";
        return Stream.of(
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_VOLUNTEER"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_VOLUNTEER"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                dateOfBirth,
                                postalCode,
                                city,
                                street,
                                streetNumber,
                                validEmail,
                                telephoneNumber,
                                description,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validFirst,
                                validSecond,
                                dateOfBirth,
                                postalCode,
                                city,
                                street,
                                streetNumber,
                                validEmail,
                                telephoneNumber,
                                description,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
                        )
                )
        );
    }

    static Stream<Arguments> getInvalidParams()
    {
        String validUsername = "user";
        String validPw = "123456";
        String validFirst = "first";
        String validSecond = "second";
        String validEmail = "user@email.com";
        return Stream.of(
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_ORGANIZATION"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_ORGANIZATION"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_ORGANIZATION"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_MAFIABOSS"))
                        )
                )
        );
    }

    static Stream<Arguments> getValidParamsOrganization()
    {
        String validUsername = "user";
        String validPw = "123456";
        String validEncryptedPw = "encrypted";
        String validOrganizationName = "organizationName";
        String validEmail = "user@email.com";
        String postalCode = "postalCode";
        String city = "city";
        String street = "street";
        String streetnumber = "streetnumber";
        String telephoneNumber = "telephoneNumber";
        String description = "description";

        return Stream.of(
                Arguments.of(
                        new OrganizationDTO(
                                validUsername,
                                validPw,
                                validOrganizationName,
                                validEmail,
                                Set.of(new Role("ROLE_ORGANIZATION"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validOrganizationName,
                                validEmail,
                                Set.of(new Role("ROLE_ORGANIZATION"))
                        )
                ),
                Arguments.of(
                        new OrganizationDTO(
                                validUsername,
                                validPw,
                                validOrganizationName,
                                postalCode,
                                city,
                                street,
                                streetnumber,
                                validEmail,
                                telephoneNumber,
                                description,
                                Set.of(new Role("ROLE_ORGANIZATION"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validOrganizationName,
                                postalCode,
                                city,
                                street,
                                streetnumber,
                                validEmail,
                                telephoneNumber,
                                description,
                                Set.of(new Role("ROLE_ORGANIZATION"))
                        )
                )
        );
    }

    static Stream<Arguments> getInvalidParamsOrganization()
    {
        String validUsername = "user";
        String validPw = "123456";
        String validOrganizationName = "organizationName";
        String validEmail = "user@email.com";
        return Stream.of(
                Arguments.of(
                        new OrganizationDTO(
                                validUsername,
                                validPw,
                                validOrganizationName,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"))
                        )
                ),
                Arguments.of(
                        new OrganizationDTO(
                                validUsername,
                                validPw,
                                validOrganizationName,
                                validEmail,
                                Set.of(new Role("ROLE_VOLUNTEER"))
                        )
                ),
                Arguments.of(
                        new OrganizationDTO(
                                validUsername,
                                validPw,
                                validOrganizationName,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
                        )
                ),
                Arguments.of(
                        new OrganizationDTO(
                                validUsername,
                                validPw,
                                validOrganizationName,
                                validEmail,
                                Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_ORGANIZATION"))
                        )
                ),
                Arguments.of(
                        new OrganizationDTO(
                                validUsername,
                                validPw,
                                validOrganizationName,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_ORGANIZATION"))
                        )
                ),
                Arguments.of(
                        new OrganizationDTO(
                                validUsername,
                                validPw,
                                validOrganizationName,
                                validEmail,
                                Set.of(new Role("ROLE_MAFIABOSS"))
                        )
                )
        );
    }
}


