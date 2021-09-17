package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import com.icegreen.greenmail.junit4.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
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
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityCompletionServiceTest {

    @Autowired
    ActivityCompletionService activityCompletionService;

    @MockBean
    ActivityService activityService;

    @MockBean
    ExceptionThrower exceptionThrower;

    @MockBean
    private EmailServiceImpl emailService;

    @MockBean
    JavaMailSender emailSender;

    @MockBean
    private ActivityStatusService activityStatusService;

    @MockBean
    RatingService ratingService;
    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    private final String endDateBeforeStartDate = "bad request";


    @Value("${activityCompletedEmail.subjectAndText}")
    private String activitycompletedmessage;

    @Mock
    private Authentication auth;

    @BeforeEach
    public void initSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    Set<Role> roles = new HashSet<>(List.of(new Role(2L, "ROLE_INDIVIDUAL")));
    User organizer = new UserEntityBuilder().setUsername("username").setPassword("password").setEmailAddress("email@email.com").setRoles(roles).createUser();
    Set<User> applicants = new HashSet<>();
    Set<User> participants = new HashSet<>();
    String categories = "oneCategory";
    LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10));
    LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2100, 2, 1), LocalTime.of(10, 10, 10));

    Activity activity = new ActivityBuilder().setTitle("title").setDescription("descr").setRecommendedSkills("skills").setCategories(List.of(categories)).setStartDateTime(startDateTime).setEndDateTime(endDateTime).setOpenEnd(false).setOrganizer(organizer).setApplicants(applicants).setParticipants(participants).createActivity();

    Draft draft = new DraftBuilder().setTitle("title").setDescription("descr").setRecommendedSkills("skills").setCategories(categories).setStartDateTime(startDateTime).setEndDateTime(endDateTime).setOpenEnd(false).setOrganizerUsername(organizer.getUsername()).createDraft();



    @Test
    void completeActivity_User_isNot_Organizer() {
        Long activityId = 1L;
        Activity activity = new ActivityBuilder().setTitle("title").setDescription("description").setStartDateTime(startDateTime).setEndDateTime(endDateTime).setOpenEnd(true).setOrganizer(organizer).createActivity();
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        Rating rating = new RatingBuilder().createRating();

        Mockito.when(activityService.findActivityById(activityId)).thenReturn(activity);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("noName");


        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityCompletionService.completeActivity(1L, rating);
        });

        verify(activityService).findActivityById(activityId);
        Mockito.verifyNoInteractions(activityStatusService, ratingService);
    }

    @Test
    void completeActivity_Activity_already_Completed() {
        Long activityId = 1L;
        Activity activity = new ActivityBuilder().setTitle("title").setDescription("description").setStartDateTime(startDateTime).setEndDateTime(endDateTime).setOpenEnd(true).setOrganizer(organizer).createActivity();
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        Rating rating = new RatingBuilder().createRating();

        //Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(activityService.findActivityById(activityId)).thenReturn(activity);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activity, Status.COMPLETED)));

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityCompletionService.completeActivity(1L, rating);
        });


        //Mockito.verify(activityService).findActivityById(activityId);
        verify(activityService).findActivityById(activityId);
        verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verifyNoInteractions(ratingService);
    }

    @Test
    void completeActivity_Activity_had_no_Participants() {
        Long activityId = 1L;
        Activity activity = new ActivityBuilder().setTitle("title").setDescription("description").setStartDateTime(startDateTime).setEndDateTime(endDateTime).setOpenEnd(true).setOrganizer(organizer).createActivity();
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        activity.setParticipants(Set.of());
        Rating rating = new RatingBuilder().createRating();

        Mockito.when(activityService.findActivityById(activityId)).thenReturn(activity);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activity, Status.PENDING)));

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityCompletionService.completeActivity(1L, rating);
        });

        verify(activityService).findActivityById(activityId);
        verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verifyNoMoreInteractions(activityStatusService);
        Mockito.verifyNoInteractions(ratingService);
    }

    @Test
    void completeActivity_success_NoFeedback() {
        // TODO: Include Check for sent Email when its done
        Long activityId = 1L;
        Activity activity = new ActivityBuilder().setTitle("title").setDescription("description").setStartDateTime(startDateTime).setEndDateTime(endDateTime).setOpenEnd(true).setOrganizer(organizer).createActivity();
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        User participant = new UserEntityBuilder().setUsername("particpant").setPassword("password").setEmailAddress("email@email.com").setRoles(Set.of(new Role("ROLE_VOLUNTEER"))).createUser();
        participant.setId(activityId);
        participants.add(participant);
        activity.setParticipants(participants);
        Rating rating = new RatingBuilder().setRatingValue(5).createRating();
        String[] activityCompletedMessageArray = activitycompletedmessage.split(";");
        String title = activityCompletedMessageArray[0] + activity.getTitle();
        String text = activityCompletedMessageArray[1] + participant.getUsername() + activityCompletedMessageArray[2] + activity.getTitle() + activityCompletedMessageArray[3] + rating.getRatingValue();

        Mockito.when(activityService.findActivityById(activityId)).thenReturn(activity);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activity, Status.ACTIVE)));
        Mockito.when(ratingService.rateUserForActivity(rating, activityId)).thenReturn(rating);
        Mockito.when(activityStatusService.changeActivityStatus(activity, Status.COMPLETED)).thenReturn(new ActivityStatus(activity, Status.COMPLETED));

        activityCompletionService.completeActivity(activityId, rating);

        verify(emailService).sendSimpleMessage(participant.getEmailAddress(), title, text);
        verify(activityService).findActivityById(activityId);
        verify(activityStatusService).getActivityStatus(activityId);
        verify(ratingService).rateUserForActivity(rating, activityId);
        verify(activityStatusService).changeActivityStatus(activity, Status.COMPLETED);

        Mockito.verifyNoMoreInteractions(activityStatusService, ratingService);
    }

    @Test
    void completeActivity_success_WithFeedback() {

        Long activityId = 1L;
        Activity activity = new ActivityBuilder().setTitle("title").setDescription("description").setStartDateTime(startDateTime).setEndDateTime(endDateTime).setOpenEnd(true).setOrganizer(organizer).createActivity();
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        User participant = new UserEntityBuilder().setUsername("particpant").setPassword("password").setEmailAddress("email@email.com").setRoles(Set.of(new Role("ROLE_VOLUNTEER"))).createUser();
        participant.setId(activityId);
        participants.add(participant);
        activity.setParticipants(participants);
        Rating rating = new RatingBuilder().setRatingValue(5).createRating();
        rating.setFeedback("ich bin ein Feedback");
        String[] activityCompletedMessageArray = activitycompletedmessage.split(";");
        String title = activityCompletedMessageArray[0] + activity.getTitle();
        String text = activityCompletedMessageArray[1] + participant.getUsername() + activityCompletedMessageArray[2] + activity.getTitle() + activityCompletedMessageArray[3] + rating.getRatingValue();
        String feedback = activityCompletedMessageArray[4] + rating.getFeedback();
        text = text + "\n" + feedback;

        Mockito.when(activityService.findActivityById(activityId)).thenReturn(activity);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activity, Status.ACTIVE)));
        Mockito.when(ratingService.rateUserForActivity(rating, activityId)).thenReturn(rating);
        Mockito.when(activityStatusService.changeActivityStatus(activity, Status.COMPLETED)).thenReturn(new ActivityStatus(activity, Status.COMPLETED));

        activityCompletionService.completeActivity(activityId, rating);

        verify(emailService).sendSimpleMessage(participant.getEmailAddress(), title, text);
        verify(activityService).findActivityById(activityId);
        verify(activityStatusService).getActivityStatus(activityId);
        verify(ratingService).rateUserForActivity(rating, activityId);
        verify(activityStatusService).changeActivityStatus(activity, Status.COMPLETED);

        Mockito.verifyNoMoreInteractions(activityStatusService, ratingService);
    }
}
