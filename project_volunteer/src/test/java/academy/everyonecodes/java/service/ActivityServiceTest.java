package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.DraftRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityServiceTest
{

    @Autowired
    ActivityService activityService;

    @MockBean
    private ActivityRepository activityRepository;

    @MockBean
    private ActivityDraftTranslator translator;

    @MockBean
    private DraftRepository draftRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    ExceptionThrower exceptionThrower;

    @MockBean
    private EmailServiceImpl emailService;

    @MockBean
    JavaMailSender emailSender;

    @MockBean
    private ActivityStatusService activityStatusService;

    @MockBean RatingService ratingService;
    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    private final String endDateBeforeStartDate = "bad request";


    @Value("${activityCompletedEmail.subjectAndText}")
    private String activitycompletedmessage;

    @Mock
    private Authentication auth;

    @BeforeEach
    public void initSecurityContext()
    {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    Set<Role> roles = new HashSet<>(List.of(new Role(2L, "ROLE_INDIVIDUAL")));
    User organizer = new User(
            "username",
            "password",
            "email@email.com",
            roles
    );
    Set<User> applicants = new HashSet<>();
    Set<User> participants = new HashSet<>();
    String categories = "oneCategory";
    LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10));
    LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2100, 2, 1), LocalTime.of(10, 10, 10));

    Activity activity = new Activity(
            "title",
            "descr",
            "skills",
            List.of(categories),
            startDateTime,
            endDateTime,
            false,
            organizer,
            applicants,
            participants);

    Draft draft = new Draft(
            "title",
            "descr",
            "skills",
            categories,
            startDateTime,
            endDateTime,
            false,
            organizer.getUsername());


    @Test
    void getAllActivities_notEmpty()
    {
        Mockito.when(activityRepository.findAll()).thenReturn(List.of(activity));
        List<Activity> actual = activityService.getAllActivities();
        assertEquals(List.of(activity), actual);
        Mockito.verify(activityRepository).findAll();
    }

    @Test
    void getAllActivities_Empty()
    {
        Mockito.when(activityRepository.findAll()).thenReturn(List.of());
        List<Activity> actual = activityService.getAllActivities();
        assertEquals(List.of(), actual);
        Mockito.verify(activityRepository).findAll();
    }

    @Test
    void getActivitiesOfOrganizer_valid()
    {
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(activityRepository.findByOrganizer_Username(organizer.getUsername())).thenReturn(List.of(activity));
        List<Activity> actual = activityService.getActivitiesOfOrganizer("username");
        assertEquals(List.of(activity), actual);
        Mockito.verify(activityRepository).findByOrganizer_Username(organizer.getUsername());
    }

    @Test
    void getActivitiesOfOrganizer_empty()
    {
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(activityRepository.findByOrganizer_Username(organizer.getUsername())).thenReturn(List.of());
        List<Activity> actual = activityService.getActivitiesOfOrganizer("username");
        assertEquals(List.of(), actual);
        Mockito.verify(activityRepository).findByOrganizer_Username(organizer.getUsername());
    }

    @Test
    void findActivityById_valid()
    {
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        activityService.findActivityById(1L);
        verify(activityRepository).findById(1L);
    }

    @Test
    void findActivityById_not_found()
    {
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.findActivityById(1L);
        });
        verify(activityRepository).findById(1L);
    }


    @Test
    void postActivity_valid()
    {
        Mockito.when(translator.toActivity(draft)).thenReturn(activity);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(userRepository.findByUsername(organizer.getUsername())).thenReturn(Optional.of(organizer));
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);

        Activity actual = activityService.postActivity(draft);
        assertEquals(activity, actual);

        Mockito.verify(translator).toActivity(draft);
        Mockito.verify(draftRepository).delete(draft);
        Mockito.verify(userRepository).findByUsername(organizer.getUsername());
        Mockito.verify(activityRepository).save(activity);
    }

    @Test
    void postActivity_invalidAuthentication()
    {
        Mockito.when(translator.toActivity(draft)).thenReturn(activity);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(userRepository.findByUsername(organizer.getUsername())).thenReturn(Optional.empty());
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.postActivity(draft);
        });

        Mockito.verify(translator).toActivity(draft);
        Mockito.verify(userRepository).findByUsername(organizer.getUsername());
    }

    @Test
    void editActivity_valid()
    {
        activity.setId(1L);
        Mockito.when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(translator.toDraft(activity)).thenReturn(draft);
        Mockito.when(translator.toActivity(draft)).thenReturn(activity);
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);
        Mockito.when(userRepository.findByUsername(organizer.getUsername())).thenReturn(Optional.of(organizer));
        Activity actual = activityService.editActivity(activity);
        assertEquals(activity, actual);

        Mockito.verify(activityRepository).findById(activity.getId());
        Mockito.verify(translator).toDraft(activity);
        Mockito.verify(translator).toActivity(draft);
        Mockito.verify(activityRepository).save(activity);

    }

    @Test
    void editActivity_invalid_because_there_are_already_participants_and_applicants()
    {
        activity.setId(1L);
        activity.setApplicants(Set.of(organizer));
        activity.setParticipants(Set.of(organizer));
        Mockito.when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(translator.toDraft(activity)).thenReturn(draft);
        Mockito.when(translator.toActivity(draft)).thenReturn(activity);
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.editActivity(activity);
        });

        Mockito.verify(activityRepository).findById(activity.getId());
    }

    @Test
    void deleteActivity_valid()
    {
        activity.setId(1L);
        activity.setApplicants(Set.of(organizer));
        Mockito.when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        activityService.deleteActivity(1L);
        Mockito.verify(emailService).sendSimpleMessage(organizer.getEmailAddress(), "Activity was deleted", "The following activity was deleted by its creator: " + activity.getTitle());
        Mockito.verify(activityRepository).deleteById(1L);
    }

    @Test
    void deleteActivity_invalid_as_volunteers_have_already_been_accepted()
    {
        activity.setId(1L);
        activity.setParticipants(Set.of(organizer));
        Mockito.when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.deleteActivity(1L);
        });
    }

    @Test
    void getDraftsOfOrganizer_valid()
    {
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(draftRepository.findByOrganizerUsername(organizer.getUsername())).thenReturn(List.of(draft));

        List<Draft> actual = activityService.getDraftsOfOrganizer();
        assertEquals(List.of(draft), actual);

        Mockito.verify(draftRepository).findByOrganizerUsername(organizer.getUsername());
    }

    @Test
    void getDraftsOfOrganizer_emptyList()
    {
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(draftRepository.findByOrganizerUsername(organizer.getUsername())).thenReturn(List.of());

        List<Draft> actual = activityService.getDraftsOfOrganizer();
        assertEquals(List.of(), actual);

        Mockito.verify(draftRepository).findByOrganizerUsername(organizer.getUsername());
    }

    @Test
    void findDraftById_valid()
    {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(1L)).thenReturn(Optional.of(draft));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Draft actual = activityService.findDraftById(1L);
        assertEquals(draft, actual);
    }

    @Test
    void findDraftById_no_draft_found()
    {
        Mockito.when(draftRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.findDraftById(1L);
        });
        verify(draftRepository).findById(1L);
    }

    @Test
    void postDraft_valid()
    {
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(draftRepository.save(draft)).thenReturn(draft);

        Draft actual = activityService.postDraft(draft);
        assertEquals(draft, actual);

        Mockito.verify(draftRepository).save(draft);
    }

    @Test
    void editDraft_valid()
    {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.of(draft));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(draftRepository.save(draft)).thenReturn(draft);

        Draft actual = activityService.editDraft(draft);
        assertEquals(draft, actual);
        Mockito.verify(draftRepository).findById(draft.getId());
        Mockito.verify(draftRepository).save(draft);

    }

    @Test
    void editDraft_no_draft_found()
    {
        draft.setId(1L);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.editDraft(draft);
        });
    }

    @Test
    void saveDraftAsActivity_valid()
    {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.of(draft));
        Mockito.when(translator.toActivity(draft)).thenReturn(activity);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(userRepository.findByUsername(organizer.getUsername())).thenReturn(Optional.of(organizer));
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);


        Activity actual = activityService.saveDraftAsActivity(draft.getId());
        assertEquals(activity, actual);

        Mockito.verify(translator).toActivity(draft);
        Mockito.verify(draftRepository).delete(draft);
        Mockito.verify(userRepository).findByUsername(organizer.getUsername());
        Mockito.verify(activityRepository).save(activity);

    }

    @Test
    void saveDraftAsActivity_isEmpty()
    {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.empty());
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(translator.toActivity(new Draft())).thenReturn(new Activity(true));

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.saveDraftAsActivity(draft.getId());
        });
    }

    @Test
    void deleteDraft_valid()
    {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.of(draft));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        activityService.deleteDraft(1L);
        Mockito.verify(draftRepository).deleteById(1L);
    }

    @Test
    void deleteDraft_no_draft_found()
    {
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.empty());
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.deleteDraft(1L);
        });
    }

    //Complete Activity Tests------------------------------------------------------------------------------
    @Test
    void completeActivity_Activity_not_found() {
        Long activityId = 1L;
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.empty());


        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.completeActivity(1L, rating);
        });

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verifyNoInteractions(activityStatusService, ratingService, userRepository, draftRepository, translator);

    }

    @Test
    void completeActivity_User_isNot_Organizer() {
        Long activityId = 1L;
        Activity activity = new Activity("title", "description", startDateTime,  endDateTime,  true,  organizer);
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("noName");


        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.completeActivity(1L, rating);
        });

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verifyNoInteractions(activityStatusService, ratingService, userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_Activity_already_Completed() {
        Long activityId = 1L;
        Activity activity = new Activity("title", "description", startDateTime,  endDateTime,  true,  organizer);
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activity, Status.COMPLETED)));

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.completeActivity(1L, rating);
        });


        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verifyNoMoreInteractions(activityRepository, activityStatusService);
        Mockito.verifyNoInteractions(ratingService, userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_Activity_had_no_Participants() {
        Long activityId = 1L;
        Activity activity = new Activity("title", "description", startDateTime,  endDateTime,  true,  organizer);
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        activity.setParticipants(Set.of());
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activity, Status.PENDING)));

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityService.completeActivity(1L, rating);
        });

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verifyNoMoreInteractions(activityRepository, activityStatusService);
        Mockito.verifyNoInteractions(ratingService, userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_success_NoFeedback() {
        // TODO: Include Check for sent Email when its done
        Long activityId = 1L;
        Activity activity = new Activity("title", "description", startDateTime,  endDateTime,  true,  organizer);
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        User participant = new User(
                "particpant",
                "password",
                "email@email.com",
                Set.of(new Role("ROLE_VOLUNTEER"))
        );
        participant.setId(activityId);
        participants.add(participant);
        activity.setParticipants(participants);
        Rating rating = new Rating(5);
        String[] activityCompletedMessageArray = activitycompletedmessage.split(";");
        String title = activityCompletedMessageArray[0] + activity.getTitle();
        String text = activityCompletedMessageArray[1] + participant.getUsername() + activityCompletedMessageArray[2] + activity.getTitle() + activityCompletedMessageArray[3] + rating.getRating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activity, Status.ACTIVE)));
        Mockito.when(ratingService.rateUserForActivity(rating, activityId)).thenReturn(rating);
        Mockito.when(activityStatusService.changeActivityStatus(activity, Status.COMPLETED)).thenReturn(new ActivityStatus(activity, Status.COMPLETED));

        activityService.completeActivity(activityId, rating);

        Mockito.verify(emailService).sendSimpleMessage(participant.getEmailAddress(), title, text);
        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verify(ratingService).rateUserForActivity(rating, activityId);
        Mockito.verify(activityStatusService).changeActivityStatus(activity, Status.COMPLETED);

        Mockito.verifyNoMoreInteractions(activityRepository, activityStatusService, ratingService);
        Mockito.verifyNoInteractions( userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_success_WithFeedback() {

        Long activityId = 1L;
        Activity activity = new Activity("title", "description", startDateTime,  endDateTime,  true,  organizer);
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        User participant = new User(
                "particpant",
                "password",
                "email@email.com",
                Set.of(new Role("ROLE_VOLUNTEER"))
        );
        participant.setId(activityId);
        participants.add(participant);
        activity.setParticipants(participants);
        Rating rating = new Rating(5);
        rating.setFeedback("ich bin ein Feedback");
        String[] activityCompletedMessageArray = activitycompletedmessage.split(";");
        String title = activityCompletedMessageArray[0] + activity.getTitle();
        String text = activityCompletedMessageArray[1] + participant.getUsername() + activityCompletedMessageArray[2] + activity.getTitle() + activityCompletedMessageArray[3] + rating.getRating();
        String feedback = activityCompletedMessageArray[4] + rating.getFeedback();
        text = text + "\n" + feedback;

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activity, Status.ACTIVE)));
        Mockito.when(ratingService.rateUserForActivity(rating, activityId)).thenReturn(rating);
        Mockito.when(activityStatusService.changeActivityStatus(activity, Status.COMPLETED)).thenReturn(new ActivityStatus(activity, Status.COMPLETED));

       activityService.completeActivity(activityId, rating);

        Mockito.verify(emailService).sendSimpleMessage(participant.getEmailAddress(), title, text);
        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verify(ratingService).rateUserForActivity(rating, activityId);
        Mockito.verify(activityStatusService).changeActivityStatus(activity, Status.COMPLETED);

        Mockito.verifyNoMoreInteractions(activityRepository, activityStatusService, ratingService);
        Mockito.verifyNoInteractions( userRepository, draftRepository, translator);
    }
}
