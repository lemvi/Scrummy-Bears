package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import academy.everyonecodes.java.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class StatusServiceTest
{
    @Autowired
    StatusService statusService;

    @MockBean
    ActivityRepository activityRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    EmailService emailService;

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

    @Test
    void acceptUser_valid()
    {
        activity.getApplicants().add(userToBeAccepted);
        activity.setTitle("title");
        userToBeAccepted.setEmailAddress("email@email.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userToBeAccepted));
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");

        statusService.acceptVolunteer(1L,1L);

        Mockito.verify(emailService).sendSimpleMessage(userToBeAccepted.getEmailAddress(), subjectAccepted, textAccepted);
        Mockito.verify(emailService).sendSimpleMessage(userToBeRejected.getEmailAddress(), subjectRejected, textRejected);

    }

    @Test
    void acceptUser_INVALID_Authentication_Fail()
    {
        activity.getApplicants().add(userToBeAccepted);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userToBeAccepted));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username2");
        assertThrows(HttpStatusCodeException.class, () ->
        {
            statusService.acceptVolunteer(1L, 1L);
        });
    }

    @Test
    void acceptUser_INVALID_User_Not_Found()
    {
        activity.getApplicants().add(userToBeAccepted);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        assertThrows(HttpStatusCodeException.class, () ->
        {
            statusService.acceptVolunteer(1L, 1L);
        });
    }

    @Test
    void acceptUser_INVALID_Activity_Not_Found()
    {
        userToBeAccepted.setId(1L);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userToBeAccepted));
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(HttpStatusCodeException.class, () ->
        {
            statusService.acceptVolunteer(1L, 1L);
        });
    }

    @Test
    void acceptUser_INVALID_User_Has_Not_Applied()
    {
        activity.setTitle("title");
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userToBeAccepted));
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        assertThrows(HttpStatusCodeException.class, () ->
        {
            statusService.acceptVolunteer(1L, 1L);
        });
    }
}
