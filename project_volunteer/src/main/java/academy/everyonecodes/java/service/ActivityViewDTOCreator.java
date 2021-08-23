package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO;
import academy.everyonecodes.java.data.dtos.OrganizerViewForVolunteerActivityViewDTO;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityViewDTOCreator {

    private final RatingService ratingService;
    private final StatusHandler statusHandler;
    private final RatingRepository ratingRepository;


    public ActivityViewDTOCreator(RatingService ratingService, StatusHandler statusHandler, UserService userService, RatingRepository ratingRepository) {
        this.ratingService = ratingService;
        this.statusHandler = statusHandler;
        this.ratingRepository = ratingRepository;
    }

    public ActivityViewDTO createActivityViewDTO_forVolunteer(Activity activity, User user) {
        Long userId = user.getId();

        Status status = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, userId);
        User userOrganizer = activity.getOrganizer();
        OrganizerViewForVolunteerActivityViewDTO organizer = translateFromUser(userOrganizer);

        int ratingGivenToOrganizerAsInt = getRatingAsInt(activity, userOrganizer);
        int ratingReceivedByOrganizerAsInt = getRatingAsInt(activity, user);

        return new ActivityViewDTO(
                activity.getTitle(),
                status,
                activity.getStartDateTime(),
                activity.getEndDateTime(),
                activity.isOpenEnd(),
                organizer,
                ratingGivenToOrganizerAsInt,
                "feedback given",                     // TODO since we don't have feedback implemented yet i used a placeholder for now
                ratingReceivedByOrganizerAsInt,
                "feedback received");               // same as above
    }

    private OrganizerViewForVolunteerActivityViewDTO translateFromUser(User user) {
        return new OrganizerViewForVolunteerActivityViewDTO(
                user.getUsername(),
                user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()),
                ratingService.calculateAverageUserRating(user.getId())
        );
    }

    private int getRatingAsInt(Activity activity, User user) {
        Optional<Rating> oRating = ratingRepository.findByActivityAndUser(activity, user);
        int ratingAsInt = -1;
        if (oRating.isPresent()) {
            Rating rating = oRating.get();
            ratingAsInt = rating.getRating();
        }
        return ratingAsInt;
    }




}
