package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.DTOs.ActivityViewDTO;
import academy.everyonecodes.java.data.DTOs.OrganizerViewForVolunteerActivityViewDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityViewDTOCreator {

    private final RatingCalculator ratingCalculator;
    private final StatusHandler statusHandler;
    private final UserService userService;
    private final RatingRepository ratingRepository;


    public ActivityViewDTOCreator(RatingCalculator ratingCalculator, StatusHandler statusHandler, UserService userService, RatingRepository ratingRepository) {
        this.ratingCalculator = ratingCalculator;
        this.statusHandler = statusHandler;
        this.userService = userService;
        this.ratingRepository = ratingRepository;
    }

    public ActivityViewDTO createActivityViewDTO_forVolunteer(Activity activity, User user) {
//        Optional<User> oUser = userService.findByUsername(user);
//        if(oUser.isEmpty()) {
//            userService.throwBadRequest("user not found");
//        }
//        User user = oUser.get();
        Long userId = user.getId();
        Long activityId = activity.getId();

        Status status = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, userId);
        User userOrganizer = activity.getOrganizer();
        OrganizerViewForVolunteerActivityViewDTO organizer = translateFromUser(userOrganizer);

        int ratingGivenToOrganizerAsInt = getRatingAsInt(activityId, userOrganizer);
        int ratingReceivedByOrganizerAsInt = getRatingAsInt(activityId, user);

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
                ratingCalculator.aggregateRating(user.getId())
        );
    }

    private int getRatingAsInt(Long eventId, User user) {
        Optional<Rating> oRating = ratingRepository.findByEventIdAndUser(eventId, user);
        int ratingAsInt = -1;
        if (oRating.isPresent()) {
            Rating rating = oRating.get();
            ratingAsInt = rating.getRating();
        }
        return ratingAsInt;
    }




}
