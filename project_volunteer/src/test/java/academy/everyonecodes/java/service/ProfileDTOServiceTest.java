//package academy.everyonecodes.java.service;
//
//
//import academy.everyonecodes.java.data.DTOs.CompanyProfileDTO;
//import academy.everyonecodes.java.data.DTOs.IndividualProfileDTO;
//import academy.everyonecodes.java.data.DTOs.VolunteerProfileDTO;
//import academy.everyonecodes.java.data.*;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.time.LocalDate;
//import java.util.Optional;
//import java.util.Set;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//class ProfileDTOServiceTest {
//    //TODO HAS TO BE WRITTEN AFTER IMPLEMENTING RATING AND SKILLS
//
//    @Autowired
//    ProfileDTOService profileDTOService;
//    @MockBean
//    UserRepository userRepository;
//    @MockBean
//    UserToProfileDTOTranslator userToProfileDTOTranslator;
//    @MockBean
//    UserService userService;
//    @MockBean
//    ProfileViewer profileViewer;
//
//    @MockBean
//    RatingCalculator ratingCalculator;
//
//    @Value("${maxIdSum}")
//    int maxIdSum;
//    @Value("${minIdSum}")
//    int minIdSum;
//    @Value("${maxIdSum}")
//    String usernameNotFound;
//
//
//    @Test
//    void userNull() {
//        String username = "username";
//        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
//        Long id = 1L;
//        user.setId(id);
//        Set<Role> roles = user.getRoles();
//        Skill skill = new Skill("skill");
//        Rating rating = new Rating(1);
//        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);
//
//        profileDTOService.viewProfile(username);
//
//        Mockito.verify(userRepository).findByUsername(username);
//
//    }
//    @Test
//    void userNotNull_hasMaximumAmountOfRoles_rolesEquals_minIdSum_() {
//        String username = "username";
//        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
//        Long id = 1L;
//        user.setId(id);
//        Long userId = user.getId();
//        Set<Role> roles = user.getRoles();
//        Skill skill = new Skill("skill");
//        Rating rating = new Rating(1);
//        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(1L);
//        Mockito.when(profileViewer.getSkill(username)).thenReturn(Optional.of(skill));
//        Mockito.when(ratingCalculator.aggregateRating(userId)).thenReturn(1.0);
//        CompanyProfileDTO companyProfiles = new CompanyProfileDTO("test", "test", "test", "test", "test", "test", "test", "test", Set.of(new Role("test")), "test");
//        Mockito.when(userToProfileDTOTranslator.toCompanyProfileDTO(user)).thenReturn(companyProfiles);
//
//        profileDTOService.viewProfile(username);
//
//        Mockito.verify(userRepository).findByUsername(username);
//        Mockito.verify(userService).getRoleIdSum(roles);
//        Mockito.verify(profileViewer).getSkill(username);
//        Mockito.verify(ratingCalculator).aggregateRating(userId);
//        Mockito.verify(userToProfileDTOTranslator).toCompanyProfileDTO(user);
//    }
//
//    @Test
//    void userNotNull_hasMaximumAmountOfRoles_true() {
//        String username = "username";
//        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
//        Long id = 1L;
//        user.setId(id);
//        Long userId = user.getId();
//        Set<Role> roles = user.getRoles();
//        Skill skill = new Skill("skill");
//        Rating rating = new Rating(1);
//        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(3L);
//        Mockito.when(profileViewer.getSkill(username)).thenReturn(Optional.of(skill));
//        Mockito.when(ratingCalculator.aggregateRating(userId)).thenReturn(1.0);
//        IndividualProfileDTO individualProfileDTO = new IndividualProfileDTO("test", "test", "test", "test", "test", "test", "test", "", Set.of(new Role("test")), 1.0, "test", 20);
//        Mockito.when(userToProfileDTOTranslator.toIndividualProfileDTO(user)).thenReturn(individualProfileDTO);
//
//        profileDTOService.viewProfile(username);
//
//        Mockito.verify(userRepository).findByUsername(username);
//        Mockito.verify(userService).getRoleIdSum(roles);
//        Mockito.verify(profileViewer).getSkill(username);
//        Mockito.verify(ratingCalculator).aggregateRating(userId);
//        Mockito.verify(userToProfileDTOTranslator).toIndividualProfileDTO(user);
//    }
//    @Test
//    void userNotNull_hasMaximumAmountOfRoles_false() {
//        String username = "username";
//        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
//        Long id = 1L;
//        user.setId(id);
//        Long userId = user.getId();
//        Set<Role> roles = user.getRoles();
//        Skill skill = new Skill("skill");
//        Rating rating = new Rating(1);
//        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        Mockito.when(userService.getRoleIdSum(roles)).thenReturn(2L);
//        Mockito.when(profileViewer.getSkill(username)).thenReturn(Optional.of(skill));
//        Mockito.when(ratingCalculator.aggregateRating(userId)).thenReturn(1.0);
//        VolunteerProfileDTO volunteerProfileDTO = new VolunteerProfileDTO("test", "test", "test", "test", "test", "test", "test", "test", Set.of(new Role("test")), 1.0, "test", 20, "skills");
//        Mockito.when(userToProfileDTOTranslator.toVolunteerProfileDTO(user)).thenReturn(volunteerProfileDTO);
//
//        profileDTOService.viewProfile(username);
//
//        Mockito.verify(userRepository).findByUsername(username);
//        Mockito.verify(userService).getRoleIdSum(roles);
//        Mockito.verify(profileViewer).getSkill(username);
//        Mockito.verify(ratingCalculator).aggregateRating(userId);
//        Mockito.verify(userToProfileDTOTranslator).toVolunteerProfileDTO(user);
//    }
//}