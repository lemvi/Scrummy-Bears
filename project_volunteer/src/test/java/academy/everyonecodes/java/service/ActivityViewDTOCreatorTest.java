package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_individualOrganization;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_volunteer;
import academy.everyonecodes.java.data.dtos.OrganizerViewForActivityViewDTO_volunteer;
import academy.everyonecodes.java.data.dtos.VolunteerViewForActivityViewDTO_individualOrganization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ActivityViewDTOCreatorTest {

    @Autowired
    private ActivityViewDTOCreator creator;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private StatusHandler statusHandler;

    @Test
    void createActivityViewDTO_forVolunteer_valid() {
        Activity activity = new Activity();
        Long organizerId = 1L;
        Long userId = 2L;
        String title = "title";
        Status status = Status.ACTIVE;
        LocalDateTime localDateTime = LocalDateTime.MAX;
        User userOrganizer = new User();
        User userVolunteer = new User();
        userVolunteer.setId(userId);
        double rating = 3.0;
        int singleRating = 3;
        userOrganizer.setId(organizerId);
        String username = "username";
        userOrganizer.setUsername(username);
        Set<Role> role_organization = Set.of(new Role("ROLE_ORGANIZATION"));
        userOrganizer.setRoles(role_organization);
        activity.setTitle(title);
        activity.setOrganizer(userOrganizer);
        activity.setApplicants(Set.of(userVolunteer));
        activity.setParticipants(Set.of());
        activity.setStartDateTime(localDateTime);
        activity.setEndDateTime(localDateTime);
        activity.setOpenEnd(true);

        when(statusHandler.getStatusForSpecificActivityAndVolunteer(activity, userId)).thenReturn(status);
        when(ratingService.calculateAverageUserRating(organizerId)).thenReturn(rating);
        when(ratingService.findByActivityAndUser(activity, userOrganizer)).thenReturn(java.util.Optional.of(new Rating(3, "feedback given")));
        when(ratingService.findByActivityAndUser(activity, userVolunteer)).thenReturn(java.util.Optional.of(new Rating(3, "feedback received")));

        OrganizerViewForActivityViewDTO_volunteer organizerView = new OrganizerViewForActivityViewDTO_volunteer(
                username,
                role_organization.stream().map(role -> role.getRole()).collect(Collectors.toSet()),
                rating);

        ActivityViewDTO_volunteer expected = new ActivityViewDTO_volunteer(
                title,
                status,
                localDateTime,
                localDateTime,
                true,
                organizerView,
                singleRating,
                "feedback given",
                singleRating,
                "feedback received");

        ActivityViewDTO_volunteer actual = creator.createActivityViewDTO_forVolunteer(activity, userVolunteer);

        assertEquals(expected, actual);

    }

    @Test
    void createActivityViewDTO_forIndividualOrganization_completedActivity_valid() {
        Activity activity = new Activity();
        Long organizerId = 1L;
        Long userId = 2L;
        String title = "title";
        Status status = Status.COMPLETED;
        LocalDateTime localDateTime = LocalDateTime.MAX;
        User userOrganizer = new User();
        User userVolunteer = new User();
        userVolunteer.setId(userId);
        double rating = 3.0;
        int singleRating = 3;
        userOrganizer.setId(organizerId);
        String usernameOrganizer = "usernameOrganizer";
        String usernameVolunteer = "usernameVolunteer";
        userOrganizer.setUsername(usernameOrganizer);
        userVolunteer.setUsername(usernameVolunteer);
        Set<Role> role_organization = Set.of(new Role("ROLE_ORGANIZATION"));
        userOrganizer.setRoles(role_organization);
        activity.setTitle(title);
        activity.setOrganizer(userOrganizer);
        activity.setApplicants(Set.of());
        activity.setParticipants(Set.of(userVolunteer));
        activity.setStartDateTime(localDateTime);
        activity.setEndDateTime(localDateTime);
        activity.setOpenEnd(true);

        when(statusHandler.getStatusForSpecificActivity(activity)).thenReturn(status);
        when(ratingService.calculateAverageUserRating(userId)).thenReturn(rating);
        when(ratingService.findByActivityAndUser(activity, userOrganizer)).thenReturn(java.util.Optional.of(new Rating(3, "feedback received")));
        when(ratingService.findByActivityAndUser(activity, userVolunteer)).thenReturn(java.util.Optional.of(new Rating(3, "feedback given")));

        VolunteerViewForActivityViewDTO_individualOrganization volunteerView = new VolunteerViewForActivityViewDTO_individualOrganization(
                usernameVolunteer,
                rating);

        ActivityViewDTO_individualOrganization expected = new ActivityViewDTO_individualOrganization(
                title,
                status,
                localDateTime,
                localDateTime,
                true,
                volunteerView,
                singleRating,
                "feedback given",
                singleRating,
                "feedback received");

        ActivityViewDTO_individualOrganization actual = creator.createActivityViewDTO_forIndividualOrOrganization(activity, userOrganizer);

        assertEquals(expected, actual);

    }

    @Test
    void createActivityViewDTO_forIndividualOrganization_activeActivityWithParticipant_valid() {
        Activity activity = new Activity();
        Long organizerId = 1L;
        Long userId = 2L;
        String title = "title";
        Status status = Status.ACTIVE;
        LocalDateTime localDateTime = LocalDateTime.MAX;
        User userOrganizer = new User();
        User userVolunteer = new User();
        userVolunteer.setId(userId);
        double rating = 3.0;
        int singleRating = -1;
        userOrganizer.setId(organizerId);
        String usernameOrganizer = "usernameOrganizer";
        String usernameVolunteer = "usernameVolunteer";
        userOrganizer.setUsername(usernameOrganizer);
        userVolunteer.setUsername(usernameVolunteer);
        Set<Role> role_organization = Set.of(new Role("ROLE_ORGANIZATION"));
        userOrganizer.setRoles(role_organization);
        activity.setTitle(title);
        activity.setOrganizer(userOrganizer);
        activity.setApplicants(Set.of());
        activity.setParticipants(Set.of(userVolunteer));
        activity.setStartDateTime(localDateTime);
        activity.setEndDateTime(localDateTime);
        activity.setOpenEnd(true);

        when(statusHandler.getStatusForSpecificActivity(activity)).thenReturn(status);
        when(ratingService.calculateAverageUserRating(userId)).thenReturn(rating);
        when(ratingService.findByActivityAndUser(activity, userOrganizer)).thenReturn(java.util.Optional.empty());
        when(ratingService.findByActivityAndUser(activity, userVolunteer)).thenReturn(java.util.Optional.empty());

        VolunteerViewForActivityViewDTO_individualOrganization volunteerView = new VolunteerViewForActivityViewDTO_individualOrganization(
                usernameVolunteer,
                rating);

        ActivityViewDTO_individualOrganization expected = new ActivityViewDTO_individualOrganization(
                title,
                status,
                localDateTime,
                localDateTime,
                true,
                volunteerView,
                singleRating,
                "no feedback yet",
                singleRating,
                "no feedback yet");

        ActivityViewDTO_individualOrganization actual = creator.createActivityViewDTO_forIndividualOrOrganization(activity, userOrganizer);

        assertEquals(expected, actual);

    }

    @Test
    void createActivityViewDTO_forIndividualOrganization_draft_valid() {
        Draft draft = new Draft();
        Long organizerId = 1L;
        String title = "title";
        Status status = Status.DRAFT;
        LocalDateTime localDateTime = LocalDateTime.MAX;
        User userOrganizer = new User();
        int singleRating = -1;
        userOrganizer.setId(organizerId);
        String usernameOrganizer = "usernameOrganizer";
        userOrganizer.setUsername(usernameOrganizer);
        Set<Role> role_organization = Set.of(new Role("ROLE_ORGANIZATION"));
        userOrganizer.setRoles(role_organization);
        draft.setTitle(title);
        draft.setStartDateTime(localDateTime);
        draft.setEndDateTime(localDateTime);
        draft.setOpenEnd(true);


        VolunteerViewForActivityViewDTO_individualOrganization volunteerView = new VolunteerViewForActivityViewDTO_individualOrganization(
                null,
                0.0);

        ActivityViewDTO_individualOrganization expected = new ActivityViewDTO_individualOrganization(
                title,
                status,
                localDateTime,
                localDateTime,
                true,
                volunteerView,
                singleRating,
                "no feedback yet",
                singleRating,
                "no feedback yet");

        ActivityViewDTO_individualOrganization actual = creator.createActivityViewDTO_forIndividualOrOrganization(draft, userOrganizer);

        assertEquals(expected, actual);

    }

    @Test
    void createActivityViewDTO_forIndividualOrganization_activeActivityWithoutParticipant_valid() {
        Activity activity = new Activity();
        Long organizerId = 1L;
        Long userId = 2L;
        String title = "title";
        Status status = Status.ACTIVE;
        LocalDateTime localDateTime = LocalDateTime.MAX;
        User userOrganizer = new User();
        User userVolunteer = new User();
        userVolunteer.setId(userId);
        double rating = 3.0;
        int singleRating = -1;
        userOrganizer.setId(organizerId);
        String usernameOrganizer = "usernameOrganizer";
        String usernameVolunteer = "usernameVolunteer";
        userOrganizer.setUsername(usernameOrganizer);
        userVolunteer.setUsername(usernameVolunteer);
        Set<Role> role_organization = Set.of(new Role("ROLE_ORGANIZATION"));
        userOrganizer.setRoles(role_organization);
        activity.setTitle(title);
        activity.setOrganizer(userOrganizer);
        activity.setApplicants(Set.of(userVolunteer));
        activity.setParticipants(Set.of());
        activity.setStartDateTime(localDateTime);
        activity.setEndDateTime(localDateTime);
        activity.setOpenEnd(true);

        when(statusHandler.getStatusForSpecificActivity(activity)).thenReturn(status);
        when(ratingService.calculateAverageUserRating(userId)).thenReturn(rating);
        when(ratingService.findByActivityAndUser(activity, userOrganizer)).thenReturn(java.util.Optional.empty());
        when(ratingService.findByActivityAndUser(activity, userVolunteer)).thenReturn(java.util.Optional.empty());

        VolunteerViewForActivityViewDTO_individualOrganization volunteerView = new VolunteerViewForActivityViewDTO_individualOrganization(
                null,
                0.0);

        ActivityViewDTO_individualOrganization expected = new ActivityViewDTO_individualOrganization(
                title,
                status,
                localDateTime,
                localDateTime,
                true,
                volunteerView,
                singleRating,
                "no feedback yet",
                singleRating,
                "no feedback yet");

        ActivityViewDTO_individualOrganization actual = creator.createActivityViewDTO_forIndividualOrOrganization(activity, userOrganizer);

        assertEquals(expected, actual);

    }

    @Test
    void createActivityViewDTO_forVolunteer_singleRatingNotFound() {
        Activity activity = new Activity();
        Long organizerId = 1L;
        Long userId = 2L;
        String title = "title";
        Status status = Status.ACTIVE;
        LocalDateTime localDateTime = LocalDateTime.MAX;
        User userOrganizer = new User();
        User userVolunteer = new User();
        userVolunteer.setId(userId);
        double rating = 3.0;
        int singleRating = -1;
        userOrganizer.setId(organizerId);
        String username = "username";
        userOrganizer.setUsername(username);
        Set<Role> role_organization = Set.of(new Role("ROLE_ORGANIZATION"));
        userOrganizer.setRoles(role_organization);
        activity.setTitle(title);
        activity.setOrganizer(userOrganizer);
        activity.setApplicants(Set.of(userVolunteer));
        activity.setParticipants(Set.of());
        activity.setStartDateTime(localDateTime);
        activity.setEndDateTime(localDateTime);
        activity.setOpenEnd(true);

        when(statusHandler.getStatusForSpecificActivityAndVolunteer(activity, userId)).thenReturn(status);
        when(ratingService.calculateAverageUserRating(organizerId)).thenReturn(rating);
        when(ratingService.findByActivityAndUser(activity, userOrganizer)).thenReturn(Optional.empty());
        when(ratingService.findByActivityAndUser(activity, userVolunteer)).thenReturn(Optional.empty());

        OrganizerViewForActivityViewDTO_volunteer organizerView = new OrganizerViewForActivityViewDTO_volunteer(
                username,
                role_organization.stream().map(role -> role.getRole()).collect(Collectors.toSet()),
                rating);

        ActivityViewDTO_volunteer expected = new ActivityViewDTO_volunteer(
                title,
                status,
                localDateTime,
                localDateTime,
                true,
                organizerView,
                singleRating,
                "no feedback yet",
                singleRating,
                "no feedback yet");

        ActivityViewDTO_volunteer actual = creator.createActivityViewDTO_forVolunteer(activity, userVolunteer);
        assertEquals(expected, actual);

    }
}