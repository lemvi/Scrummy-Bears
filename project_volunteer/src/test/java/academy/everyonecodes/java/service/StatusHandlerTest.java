package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.ActivityStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StatusHandlerTest
{

    @Autowired
    private StatusHandler statusHandler;

    @MockBean
    private UserService userService;

    @MockBean
    private ActivityStatusRepository activityStatusRepository;

    @Test
    void getStatusForSpecificActivity_CompletedWhenOrganizerSetsStatusCompleted()
    {
        Activity activity = new ActivityBuilder().createActivity();
        activity.setEndDateTime(LocalDateTime.MIN);
        activity.setApplicants(Set.of());
        activity.setParticipants(Set.of());
        activity.setOpenEnd(false);

        when(userService.findById(1L)).thenReturn(Optional.of(new UserEntityBuilder().createUser()));
        when(activityStatusRepository.findByActivity(activity)).thenReturn(Optional.of(new ActivityStatus(activity, Status.COMPLETED)));

        Status expected = Status.COMPLETED;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, 1L);

        assertEquals(expected, actual);


        verify(activityStatusRepository).findByActivity(activity);
        verify(userService).findById(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getStatusForSpecificActivityAndVolunteer_ActiveWhenStartDateInPastAndActivityHasOpenEndAndParticipant()
    {
        User user = new UserEntityBuilder().createUser();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new ActivityBuilder().createActivity();
        activity.setStartDateTime(LocalDateTime.MIN);
        activity.setEndDateTime(LocalDateTime.MIN);
        activity.setOpenEnd(true);
        activity.setApplicants(Set.of());
        activity.setParticipants(Set.of(user));

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(activityStatusRepository.findByActivity(activity)).thenReturn(Optional.of(new ActivityStatus(activity, Status.NOT_SET)));

        Status expected = Status.ACTIVE;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verify(activityStatusRepository).findByActivity(activity);
        verifyNoMoreInteractions(userService);
    }


    @Test
    void getStatusForSpecificActivityAndVolunteer_ActiveWhenStartDateInPastAndEndDateInFutureAndParticipant()
    {
        User user = new UserEntityBuilder().createUser();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new ActivityBuilder().createActivity();
        activity.setStartDateTime(LocalDateTime.now().minusMinutes(1L));
        activity.setEndDateTime(LocalDateTime.MAX);
        activity.setOpenEnd(false);
        activity.setApplicants(Set.of());
        activity.setParticipants(Set.of(user));

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Status expected = Status.ACTIVE;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getStatusForSpecificActivityAndVolunteer_AppliedWhenActivityStartDateInFutureAndApplicant()
    {
        User user = new UserEntityBuilder().createUser();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new ActivityBuilder().createActivity();
        activity.setStartDateTime(LocalDateTime.now().plusMinutes(1L));
        activity.setEndDateTime(LocalDateTime.MAX);
        activity.setOpenEnd(false);
        activity.setApplicants(Set.of(user));
        activity.setParticipants(Set.of());

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Status expected = Status.APPLIED;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getStatusForSpecificActivityAndVolunteer_PendingWhenActivityStartDateInFutureAndParticipant()
    {
        User user = new UserEntityBuilder().createUser();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new ActivityBuilder().createActivity();
        activity.setStartDateTime(LocalDateTime.now().plusMinutes(1L));
        activity.setEndDateTime(LocalDateTime.MAX);
        activity.setOpenEnd(false);
        activity.setApplicants(Set.of());
        activity.setParticipants(Set.of(user));

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Status expected = Status.PENDING;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getStatusForSpecificActivityAndVolunteer_Rejected()
    {
        User user = new UserEntityBuilder().createUser();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new ActivityBuilder().createActivity();
        activity.setStartDateTime(LocalDateTime.now().plusMinutes(1L));
        activity.setEndDateTime(LocalDateTime.MAX);
        activity.setOpenEnd(false);
        activity.setApplicants(Set.of(user));
        activity.setParticipants(Set.of(new UserEntityBuilder().createUser()));

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Status expected = Status.REJECTED;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getStatusForSpecificActivity_completed() {
        Activity activity = new ActivityBuilder().createActivity();
        when(activityStatusRepository.findByActivity(activity)).thenReturn(Optional.of(new ActivityStatus(activity, Status.COMPLETED)));
        Status actual = statusHandler.getStatusForSpecificActivity(activity);
        assertEquals(Status.COMPLETED, actual);
    }

    @Test
    void getStatusForSpecificActivity_active() {
        Activity activity = new ActivityBuilder().createActivity();
        when(activityStatusRepository.findByActivity(activity)).thenReturn(Optional.empty());
        Status actual = statusHandler.getStatusForSpecificActivity(activity);
        assertEquals(Status.ACTIVE, actual);
    }


}