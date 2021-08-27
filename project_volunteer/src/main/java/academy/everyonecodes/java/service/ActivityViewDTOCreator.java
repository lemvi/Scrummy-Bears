package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO;
import academy.everyonecodes.java.data.dtos.OrganizerViewForVolunteerActivityViewDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityViewDTOCreator {

    private final RatingService ratingService;
    private final StatusHandler statusHandler;


    public ActivityViewDTOCreator(RatingService ratingService, StatusHandler statusHandler, UserService userService) {
        this.ratingService = ratingService;
        this.statusHandler = statusHandler;
    }

    public ActivityViewDTO createActivityViewDTO_forVolunteer(Activity activity, User user) {
        Long userId = user.getId();

        Status status = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, userId);
        User userOrganizer = activity.getOrganizer();
        OrganizerViewForVolunteerActivityViewDTO organizer = translateFromUser(userOrganizer);

        int ratingGivenToOrganizerAsInt = getRatingAsInt(activity, userOrganizer);
        int ratingReceivedByOrganizerAsInt = getRatingAsInt(activity, user);
        String feedbackGivenToOrganizer = getFeedbackAsString(activity, userOrganizer);
        String feedbackReceivedByOrganizer = getFeedbackAsString(activity, user);

        return new ActivityViewDTO(
                activity.getTitle(),
                status,
                activity.getStartDateTime(),
                activity.getEndDateTime(),
                activity.isOpenEnd(),
                organizer,
                ratingGivenToOrganizerAsInt,
                feedbackGivenToOrganizer,
                ratingReceivedByOrganizerAsInt,
                feedbackReceivedByOrganizer);
    }

    private OrganizerViewForVolunteerActivityViewDTO translateFromUser(User user) {
        return new OrganizerViewForVolunteerActivityViewDTO(
                user.getUsername(),
                user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()),
                ratingService.calculateAverageUserRating(user.getId())
        );
    }

    private int getRatingAsInt(Activity activity, User user) {
        int ratingAsInt = -1;
        Optional<Rating> oRating = getRating(activity, user);
        if (oRating.isPresent()) {
            Rating rating = oRating.get();
            ratingAsInt = rating.getRatingValue();
        }
        return ratingAsInt;
    }

    private String getFeedbackAsString(Activity activity, User user) {
        String feedbackString = "no feedback yet";
        Optional<Rating> oRating = getRating(activity, user);
        if (oRating.isPresent()) {
            Rating rating = oRating.get();
            feedbackString = rating.getFeedback();
        }
        return feedbackString;
    }

    private Optional<Rating> getRating(Activity activity, User user) {
        Optional<Rating> oRating = ratingService.findByActivityAndUser(activity, user);
        return oRating;
    }


}
