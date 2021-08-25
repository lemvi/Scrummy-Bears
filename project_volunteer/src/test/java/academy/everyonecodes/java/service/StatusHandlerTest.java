package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StatusHandlerTest {

    @Autowired
    private StatusHandler statusHandler;

    @MockBean
    private UserService userService;

    @Value("${errorMessages.usernameNotFound}")
    private String userNotFoundErrorMessage;

    @Test
    void getStatusForSpecificActivityAndVolunteer_PendingWhenActivityEndDateInFutureAndApplicant() {
        User user = new User();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new Activity();
        activity.setEndDateTime(LocalDateTime.MAX);
        activity.setApplicants(Set.of(user));

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Status expected = Status.PENDING;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getStatusForSpecificActivityAndVolunteer_PendingWhenActivityHasOpenEndAndApplicant() {
        User user = new User();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new Activity();
        activity.setEndDateTime(LocalDateTime.MIN);
        activity.setOpenEnd(true);
        activity.setApplicants(Set.of(user));

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Status expected = Status.PENDING;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getStatusForSpecificActivityAndVolunteer_CompletedWhenEndTimeInPastAndNotOpenEnd() {
        User user = new User();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new Activity();
        activity.setEndDateTime(LocalDateTime.MIN);
        activity.setApplicants(Set.of(user));
        activity.setOpenEnd(false);

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Status expected = Status.COMPLETED;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getStatusForSpecificActivityAndVolunteer_ActiveWhenEndDateInFutureAndParticipant() {
        User user = new User();
        Long userId = 1L;
        user.setId(userId);
        Activity activity = new Activity();
        activity.setEndDateTime(LocalDateTime.MAX);
        activity.setApplicants(Set.of());
        activity.setParticipants(Set.of(user));

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Status expected = Status.ACTIVE;
        Status actual = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());

        assertEquals(expected, actual);

        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }
}