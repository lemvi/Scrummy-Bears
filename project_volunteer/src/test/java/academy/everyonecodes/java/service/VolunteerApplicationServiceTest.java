package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import com.icegreen.greenmail.junit4.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class VolunteerApplicationServiceTest {

    @Autowired
    VolunteerApplicationService volunteerApplicationService;

    @MockBean
    ActivityRepository activityRepository;

    @MockBean
    ActivityService activityService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    EmailServiceImpl emailServiceImpl;

    @Value("${applicationEmail.subject}")
    String subject;

    @Mock
    private Authentication auth;

    @BeforeEach
    public void initSecurityContext()
    {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @MockBean
    JavaMailSender emailSender;

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @Value("${text800Chars}")
    String text800Chars;

    @Value("${text801Chars}")
    String text801Chars;


    Set<Role> rolesOrg = new HashSet<>(List.of(new Role(1L, "ROLE_VOLUNTEER")));
    User organizer = new UserEntityBuilder().setUsername("username").setPassword("password").setEmailAddress("email@email.com").setRoles(rolesOrg).createUser();
    Set<Role> rolesVol = new HashSet<>(List.of(new Role(1L, "ROLE_VOLUNTEER")));
    User volunteer = new UserEntityBuilder().setUsername("username").setPassword("password").setEmailAddress("email@email.com").setRoles(rolesVol).createUser();
    Set<User> applicants = new HashSet<>();
    Set<User> participants = new HashSet<>();
    String categories = "oneCategory";
    LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10));
    LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2100, 2, 1), LocalTime.of(10, 10, 10));

    Activity activity = new ActivityBuilder().setTitle("title").setDescription("descr").setRecommendedSkills("skills").setCategories(List.of(categories)).setStartDateTime(startDateTime).setEndDateTime(endDateTime).setOpenEnd(false).setOrganizer(organizer).setApplicants(applicants).setParticipants(participants).createActivity();

    @Test
    void apply_Text800Chars_Valid() {
        activity.setId(1L);
        System.out.println(text800Chars.length());
        Mockito.when(activityService.findActivityById(1L)).thenReturn(activity);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(volunteer.getUsername());
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(volunteer));
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);

        volunteerApplicationService.applyForActivityWithEmailAndText(activity.getId(), 1L, text800Chars);
        Assertions.assertThat(activity.getApplicants().size()).isEqualTo(1);

        Mockito.verify(activityService).findActivityById(1L);
        Mockito.verify(userRepository).findById(1L);
        Mockito.verify(activityRepository).save(activity);


        Mockito.verify(emailServiceImpl).sendSimpleMessage(organizer.getEmailAddress(),
                volunteer.getUsername() + subject + activity.getTitle(), text800Chars);

    }

    @Test
    void apply_TextMoreThan800Chars_notValid() {
        activity.setId(1L);
        System.out.println(text801Chars.length());
        Mockito.when(activityService.findActivityById(1L)).thenReturn(activity);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(volunteer.getUsername());
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(volunteer));
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);

        Exception exception = assertThrows(HttpStatusCodeException.class, () -> volunteerApplicationService.applyForActivityWithEmailAndText(1L, 1L, text801Chars));

        Assertions.assertThat(activity.getApplicants().size()).isEqualTo(1);
        Mockito.verify(activityService).findActivityById(1L);
        Mockito.verify(userRepository).findById(1L);
        Mockito.verify(activityRepository).save(activity);
        Mockito.verifyNoInteractions(emailServiceImpl);

    }



}
