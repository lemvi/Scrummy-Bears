package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.DraftRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityServiceTest {

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
    private UserService userService;

    @MockBean
    private ActivityStatusService activityStatusService;

    @MockBean RatingService ratingService;

    private String endDateBeforeStartDate = "bad request";

    @Value("${errorMessages.noMatchingActivityFound}")
    private String noMatchingActivityFound;
    @Value("${errorMessages.userNotAuthorizedToCompleteActivity}")
    private String userNotAuthorizedToCompleteActivity;
    @Value("${errorMessages.activityAlreadyCompleted}")
    private String activityAlreadyCompleted;
    @Value("${errorMessages.noParticipantsForActivity}")
    private String noParticipantsForActivity;
    @Value("${message.activitycompleted}")
    private String activitycompletedmessage;

    @Mock
    private Authentication auth;

    @BeforeEach
    public void initSecurityContext() {
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
    LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10));

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
    void postActivity_valid() {
        Mockito.when(translator.toActivity(draft)).thenReturn(activity);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(userRepository.findByUsername(organizer.getUsername())).thenReturn(Optional.of(organizer));
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);

        Activity actual = activityService.postActivity(draft);
        Assertions.assertEquals(activity, actual);

        Mockito.verify(translator).toActivity(draft);
        Mockito.verify(draftRepository).delete(draft);
        Mockito.verify(userRepository).findByUsername(organizer.getUsername());
        Mockito.verify(activityRepository).save(activity);
    }

    @Test
    void postDraft_valid() {
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(draftRepository.save(draft)).thenReturn(draft);

        Draft actual = activityService.postDraft(draft);
        Assertions.assertEquals(draft, actual);

        Mockito.verify(draftRepository).save(draft);
    }

    @Test
    void getDraftsOfOrganizer_valid() {
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
      Mockito.when(draftRepository.findByOrganizer(organizer.getUsername())).thenReturn(List.of(draft));

      List<Draft> actual = activityService.getDraftsOfOrganizer();
      Assertions.assertEquals(List.of(draft), actual);

      Mockito.verify(draftRepository).findByOrganizer(organizer.getUsername());
    }


    @Test
    void editDraft_valid() {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.of(draft));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(draftRepository.save(draft)).thenReturn(draft);

        Optional<Draft> oDraft = activityService.editDraft(draft);
        Assertions.assertEquals(Optional.of(draft), oDraft);
        Mockito.verify(draftRepository).findById(draft.getId());
        Mockito.verify(draftRepository).save(draft);

    }

    @Test
    void editDraft_isEmpty() {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.empty());
        Optional<Draft> oDraft = activityService.editDraft(draft);
        Assertions.assertEquals(Optional.empty(), oDraft);
        Mockito.verify(draftRepository).findById(draft.getId());
        Mockito.verifyNoMoreInteractions(draftRepository);

    }

    @Test
    void saveDraftAsActivity_valid() {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.of(draft));
        Mockito.when(translator.toActivity(draft)).thenReturn(activity);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(userRepository.findByUsername(organizer.getUsername())).thenReturn(Optional.of(organizer));
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);

        Optional<Activity> oActivity = activityService.saveDraftAsActivity(draft.getId());
        Assertions.assertEquals(Optional.of(activity), oActivity);

        Mockito.verify(translator).toActivity(draft);
        Mockito.verify(draftRepository).delete(draft);
        Mockito.verify(userRepository).findByUsername(organizer.getUsername());
        Mockito.verify(activityRepository).save(activity);

    }

    @Test
    void saveDraftAsActivity_isEmpty() {
        draft.setId(1L);
        Mockito.when(draftRepository.findById(draft.getId())).thenReturn(Optional.empty());
        Optional<Activity> oActivity = activityService.saveDraftAsActivity(draft.getId());
        Assertions.assertEquals(Optional.empty(), oActivity);
        Mockito.verify(draftRepository).findById(draft.getId());
        Mockito.verifyNoInteractions(translator);
        Mockito.verifyNoMoreInteractions(draftRepository);
        Mockito.verifyNoInteractions(userRepository);
        Mockito.verifyNoInteractions(activityRepository);

    }

    @Test
    void getActivitiesOfOrganizer_valid() {
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(activityRepository.findByOrganizer_Username(organizer.getUsername())).thenReturn(List.of());

        List<Activity> actual = activityService.getActivitiesOfOrganizer();
        Assertions.assertEquals(List.of(), actual);

        Mockito.verify(activityRepository).findByOrganizer_Username(organizer.getUsername());
    }

    @Test
    void findById() {
        activityService.findById(1L);

        verify(activityRepository).findById(1L);
    }

    @Test
    void findAll() {
        activityService.getAllActivities();

        verify(activityRepository).findAll();
    }

    @Test
    void completeActivity_Activity_not_found() {
        Long activityId = 1L;
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        activityService.completeActivity(activityId, rating);

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(userService).throwBadRequest(noMatchingActivityFound);
        Mockito.verifyNoMoreInteractions(activityRepository, userService);
        Mockito.verifyNoInteractions(activityStatusService, ratingService, userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_User_isNot_Organizer() {
        Long activityId = 1L;
        Activity activity = new Activity();
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("noName");

        activityService.completeActivity(activityId, rating);

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(userService).throwBadRequest(userNotAuthorizedToCompleteActivity);
        Mockito.verifyNoMoreInteractions(activityRepository, userService);
        Mockito.verifyNoInteractions(activityStatusService, ratingService, userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_Activity_already_Completed() {
        Long activityId = 1L;
        Activity activity = new Activity();
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activityId, activity, Status.COMPLETED)));

        activityService.completeActivity(activityId, rating);

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verify(userService).throwBadRequest(activityAlreadyCompleted);
        Mockito.verifyNoMoreInteractions(activityRepository, userService, activityStatusService);
        Mockito.verifyNoInteractions(ratingService, userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_Activity_had_no_Participants() {
        Long activityId = 1L;
        Activity activity = new Activity();
        activity.setId(activityId);
        activity.setOrganizer(organizer);
        activity.setParticipants(Set.of());
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activityId, activity, Status.PENDING)));

        activityService.completeActivity(activityId, rating);

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verify(userService).throwBadRequest(noParticipantsForActivity);
        Mockito.verifyNoMoreInteractions(activityRepository, userService, activityStatusService);
        Mockito.verifyNoInteractions(ratingService, userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_success_NoFeedback() {
        // TODO: Include Check for sent Email when its done
        Long activityId = 1L;
        Activity activity = new Activity();
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
        Rating rating = new Rating();

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activityId, activity, Status.ACTIVE)));
        Mockito.when(ratingService.rateUserForActivity(rating, activityId)).thenReturn(rating);
        Mockito.when(activityStatusService.changeActivityStatus(activity, Status.COMPLETED)).thenReturn(new ActivityStatus(activityId, activity, Status.COMPLETED));

        activityService.completeActivity(activityId, rating);

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verify(ratingService).rateUserForActivity(rating, activityId);
        Mockito.verify(activityStatusService).changeActivityStatus(activity, Status.COMPLETED);

        Mockito.verifyNoMoreInteractions(activityRepository, activityStatusService, ratingService);
        Mockito.verifyNoInteractions(userService, userRepository, draftRepository, translator);
    }

    @Test
    void completeActivity_success_WithFeedback() {
        // TODO: Include Check for sent Email when its done
        Long activityId = 1L;
        Activity activity = new Activity();
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
        Rating rating = new Rating();
        rating.setFeedback("ich bin ein Feedback");

        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("username");
        Mockito.when(activityStatusService.getActivityStatus(activityId)).thenReturn(Optional.of(new ActivityStatus(activityId, activity, Status.ACTIVE)));
        Mockito.when(ratingService.rateUserForActivity(rating, activityId)).thenReturn(rating);
        Mockito.when(activityStatusService.changeActivityStatus(activity, Status.COMPLETED)).thenReturn(new ActivityStatus(activityId, activity, Status.COMPLETED));

        activityService.completeActivity(activityId, rating);

        Mockito.verify(activityRepository).findById(activityId);
        Mockito.verify(activityStatusService).getActivityStatus(activityId);
        Mockito.verify(ratingService).rateUserForActivity(rating, activityId);
        Mockito.verify(activityStatusService).changeActivityStatus(activity, Status.COMPLETED);

        Mockito.verifyNoMoreInteractions(activityRepository, activityStatusService, ratingService);
        Mockito.verifyNoInteractions(userService, userRepository, draftRepository, translator);
    }
}
