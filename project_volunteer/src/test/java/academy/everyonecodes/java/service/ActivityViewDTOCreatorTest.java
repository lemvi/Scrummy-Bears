package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.DTOs.ActivityViewDTO;
import academy.everyonecodes.java.data.DTOs.OrganizerViewForVolunteerActivityViewDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
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
    private RatingCalculator ratingCalculator;

    @MockBean
    private StatusHandler statusHandler;

    @MockBean
    private RatingRepository ratingRepository;

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
        Set<Role> role_company = Set.of(new Role("ROLE_COMPANY"));
        userOrganizer.setRoles(role_company);
        activity.setTitle(title);
        activity.setOrganizer(userOrganizer);
        activity.setApplicants(Set.of(userVolunteer));
        activity.setParticipants(Set.of());
        activity.setStartDateTime(localDateTime);
        activity.setEndDateTime(localDateTime);
        activity.setOpenEnd(true);

        when(statusHandler.getStatusForSpecificActivityAndVolunteer(activity, userId)).thenReturn(status);
        when(ratingCalculator.aggregateRating(organizerId)).thenReturn(rating);
        when(ratingRepository.findByEventIdAndUser(activity.getId(), userOrganizer)).thenReturn(java.util.Optional.of(new Rating(3)));
        when(ratingRepository.findByEventIdAndUser(activity.getId(), userVolunteer)).thenReturn(java.util.Optional.of(new Rating(3)));

        OrganizerViewForVolunteerActivityViewDTO organizerView = new OrganizerViewForVolunteerActivityViewDTO(
                username,
                role_company.stream().map(role -> role.getRole()).collect(Collectors.toSet()),
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
        Set<Role> role_company = Set.of(new Role("ROLE_COMPANY"));
        userOrganizer.setRoles(role_company);
        activity.setTitle(title);
        activity.setOrganizer(userOrganizer);
        activity.setApplicants(Set.of(userVolunteer));
        activity.setParticipants(Set.of());
        activity.setStartDateTime(localDateTime);
        activity.setEndDateTime(localDateTime);
        activity.setOpenEnd(true);

        when(statusHandler.getStatusForSpecificActivityAndVolunteer(activity, userId)).thenReturn(status);
        when(ratingCalculator.aggregateRating(organizerId)).thenReturn(rating);
        when(ratingRepository.findByEventIdAndUser(activity.getId(), userOrganizer)).thenReturn(Optional.empty());
        when(ratingRepository.findByEventIdAndUser(activity.getId(), userVolunteer)).thenReturn(Optional.empty());

        OrganizerViewForVolunteerActivityViewDTO organizerView = new OrganizerViewForVolunteerActivityViewDTO(
                username,
                role_company.stream().map(role -> role.getRole()).collect(Collectors.toSet()),
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
}