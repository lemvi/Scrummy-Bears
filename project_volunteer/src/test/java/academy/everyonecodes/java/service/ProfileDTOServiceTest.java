package academy.everyonecodes.java.service;


import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.OrganizationProfileDTO;
import academy.everyonecodes.java.data.dtos.IndividualProfileDTO;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProfileDTOServiceTest {

    @Autowired
    ProfileDTOService profileDTOService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    UserToProfileDTOTranslator userToProfileDTOTranslator;
    @MockBean
    UserService userService;

    User user = new UserEntityBuilder().setUsername("username").setPassword("test").setFirstNamePerson("full").setLastNamePerson("name").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("num").setEmailAddress("email" +
            "@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of()).createUser();

    VolunteerProfileDTO volunteerProfileDTO = new VolunteerProfileDTO("username", "postalCode", "city", "street", "num", "email@email.com",
            "phone", "description",  Set.of(new Role(1L, "ROLE_VOLUNTEER")), 1.0, "fullname", 20, Optional.of("skill"));

    IndividualProfileDTO individualProfileDTO = new IndividualProfileDTO("username", "postalCode", "city", "street", "num", "email@email.com",
            "phone", "description", Set.of(new Role(2L, "ROLE_INDIVIDUAL")), 1.0, "fullname", 20);

    OrganizationProfileDTO organizationProfileDTO = new OrganizationProfileDTO("username", "postalCode", "city", "street", "num", "email@email.com", "phone", "description", Set.of(new Role(1L, "ROLE_ORGANIZATION")), 1.0, "organizationName");


    @Test
    void viewAllProfilesOfVolunteers() {
        user.setRoles(Set.of(new Role(1L, "ROLE_VOLUNTEER")));
        Mockito.when(userService.findAllVolunteers()).thenReturn(List.of(user));
        Mockito.when(userToProfileDTOTranslator.toVolunteerProfileDTO(user)).thenReturn(volunteerProfileDTO);
        Assertions.assertEquals(List.of(volunteerProfileDTO), profileDTOService.viewAllProfilesOfVolunteers());
        Mockito.verify(userService).findAllVolunteers();
        Mockito.verify(userToProfileDTOTranslator).toVolunteerProfileDTO(user);
    }

    @Test
    void viewAllProfilesOfVolunteers_noBirthDate() {
        user.setDateOfBirth(null);
        volunteerProfileDTO.setAge(0);
        Mockito.when(userService.findAllVolunteers()).thenReturn(List.of(user));
        Mockito.when(userToProfileDTOTranslator.toVolunteerProfileDTO(user)).thenReturn(volunteerProfileDTO);
        Assertions.assertEquals(List.of(volunteerProfileDTO), profileDTOService.viewAllProfilesOfVolunteers());
        Mockito.verify(userService).findAllVolunteers();
        Mockito.verify(userToProfileDTOTranslator).toVolunteerProfileDTO(user);
    }

    @Test
    void viewAllProfilesOfIndividuals() {
        user.setRoles(Set.of(new Role(2L, "ROLE_INDIVIDUAL")));
        Mockito.when(userService.findAllIndividuals()).thenReturn(List.of(user));
        Mockito.when(userToProfileDTOTranslator.toIndividualProfileDTO(user)).thenReturn(individualProfileDTO);
        Assertions.assertEquals(List.of(individualProfileDTO), profileDTOService.viewAllProfilesOfIndividuals());
        Mockito.verify(userService).findAllIndividuals();
        Mockito.verify(userToProfileDTOTranslator).toIndividualProfileDTO(user);
    }

    @Test
    void viewAllProfilesOfOrganizations() {
        user.setRoles(Set.of(new Role(2L, "ROLE_ORGANIZATION")));
        Mockito.when(userService.findAllOrganizations()).thenReturn(List.of(user));
        Mockito.when(userToProfileDTOTranslator.toOrganizationProfileDTO(user)).thenReturn(organizationProfileDTO);
        Assertions.assertEquals(List.of(organizationProfileDTO), profileDTOService.viewAllProfilesOfOrganizations());
        Mockito.verify(userService).findAllOrganizations();
        Mockito.verify(userToProfileDTOTranslator).toOrganizationProfileDTO(user);
    }

    @Test
    void viewAllProfilesOfOrganizers() {
        user.setRoles(Set.of(new Role(2L, "ROLE_INDIVIDUAL")));
        User user2 = new UserEntityBuilder().setUsername("username").setPassword("test").setFirstNamePerson("full").setLastNamePerson("name").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("num").setEmailAddress("email" +
                "@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of()).createUser();
        user2.setRoles(Set.of(new Role(2L, "ROLE_ORGANIZATION")));
        Mockito.when(userService.findAllIndividuals()).thenReturn(List.of(user));
        Mockito.when(userService.findAllOrganizations()).thenReturn(List.of(user2));
        Mockito.when(userToProfileDTOTranslator.toIndividualProfileDTO(user)).thenReturn(individualProfileDTO);
        Mockito.when(userToProfileDTOTranslator.toOrganizationProfileDTO(user2)).thenReturn(organizationProfileDTO);
        Assertions.assertEquals(List.of(individualProfileDTO, organizationProfileDTO), profileDTOService.viewAllProfilesOfOrganizers());
        Mockito.verify(userToProfileDTOTranslator).toIndividualProfileDTO(user);
        Mockito.verify(userToProfileDTOTranslator).toOrganizationProfileDTO(user2);
    }



    @Test
    void userNull() {
        String username = "username";
        User user = new UserEntityBuilder().setUsername("username").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setOrganizationName("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createUser();
        Long id = 1L;
        user.setId(id);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpStatusCodeException.class, () -> profileDTOService.viewProfile(username));
        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoInteractions(userToProfileDTOTranslator);

    }
    @Test
    void userNotNull_hasMaximumAmountOfRoles_rolesEquals_minIdSum_VOLUNTEER() {
        String username = "username";
        Long id = 1L;
        Role role = new Role( "ROLE_VOLUNTEER");
        role.setId(id);
        User user = new UserEntityBuilder().setUsername("username").setPassword("test").setFirstNamePerson("full").setLastNamePerson("name").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("num").setEmailAddress("email@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of(role)).createUser();
        user.setId(id);
        Set<Role> roles = user.getRoles();
        Rating rating = new RatingBuilder().setRatingValue(1).createRating();
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(Long.valueOf(rating.getRatingValue()));
        Mockito.when(userToProfileDTOTranslator.toVolunteerProfileDTO(user)).thenReturn(volunteerProfileDTO);

        profileDTOService.viewProfile(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userService).getRoleIdSum(roles);
        Mockito.verify(userToProfileDTOTranslator).toVolunteerProfileDTO(user);
    }

    @Test
    void userNotNull_hasMaximumAmountOfRoles_true_ORGANIZATION() {
        String username = "username";
        Role role = new Role( "ROLE_ORGANIZATION");
        User user = new UserEntityBuilder().setUsername("username").setPassword("test").setFirstNamePerson("full").setLastNamePerson("name").setOrganizationName("organizationName").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("num").setEmailAddress("email@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of(role)).createUser();
        Long id = 1L;
        user.setId(id);
        Set<Role> roles = user.getRoles();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(3L);

        Mockito.when(userToProfileDTOTranslator.toOrganizationProfileDTO(user)).thenReturn(organizationProfileDTO);

        profileDTOService.viewProfile(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userService).getRoleIdSum(roles);
        Mockito.verify(userToProfileDTOTranslator).toOrganizationProfileDTO(user);
    }
    @Test
    void userNotNull_hasMaximumAmountOfRoles_false_INDIVIDUAL() {
        String username = "username";
        Role role = new Role( "ROLE_INDIVIDUAL");

        User user = new UserEntityBuilder().setUsername("username").setPassword("test").setFirstNamePerson("full").setLastNamePerson("name").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("num").setEmailAddress("email@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of(role)).createUser();
        Long id = 1L;
        role.setId(2L);
        user.setId(id);
        Set<Role> roles = user.getRoles();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(2L);
        Mockito.when(userToProfileDTOTranslator.toIndividualProfileDTO(user)).thenReturn(individualProfileDTO);

        profileDTOService.viewProfile(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userService).getRoleIdSum(roles);
        Mockito.verify(userToProfileDTOTranslator).toIndividualProfileDTO(user);
    }

    @Test
    void userIsINDIVIDUAL_AND_VOLUNTEER() {
        String username = "username";
        Role role = new Role( "ROLE_INDIVIDUAL");
        Role role2 = new Role( "ROLE_VOLUNTEER");
        Long id = 1L;
        role.setId(2L);
        role2.setId(id);

        User user = new UserEntityBuilder().setUsername("username").setPassword("test").setFirstNamePerson("full").setLastNamePerson("name").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("num").setEmailAddress("email@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of(role, role2)).createUser();
        user.setId(id);
        Set<Role> roles = user.getRoles();
        Rating rating = new RatingBuilder().setRatingValue(1).createRating();
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(Long.valueOf(rating.getRatingValue()));
        VolunteerProfileDTO volunteerProfileDTO = new VolunteerProfileDTO("username", "postalCode", "city", "street", "num", "email@email.com", "phone", "description", Set.of(role), 1.0, "fullname", 20, Optional.of("skill"));
        Mockito.when(userToProfileDTOTranslator.toVolunteerProfileDTO(user)).thenReturn(volunteerProfileDTO);

        profileDTOService.viewProfile(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userService).getRoleIdSum(roles);
        Mockito.verify(userToProfileDTOTranslator).toVolunteerProfileDTO(user);
    }
}