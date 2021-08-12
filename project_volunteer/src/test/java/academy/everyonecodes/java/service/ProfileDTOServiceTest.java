package academy.everyonecodes.java.service;


import academy.everyonecodes.java.data.CompanyDTO;
import academy.everyonecodes.java.data.ProfileDTOs.CompanyProfileDTO;
import academy.everyonecodes.java.data.ProfileDTOs.IndividualProfileDTO;
import academy.everyonecodes.java.data.ProfileDTOs.VolunteerProfileDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProfileDTOServiceTest {
    //TODO HAS TO BE WRITTEN AFTER IMPLEMENTING RATING AND SKILLS

    @Autowired
    ProfileDTOService profileDTOService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    UserToProfileDTOTranslator userToProfileDTOTranslator;
    @MockBean
    UserService userService;
    @MockBean
    ProfileViewer profileViewer;

    @Value("${maxIdSum}")
    int maxIdSum;
    @Value("${minIdSum}")
    int minIdSum;
    @Value("${maxIdSum}")
    String usernameNotFound;


    @Test
    void userNull() {
        String username = "username";
        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Long id = 1L;
        user.setId(id);
        Set<Role> roles = user.getRoles();
        Skill skill = new Skil("skill");
        Rating rating = new Rating(1);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);
      
        profileDTOService.viewProfile(username);

        Mockito.verify(userRepository).findByUsername(username);

    }
    @Test
    void userNotNull_hasMaximumAmountOfRoles_rolesEquals_minIdSum_() {
        String username = "username";
        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Long id = 1L;
        user.setId(id);
        Set<Role> roles = user.getRoles();
        Skill skill = new Skil("skill");
        Rating rating = new Rating(1);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(1L);
        Mockito.when(profileViewer.getSkill(username)).thenReturn(skill);
        Mockito.when(profileViewer.getRating(username)).thenReturn(rating);
        CompanyProfileDTO companyProfiles = new CompanyProfileDTO("test", "test", "test", "test", "test", "test", "test", "test", Set.of(new Role("test")), "test");
        Mockito.when(userToProfileDTOTranslator.toCompanyProfileDTO(user)).thenReturn(companyProfiles);

        profileDTOService.viewProfile(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userService).getRoleIdSum(roles);
        Mockito.verify(profileViewer).getSkill(username);
        Mockito.verify(profileViewer).getRating(username);
        Mockito.verify(userToProfileDTOTranslator).toCompanyProfileDTO(user);
    }

    @Test
    void userNotNull_hasMaximumAmountOfRoles_true() {
        String username = "username";
        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Long id = 1L;
        user.setId(id);
        Set<Role> roles = user.getRoles();
        Skill skill = new Skil("skill");
        Rating rating = new Rating(1);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(3L);
        Mockito.when(profileViewer.getSkill(username)).thenReturn(skill);
        Mockito.when(profileViewer.getRating(username)).thenReturn(rating);
        IndividualProfileDTO individualProfileDTO = new IndividualProfileDTO("test", "test", "test", "test", "test", "test", "test", "test", Set.of(new Role("test")), "test", 20);
        Mockito.when(userToProfileDTOTranslator.toIndividualProfileDTO(user)).thenReturn(individualProfileDTO);

        profileDTOService.viewProfile(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userService).getRoleIdSum(roles);
        Mockito.verify(profileViewer).getSkill(username);
        Mockito.verify(profileViewer).getRating(username);
        Mockito.verify(userToProfileDTOTranslator).toIndividualProfileDTO(user);
    }
    @Test
    void userNotNull_hasMaximumAmountOfRoles_false() {
        String username = "username";
        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Long id = 1L;
        user.setId(id);
        Set<Role> roles = user.getRoles();
        Skill skill = new Skil("skill");
        Rating rating = new Rating(1);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(2L);
        Mockito.when(profileViewer.getSkill(username)).thenReturn(skill);
        Mockito.when(profileViewer.getRating(username)).thenReturn(rating);
        VolunteerProfileDTO volunteerProfileDTO = new VolunteerProfileDTO("test", "test", "test", "test", "test", "test", "test", "test", Set.of(new Role("test")), "test", 20);
        Mockito.when(userToProfileDTOTranslator.toVolunteerProfileDTO(user)).thenReturn(volunteerProfileDTO);

        profileDTOService.viewProfile(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(userService).getRoleIdSum(roles);
        Mockito.verify(profileViewer).getSkill(username);
        Mockito.verify(profileViewer).getRating(username);
        Mockito.verify(userToProfileDTOTranslator).toVolunteerProfileDTO(user);
    }
}