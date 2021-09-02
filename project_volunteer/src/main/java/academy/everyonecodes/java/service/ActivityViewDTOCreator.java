package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_individualOrganization;
import academy.everyonecodes.java.data.dtos.ActivityViewDTO_volunteer;
import academy.everyonecodes.java.data.dtos.OrganizerViewForActivityViewDTO_volunteer;
import academy.everyonecodes.java.data.dtos.VolunteerViewForActivityViewDTO_individualOrganization;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityViewDTOCreator {

    private final RatingService ratingService;
    private final StatusHandler statusHandler;


    public ActivityViewDTOCreator(RatingService ratingService, StatusHandler statusHandler) {
        this.ratingService = ratingService;
        this.statusHandler = statusHandler;
    }

    public ActivityViewDTO_volunteer createActivityViewDTO_forVolunteer(Activity activity, User user) {
        Status status = statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());
        User userOrganizer = activity.getOrganizer();
        OrganizerViewForActivityViewDTO_volunteer organizer = translateFromOrganizer(userOrganizer);

        int ratingGivenToOrganizerAsInt = getRatingAsInt(activity, userOrganizer);
        int ratingReceivedByOrganizerAsInt = getRatingAsInt(activity, user);
        String feedbackGivenToOrganizer = getFeedbackAsString(activity, userOrganizer);
        String feedbackReceivedByOrganizer = getFeedbackAsString(activity, user);

        return new ActivityViewDTO_volunteer(
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

    public ActivityViewDTO_individualOrganization createActivityViewDTO_forIndividualOrOrganization(ActivityDraft activityDraft, User individualOrganization) {
        if (activityDraft.isActivity()) {
            return getActivityViewDTO_individualOrganization_activity( activityDraft, individualOrganization);

        }
        else {
            return getActivityViewDTO_individualOrganization_draft(activityDraft);
        }
    }

    private ActivityViewDTO_individualOrganization getActivityViewDTO_individualOrganization_activity(ActivityDraft activityDraft, User individualOrganization) {
        Status status;
        int ratingGivenToVolunteerAsInt = -1;
        int ratingReceivedByVolunteerAsInt = -1;
        String feedbackGivenToVolunteer = "no feedback yet";
        String feedbackReceivedByVolunteer = "no feedback yet";
        VolunteerViewForActivityViewDTO_individualOrganization volunteerView = new VolunteerViewForActivityViewDTO_individualOrganization();
        Activity activity = (Activity) activityDraft;
        status = statusHandler.getStatusForSpecificActivity(activity);
        Optional<User> oParticipant = activity.getParticipants().stream().findFirst();
        User participant = new User();
        if (oParticipant.isPresent()) {
            participant = oParticipant.get();
            volunteerView = translateFromVolunteer(participant);
        }

        if (status.equals(Status.COMPLETED)) {
            ratingGivenToVolunteerAsInt = getRatingAsInt(activity, participant);
            ratingReceivedByVolunteerAsInt = getRatingAsInt(activity, individualOrganization);
            feedbackGivenToVolunteer = getFeedbackAsString(activity, participant);
            feedbackReceivedByVolunteer = getFeedbackAsString(activity, individualOrganization);
        }
        return new ActivityViewDTO_individualOrganization(
                activity.getTitle(),
                status,
                activity.getStartDateTime(),
                activity.getEndDateTime(),
                activity.isOpenEnd(),
                volunteerView,
                ratingGivenToVolunteerAsInt,
                feedbackGivenToVolunteer,
                ratingReceivedByVolunteerAsInt,
                feedbackReceivedByVolunteer);
    }

    private ActivityViewDTO_individualOrganization getActivityViewDTO_individualOrganization_draft(ActivityDraft activityDraft) {
        Status status;
        int ratingGivenToVolunteerAsInt = -1;
        int ratingReceivedByVolunteerAsInt = -1;
        String feedbackGivenToVolunteer = "no feedback yet";
        String feedbackReceivedByVolunteer = "no feedback yet";
        VolunteerViewForActivityViewDTO_individualOrganization volunteer = new VolunteerViewForActivityViewDTO_individualOrganization();
        Draft draft = (Draft) activityDraft;
        status = Status.DRAFT;

        return new ActivityViewDTO_individualOrganization(
                draft.getTitle(),
                status,
                draft.getStartDateTime(),
                draft.getEndDateTime(),
                draft.isOpenEnd(),
                volunteer,
                ratingGivenToVolunteerAsInt,
                feedbackGivenToVolunteer,
                ratingReceivedByVolunteerAsInt,
                feedbackReceivedByVolunteer);
    }

    private OrganizerViewForActivityViewDTO_volunteer translateFromOrganizer(User user) {
        return new OrganizerViewForActivityViewDTO_volunteer(
                user.getUsername(),
                user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()),
                ratingService.calculateAverageUserRating(user.getId())
        );
    }

    private VolunteerViewForActivityViewDTO_individualOrganization translateFromVolunteer(User user) {
        return new VolunteerViewForActivityViewDTO_individualOrganization(
                user.getUsername(),
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
        return ratingService.findByActivityAndUser(activity, user);
    }


}
