package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class VolunteerAcceptanceServiceTest
{
    @Autowired
    VolunteerAcceptanceService volunteerAcceptanceService;

    @MockBean
    ActivityRepository activityRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    EmailServiceImpl emailServiceImpl;

    @Mock
    Authentication authentication;

    @Value("${acceptedVolunteerEmail.subject}")
    private String subjectAccepted;

    @Value("${acceptedVolunteerEmail.text}")
    private String textAccepted;

    @Value("${rejectedVolunteerEmail.subject}")
    private String subjectRejected;

    @Value("${rejectedVolunteerEmail.text}")
    private String textRejected;

    User userToBeAccepted = new User("username");
    User userToBeRejected = new User("loser");
    Activity activity = new Activity();
    Set<User> applicants = new HashSet<>();

    @BeforeEach
    public void initSecurityContext()
    {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void acceptUser_valid() throws MessagingException
    {
        userToBeAccepted.setId(1L);
        userToBeRejected.setId(2L);
        userToBeAccepted.setEmailAddress("email@email.com");
        userToBeRejected.setEmailAddress("email@email.com");
        applicants.add(userToBeAccepted);
        applicants.add(userToBeRejected);
        activity.setApplicants(applicants);
        System.out.println(activity.getApplicants());
        activity.setParticipants(new HashSet<>());
        activity.setTitle("title");
        activity.setOrganizer(new User("username"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToBeAccepted));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        when(activityRepository.save(activity)).thenReturn(activity);

        volunteerAcceptanceService.acceptVolunteer(1L, 1L);

        verify(emailServiceImpl).sendMessageWithAttachment(userToBeAccepted.getEmailAddress(), subjectAccepted, textAccepted + activity.getTitle(), "project_volunteer/src/main/resources/Scrummy Bears Logo.jpg");
        verify(emailServiceImpl).sendSimpleMessage(userToBeRejected.getEmailAddress(), subjectRejected, textRejected + activity.getTitle());

    }

    @Test
    void acceptUser_INVALID_Authentication_Fail()
    {
        activity.setApplicants(new HashSet<>());
        activity.getApplicants().add(userToBeAccepted);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userToBeAccepted));
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username2");
        assertThrows(HttpStatusCodeException.class, () ->
        {
            volunteerAcceptanceService.acceptVolunteer(1L, 1L);
        });
    }

    @Test
    void acceptUser_INVALID_User_Not_Found()
    {
        activity.setApplicants(new HashSet<>());
        activity.getApplicants().add(userToBeAccepted);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        assertThrows(HttpStatusCodeException.class, () ->
        {
            volunteerAcceptanceService.acceptVolunteer(1L, 1L);
        });
    }

    @Test
    void acceptUser_INVALID_Activity_Not_Found()
    {
        activity.setApplicants(new HashSet<>());
        userToBeAccepted.setId(1L);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        when(userRepository.findById(1L)).thenReturn(Optional.of(userToBeAccepted));
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(HttpStatusCodeException.class, () ->
        {
            volunteerAcceptanceService.acceptVolunteer(1L, 1L);
        });
    }

    @Test
    void acceptUser_INVALID_User_Not_applied()
    {
        userToBeAccepted.setId(1L);
        userToBeRejected.setId(2L);
        userToBeAccepted.setEmailAddress("email@email.com");
        userToBeRejected.setEmailAddress("email@email.com");
        applicants.add(userToBeRejected);
        activity.setApplicants(applicants);
        System.out.println(activity.getApplicants());
        activity.setParticipants(new HashSet<>());
        activity.setTitle("title");
        activity.setOrganizer(new User("username"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToBeAccepted));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");

        assertThrows(HttpStatusCodeException.class, () ->
        {
            volunteerAcceptanceService.acceptVolunteer(1L, 1L);
        });

    }
}
