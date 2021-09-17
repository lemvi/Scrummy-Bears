package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTOBuilder;
import academy.everyonecodes.java.data.dtos.OrganizationDTO;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.dtos.OrganizationDTOBuilder;
import academy.everyonecodes.java.data.repositories.UserRepository;
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

    User organization = new UserEntityBuilder().setUsername("organization")
                                               .setPassword("validEncryptedPw")
                                               .setOrganizationName("validOrganizationName")
                                               .setEmailAddress("validEmail")
                                               .setRoles(Set.of(new Role(3L, "ROLE_ORGANIZATION")))
                                               .createUser();
    User volunteer = new UserEntityBuilder().setUsername("volunteer")
                                            .setPassword("validEncryptedPw")
                                            .setFirstNamePerson("validFirst")
                                            .setLastNamePerson("validSecond")
                                            .setEmailAddress("validEmail")
                                            .setRoles(Set.of(new Role(1L, "ROLE_VOLUNTEER")))
                                            .createUser();
    User individual = new UserEntityBuilder().setUsername("individual")
                                             .setPassword("validEncryptedPw")
                                             .setFirstNamePerson("validFirst")
                                             .setLastNamePerson("validSecond")
                                             .setEmailAddress("validEmail")
                                             .setRoles(Set.of(new Role(2L, "ROLE_INDIVIDUAL")))
                                             .createUser();
    List<User> users = List.of(organization, volunteer, individual);

    @Test
    void findAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertEquals(users, userService.findAllUsers());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void findAllVolunteers() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertEquals(List.of(volunteer), userService.findAllVolunteers());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void findAllIndividuals() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertEquals(List.of(individual), userService.findAllIndividuals());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void findAllOrganizations() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertEquals(List.of(organization), userService.findAllOrganizations());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void findAllOrganizers() {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertEquals(List.of(individual, organization), userService.findAllOrganizers());
        Mockito.verify(userRepository, times(2)).findAll();
    }

    @Test
    void findByUsername() {
        Mockito.when(userRepository.findByUsername("volunteer")).thenReturn(Optional.of(volunteer));
        assertEquals(Optional.of(volunteer), userService.findByUsername("volunteer"));
        Mockito.verify(userRepository).findByUsername("volunteer");
    }

    @Test
    void findById_usernameFound() {
        volunteer.setId(1L);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(volunteer));
        assertEquals(Optional.of(volunteer), userService.findById(1L));
        Mockito.verify(userRepository).findById(1L);
    }
    @Test
    void findById_usernameNotFound() {
        volunteer.setId(1L);
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpStatusCodeException.class, () -> userService.findById(2L));
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
                        new IndividualVolunteerDTOBuilder().setUsername(validUsername)
                                                           .setPassword(validPw)
                                                           .setFirstNamePerson(validFirst)
                                                           .setLastNamePerson(validSecond)
                                                           .setEmailAddress(validEmail)
                                                           .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                           .createIndividualVolunteerDTO()
                        ,
                        new UserEntityBuilder().setUsername(validUsername)
                                               .setPassword(validEncryptedPw)
                                               .setFirstNamePerson(validFirst)
                                               .setLastNamePerson(validSecond)
                                               .setEmailAddress(validEmail)
                                               .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                               .createUser()
                ),
                Arguments.of(
                        new IndividualVolunteerDTOBuilder().setUsername(validUsername)
                                                           .setPassword(validPw)
                                                           .setFirstNamePerson(validFirst)
                                                           .setLastNamePerson(validSecond)
                                                           .setEmailAddress(validEmail)
                                                           .setRoles(Set.of(new Role("ROLE_INDIVIDUAL")))
                                                           .createIndividualVolunteerDTO(),
                        new UserEntityBuilder().setUsername(validUsername)
                                               .setPassword(validEncryptedPw)
                                               .setFirstNamePerson(validFirst)
                                               .setLastNamePerson(validSecond)
                                               .setEmailAddress(validEmail)
                                               .setRoles(Set.of(new Role("ROLE_INDIVIDUAL")))
                                               .createUser()
                ),
                Arguments.of(
                        new IndividualVolunteerDTOBuilder().setUsername(validUsername)
                                                           .setPassword(validPw)
                                                           .setFirstNamePerson(validFirst)
                                                           .setLastNamePerson(validSecond)
                                                           .setEmailAddress(validEmail)
                                                           .setRoles(Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL")))
                                                           .createIndividualVolunteerDTO(),
                        new UserEntityBuilder().setUsername(validUsername)
                                               .setPassword(validEncryptedPw)
                                               .setFirstNamePerson(validFirst)
                                               .setLastNamePerson(validSecond)
                                               .setEmailAddress(validEmail)
                                               .setRoles(Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER")))
                                               .createUser()
                ),
                Arguments.of(
                        new IndividualVolunteerDTOBuilder().setUsername(validUsername)
                                                           .setPassword(validPw)
                                                           .setFirstNamePerson(validFirst)
                                                           .setLastNamePerson(validSecond)
                                                           .setDateOfBirth(dateOfBirth)
                                                           .setPostalCode(postalCode)
                                                           .setCity(city)
                                                           .setStreet(street)
                                                           .setStreetNumber(streetNumber)
                                                           .setEmailAddress(validEmail)
                                                           .setTelephoneNumber(telephoneNumber)
                                                           .setDescription(description)
                                                           .setRoles(Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER")))
                                                           .createIndividualVolunteerDTO(),
                        new UserEntityBuilder().setUsername(validUsername)
                                               .setPassword(validEncryptedPw)
                                               .setFirstNamePerson(validFirst)
                                               .setLastNamePerson(validSecond)
                                               .setDateOfBirth(dateOfBirth)
                                               .setPostalCode(postalCode)
                                               .setCity(city)
                                               .setStreet(street)
                                               .setStreetNumber(streetNumber)
                                               .setEmailAddress(validEmail)
                                               .setTelephoneNumber(telephoneNumber)
                                               .setDescription(description)
                                               .setRoles(Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER")))
                                               .createUser()
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
                        new IndividualVolunteerDTOBuilder().setUsername(validUsername)
                                                           .setPassword(validPw)
                                                           .setFirstNamePerson(validFirst)
                                                           .setLastNamePerson(validSecond)
                                                           .setEmailAddress(validEmail)
                                                           .setRoles(Set.of(new Role("ROLE_ORGANIZATION")))
                                                           .createIndividualVolunteerDTO()
                ),
                Arguments.of(
                        new IndividualVolunteerDTOBuilder().setUsername(validUsername)
                                                           .setPassword(validPw)
                                                           .setFirstNamePerson(validFirst)
                                                           .setLastNamePerson(validSecond)
                                                           .setEmailAddress(validEmail)
                                                           .setRoles(Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_ORGANIZATION")))
                                                           .createIndividualVolunteerDTO()
                ),
                Arguments.of(
                        new IndividualVolunteerDTOBuilder().setUsername(validUsername)
                                                           .setPassword(validPw)
                                                           .setFirstNamePerson(validFirst)
                                                           .setLastNamePerson(validSecond)
                                                           .setEmailAddress(validEmail)
                                                           .setRoles(Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_ORGANIZATION")))
                                                           .createIndividualVolunteerDTO()
                ),
                Arguments.of(
                        new IndividualVolunteerDTOBuilder().setUsername(validUsername)
                                                           .setPassword(validPw)
                                                           .setFirstNamePerson(validFirst)
                                                           .setLastNamePerson(validSecond)
                                                           .setEmailAddress(validEmail)
                                                           .setRoles(Set.of(new Role("ROLE_MAFIABOSS")))
                                                           .createIndividualVolunteerDTO()
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
                        new OrganizationDTOBuilder().setUsername(validUsername)
                                                    .setPassword(validPw)
                                                    .setOrganizationName(validOrganizationName)
                                                    .setEmailAddress(validEmail)
                                                    .setRoles(Set.of(new Role("ROLE_ORGANIZATION")))
                                                    .createOrganizationDTO()

                ,
                new UserEntityBuilder().setUsername(validUsername)
                                       .setPassword(validEncryptedPw)
                                       .setOrganizationName(validOrganizationName)
                                       .setEmailAddress(validEmail)
                                       .setRoles(Set.of(new Role("ROLE_ORGANIZATION")))
                                       .createUser()
        ),
                Arguments.of(
                        new OrganizationDTOBuilder().setUsername(validUsername).setPassword(validPw).setOrganizationName(validOrganizationName).setPostalCode(postalCode).setCity(city).setStreet(street).setStreetNumber(streetnumber).setEmailAddress(validEmail).setTelephoneNumber(telephoneNumber).setDescription(description).setRoles(Set.of(new Role("ROLE_ORGANIZATION"))).createOrganizationDTO(),
                        new UserEntityBuilder().setUsername(validUsername).setPassword(validEncryptedPw).setOrganizationName(validOrganizationName).setPostalCode(postalCode).setCity(city).setStreet(street).setStreetNumber(streetnumber).setEmailAddress(validEmail).setTelephoneNumber(telephoneNumber).setDescription(description).setRoles(Set.of(new Role("ROLE_ORGANIZATION"))).createUser()
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
                        new OrganizationDTOBuilder().setUsername(validUsername)
                                                    .setPassword(validPw)
                                                    .setOrganizationName(validOrganizationName)
                                                    .setEmailAddress(validEmail)
                                                    .setRoles(Set.of(new Role("ROLE_INDIVIDUAL")))
                                                    .createOrganizationDTO()

                ),
                Arguments.of(
                        new OrganizationDTOBuilder().setUsername(validUsername)
                                                    .setPassword(validPw)
                                                    .setOrganizationName(validOrganizationName)
                                                    .setEmailAddress(validEmail)
                                                    .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                    .createOrganizationDTO()

                ),
                Arguments.of(
                        new OrganizationDTOBuilder().setUsername(validUsername)
                                                    .setPassword(validPw)
                                                    .setOrganizationName(validOrganizationName)
                                                    .setEmailAddress(validEmail)
                                                    .setRoles(Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER")))
                                                    .createOrganizationDTO()

                ),
                Arguments.of(
                        new OrganizationDTOBuilder().setUsername(validUsername)
                                                    .setPassword(validPw)
                                                    .setOrganizationName(validOrganizationName)
                                                    .setEmailAddress(validEmail)
                                                    .setRoles(Set.of(new Role("ROLE_ORGANIZATION"), new Role("ROLE_VOLUNTEER")))
                                                    .createOrganizationDTO()

                ),
                Arguments.of(
                        new OrganizationDTOBuilder().setUsername(validUsername)
                                                    .setPassword(validPw)
                                                    .setOrganizationName(validOrganizationName)
                                                    .setEmailAddress(validEmail)
                                                    .setRoles(Set.of(new Role("ROLE_ORGANIZATION"), new Role("ROLE_INDIVIDUAL")))
                                                    .createOrganizationDTO()

                ),
                Arguments.of(
                        new OrganizationDTOBuilder().setUsername(validUsername)
                                                    .setPassword(validPw)
                                                    .setOrganizationName(validOrganizationName)
                                                    .setEmailAddress(validEmail)
                                                    .setRoles(Set.of(new Role("ROLE_MAFIABOSS")))
                                                    .createOrganizationDTO()

                )
        );
    }

    @Test
    void validateAndSaveUser() {
        String validUsername = "user";
        String validEncryptedPw = "encrypted";
        String validFirst = "first";
        String validSecond = "second";
        String validEmail = "user@email.com";

        User testUser = new UserEntityBuilder().setUsername(validUsername).setPassword(validEncryptedPw).setFirstNamePerson(validFirst).setLastNamePerson(validSecond).setEmailAddress(validEmail).setRoles(Set.of(new Role("ROLE_VOLUNTEER"))).createUser();

        userService.validateAndSaveUser(testUser);

        Mockito.verify(userRepository).save(testUser);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}


