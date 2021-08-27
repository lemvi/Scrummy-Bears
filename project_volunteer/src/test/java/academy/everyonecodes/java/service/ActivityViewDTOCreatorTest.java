package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO;
import academy.everyonecodes.java.data.dtos.OrganizerViewForVolunteerActivityViewDTO;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
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

        OrganizerViewForVolunteerActivityViewDTO organizerView = new OrganizerViewForVolunteerActivityViewDTO(
                username,
                role_organization.stream().map(role -> role.getRole()).collect(Collectors.toSet()),
                rating);

        ActivityViewDTO expected = new ActivityViewDTO(
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

        ActivityViewDTO actual = creator.createActivityViewDTO_forVolunteer(activity, userVolunteer);

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

        OrganizerViewForVolunteerActivityViewDTO organizerView = new OrganizerViewForVolunteerActivityViewDTO(
                username,
                role_organization.stream().map(role -> role.getRole()).collect(Collectors.toSet()),
                rating);

        ActivityViewDTO expected = new ActivityViewDTO(
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

        ActivityViewDTO actual = creator.createActivityViewDTO_forVolunteer(activity, userVolunteer);
        assertEquals(expected, actual);

    }
}