package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_individualOrganization;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_volunteer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ActivityViewerServiceTest
{
    @Autowired
    private ActivityViewerService activityViewerService;

    @MockBean
    private ActivityViewDTOCreator creator;

    @MockBean
    private UserService userService;

    @MockBean
    ExceptionThrower exceptionThrower;

    @MockBean
    private ActivityService activityService;

    @Value("${errorMessages.usernameNotFound}")
    private String userNotFoundErrorMessage;

    @Value("${errorMessages.loggedInUserNotMatchingRequest}")
    private String loggedInUserNotMatchingRequestMessage;

    @Mock
    private Authentication auth;

    private String username = "username";

    private User volunteer = new User();

    private Activity activity = new Activity();

    private List<Activity> activities = List.of(activity);

    private Optional<User> oUser = Optional.of(volunteer);

    @BeforeEach
    public void initSecurityContext()
    {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @BeforeEach
    public void setUpTestData() {
        volunteer.setId(111L);
        activity.setApplicants(Set.of(volunteer));
        activity.setParticipants(Set.of(new User()));
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteer_notLoggedInUser()
    {
        String username = "username";

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer("wrongUser");
        });
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteer_usernameNotFound()
    {
        String username = "testUser";

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username);
        });
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteer_valid()
    {
        ActivityViewDTO_volunteer activityViewDTOVolunteer = new ActivityViewDTO_volunteer();

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(oUser);
        when(activityService.getAllActivities(false)).thenReturn(activities);
        when(creator.createActivityViewDTO_forVolunteer(activity, volunteer)).thenReturn(activityViewDTOVolunteer);

        List<ActivityViewDTO_volunteer> expected = List.of(activityViewDTOVolunteer);
        List<ActivityViewDTO_volunteer> actual = activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username);

        assertEquals(expected, actual);
        assertDoesNotThrow(() -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteer(username));
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus_pending_valid()
    {
        Status status = Status.PENDING;
        ActivityViewDTO_volunteer activityViewDTOVolunteer = new ActivityViewDTO_volunteer();
        activityViewDTOVolunteer.setStatus(status);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(oUser);
        when(activityService.getAllActivities(false)).thenReturn(activities);
        when(creator.createActivityViewDTO_forVolunteer(activity, volunteer)).thenReturn(activityViewDTOVolunteer);

        List<ActivityViewDTO_volunteer> expected = List.of(activityViewDTOVolunteer);
        List<ActivityViewDTO_volunteer> actual = activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, status);

        assertEquals(expected, actual);
        assertDoesNotThrow(() -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, status));
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus_completed_valid()
    {
        Status status = Status.COMPLETED;
        ActivityViewDTO_volunteer activityViewDTOVolunteer = new ActivityViewDTO_volunteer();
        activityViewDTOVolunteer.setStatus(status);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(oUser);
        when(activityService.getAllActivities(false)).thenReturn(activities);
        when(creator.createActivityViewDTO_forVolunteer(activity, volunteer)).thenReturn(activityViewDTOVolunteer);

        List<ActivityViewDTO_volunteer> expected = List.of(activityViewDTOVolunteer);
        List<ActivityViewDTO_volunteer> actual = activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, status);

        assertEquals(expected, actual);
        assertDoesNotThrow(() -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, status));
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus_active_valid()
    {
        Status status = Status.ACTIVE;
        ActivityViewDTO_volunteer activityViewDTOVolunteer = new ActivityViewDTO_volunteer();
        activityViewDTOVolunteer.setStatus(status);


        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(oUser);
        when(activityService.getAllActivities(false)).thenReturn(activities);
        when(creator.createActivityViewDTO_forVolunteer(activity, volunteer)).thenReturn(activityViewDTOVolunteer);

        List<ActivityViewDTO_volunteer> expected = List.of(activityViewDTOVolunteer);
        List<ActivityViewDTO_volunteer> actual = activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, status);

        assertEquals(expected, actual);
        assertDoesNotThrow(() -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, status));
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus_rejected_valid()
    {
        Status status = Status.REJECTED;
        ActivityViewDTO_volunteer activityViewDTOVolunteer = new ActivityViewDTO_volunteer();
        activityViewDTOVolunteer.setStatus(status);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(oUser);
        when(activityService.getAllActivities(false)).thenReturn(activities);
        when(creator.createActivityViewDTO_forVolunteer(activity, volunteer)).thenReturn(activityViewDTOVolunteer);

        List<ActivityViewDTO_volunteer> expected = List.of(activityViewDTOVolunteer);
        List<ActivityViewDTO_volunteer> actual = activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, status);

        assertEquals(expected, actual);
        assertDoesNotThrow(() -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, status));
    }

    @Test
    void getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus_applied_valid()
    {
        ActivityViewDTO_volunteer activityViewDTOVolunteer = new ActivityViewDTO_volunteer();
        activityViewDTOVolunteer.setStatus(Status.APPLIED);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(oUser);
        when(activityService.getAllActivities(false)).thenReturn(activities);
        when(creator.createActivityViewDTO_forVolunteer(activity, volunteer)).thenReturn(activityViewDTOVolunteer);

        List<ActivityViewDTO_volunteer> expected = List.of(activityViewDTOVolunteer);
        List<ActivityViewDTO_volunteer> actual = activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, Status.APPLIED);

        assertEquals(expected, actual);
        assertDoesNotThrow(() -> activityViewerService.getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, Status.APPLIED));
    }

    @Test
    void getAllActivitiesForSpecificVolunteer()
    {
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

        when(activityService.getAllActivities(false)).thenReturn(activities);

        List<Activity> expected = List.of(activity1);
        List<Activity> actual = activityViewerService.getAllActivitiesForSpecificVolunteer(volunteer1);
        assertEquals(expected, actual);

        expected = List.of(activity2);
        actual = activityViewerService.getAllActivitiesForSpecificVolunteer(volunteer2);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getParams")
    void getListOfActivityViewDTOsForSpecificIndividualOrOrganizationFilteredByStatus_valid(Status status) {
        ActivityViewDTO_individualOrganization activityViewDTO_individualOrganization = new ActivityViewDTO_individualOrganization();
        activityViewDTO_individualOrganization.setStatus(status);
        ActivityViewDTO_individualOrganization activityViewDTO_individualOrganization_other = new ActivityViewDTO_individualOrganization();
        activityViewDTO_individualOrganization_other.setStatus(Status.NOT_SET);

        User organizer = volunteer;
        activity.setOrganizer(organizer);
        Draft draft = new Draft();
        List<Draft> drafts = List.of(draft);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(oUser);
        when(activityService.getAllActivities(false)).thenReturn(activities);
        when(activityService.getDraftsOfOrganizer()).thenReturn(drafts);
        when(creator.createActivityViewDTO_forIndividualOrOrganization(draft, organizer)).thenReturn(activityViewDTO_individualOrganization);
        when(creator.createActivityViewDTO_forIndividualOrOrganization(activity, organizer)).thenReturn(activityViewDTO_individualOrganization_other);

        List<ActivityViewDTO_individualOrganization> expected = List.of(activityViewDTO_individualOrganization);
        List<ActivityViewDTO_individualOrganization> actual = activityViewerService.getListOfActivityViewDTOsForSpecificIndividualOrOrganizationFilteredByStatus(username, status);

        assertEquals(expected, actual);
        assertDoesNotThrow(() -> activityViewerService.getListOfActivityViewDTOsForSpecificIndividualOrOrganizationFilteredByStatus(username, status));

    }

    static Stream<Arguments> getParams() {
        return Stream.of(
                Arguments.of(Status.DRAFT),
                Arguments.of(Status.ACTIVE),
                Arguments.of(Status.COMPLETED)
        );
    }
}