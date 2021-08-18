package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.DTOs.ActivityViewDTO;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ActivityViewerServiceTest {

    @Autowired
    private ActivityViewerService activityViewerService;

    @MockBean
    private ActivityViewDTOCreator creator;

    @MockBean
    private UserService userService;

    @MockBean
    private ActivityService activityService;

    @Value("${errorMessages.usernameNotFound}")
    private String userNotFoundErrorMessage;

    @Value("${errorMessages.loggedInUserNotMatchingRequest}")
    private String loggedInUserNotMatchingRequestMessage;

    @Mock
    private Authentication auth;

    @BeforeEach
    public void initSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteer_notLoggedInUser() {
        String username = "username";

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        doThrow(new HttpStatusCodeException(HttpStatus.BAD_REQUEST) {}).when(userService).throwBadRequest(loggedInUserNotMatchingRequestMessage);

        assertThrows(HttpStatusCodeException.class, () -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer("wrongUser"), loggedInUserNotMatchingRequestMessage);
        verify(userService).throwBadRequest(loggedInUserNotMatchingRequestMessage);
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteer_usernameNotFound() {
        String username = "testUser";

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());
        doThrow(new HttpStatusCodeException(HttpStatus.BAD_REQUEST) {}).when(userService).throwBadRequest(userNotFoundErrorMessage);

        assertThrows(HttpStatusCodeException.class, () -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username), userNotFoundErrorMessage);
        verify(userService).throwBadRequest(userNotFoundErrorMessage);
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteer_valid() {
        String username = "username";

        User volunteer = new User();
        volunteer.setId(111L);

        Activity activity = new Activity();
        activity.setApplicants(Set.of(volunteer));
        activity.setParticipants(Set.of(new User()));

        ActivityViewDTO activityViewDTO = new ActivityViewDTO();

        List<Activity> activities = List.of(activity);

        Optional<User> oUser = Optional.of(volunteer);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(oUser);
        when(activityService.findAll()).thenReturn(activities);
        doThrow(new HttpStatusCodeException(HttpStatus.BAD_REQUEST) {}).when(userService).throwBadRequest(userNotFoundErrorMessage);
        when(creator.createActivityViewDTO_forVolunteer(activity, volunteer)).thenReturn(activityViewDTO);

        List<ActivityViewDTO> expected = List.of(activityViewDTO);
        List<ActivityViewDTO> actual = activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username);

        assertEquals(expected, actual);
        assertDoesNotThrow(() -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username));
    }

    @Test
    void getAllActivitiesForSpecificVolunteer() {
        User volunteer1 = new User();
        volunteer1.setId(1L);

        User volunteer2 = new User();
        volunteer2.setId(2L);

        Activity activity1 = new Activity();
        activity1.setApplicants(Set.of(volunteer1));
        activity1.setParticipants(Set.of());

        Activity activity2 = new Activity();
        activity2.setApplicants(Set.of());
        activity2.setParticipants(Set.of(volunteer2));

        List<Activity> activities = List.of(activity1, activity2);

        when(activityService.findAll()).thenReturn(activities);

        List<Activity> expected = List.of(activity1);
        List<Activity> actual = activityViewerService.getAllActivitiesForSpecificVolunteer(volunteer1);
        assertEquals(expected, actual);

        expected = List.of(activity2);
        actual = activityViewerService.getAllActivitiesForSpecificVolunteer(volunteer2);
        assertEquals(expected, actual);
    }
}