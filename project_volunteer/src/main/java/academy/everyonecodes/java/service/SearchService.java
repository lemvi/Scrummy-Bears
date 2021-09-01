package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ErrorMessage;
import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.SkillRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserToProfileDTOTranslator userToProfileDTOTranslator;
    private final RatingService ratingService;
    private final String activitiesNotFound;

    public SearchService(ActivityRepository activityRepository, UserRepository userRepository, SkillRepository skillRepository, UserToProfileDTOTranslator userToProfileDTOTranslator, RatingService ratingService, @Value("${errorMessages.activitiesNotFound}") String activitiesNotFound) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userToProfileDTOTranslator = userToProfileDTOTranslator;
        this.ratingService = ratingService;
        this.activitiesNotFound = activitiesNotFound;
    }

    //-------------SEARCH & FILTER ACTIVITIES---------------------------------
//-----------------BASIC SEARCH METHOD----------------------
    public List<Activity> searchActivities(String text) {
        List<Activity> exactList = activityRepository.findFullTextSearchByText(text);

        List<Activity> titleList = activityRepository.findByTitleContainingIgnoreCase(text);
        List<Activity> descriptionList = activityRepository.findByDescriptionContainingIgnoreCase(text);
        List<Activity> recommendedSkillList = activityRepository.findByRecommendedSkillsContainingIgnoreCase(text);

        List<Activity> returnList = mergeActivityLists(exactList, titleList, descriptionList, recommendedSkillList);
        if (returnList.isEmpty()) {
            System.out.println(activitiesNotFound + " " + text);
        }
        return returnList;
    }

    public List<Activity> searchActivities(String searchKeyword, Optional<String> oCreatorFilter, Optional<String> startDate, Optional<String> endDate, Optional<Integer> oRatingMin, Optional<Integer> oRatingMax) {
        List<Activity> searchList = searchActivities(searchKeyword);
        if (oCreatorFilter.isPresent() && startDate.isPresent() && endDate.isPresent() && oRatingMin.isPresent() && oRatingMax.isPresent()) {

            LocalDateTime start = LocalDate.parse(startDate.get()).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate.get()).atStartOfDay();
            String creatorFilter = oCreatorFilter.get();
            Integer ratingMin = oRatingMin.get();
            Integer ratingMax = oRatingMax.get();

            return searchList.stream()
                    .filter(n -> n.getOrganizer().getUsername().equals(creatorFilter))
                    .filter(n -> n.getStartDateTime().isAfter(start) && n.getStartDateTime().isBefore(end))
                    .filter(n -> n.getEndDateTime().isAfter(start) && n.getEndDateTime().isBefore(end))
                    .filter(n -> ratingService.calculateAverageUserRating(n.getId()) <= ratingMax && ratingService.calculateAverageUserRating(n.getId()) >= ratingMin)
                    .collect(Collectors.toList());
        }
        if (oCreatorFilter.isPresent() && startDate.isPresent() && endDate.isPresent() && oRatingMin.isEmpty() && oRatingMax.isEmpty()) {
            LocalDateTime start = LocalDate.parse(startDate.get()).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate.get()).atStartOfDay();
            String creatorFilter = oCreatorFilter.get();
            return searchList.stream()
                    .filter(n -> n.getOrganizer().getUsername().equals(creatorFilter))
                    .filter(n -> n.getStartDateTime().isAfter(start) && n.getStartDateTime().isBefore(end))
                    .filter(n -> n.getEndDateTime().isAfter(start) && n.getEndDateTime().isBefore(end))
                    .collect(Collectors.toList());
        }
        if (oCreatorFilter.isPresent() && startDate.isEmpty() && endDate.isEmpty() && oRatingMin.isPresent() && oRatingMax.isPresent()) {
            String creatorFilter = oCreatorFilter.get();
            Integer ratingMin = oRatingMin.get();
            Integer ratingMax = oRatingMax.get();
            return searchList.stream()
                    .filter(n -> n.getOrganizer().getUsername().equals(creatorFilter))
                    .filter(n -> ratingService.calculateAverageUserRating(n.getId()) <= ratingMax && ratingService.calculateAverageUserRating(n.getId()) >= ratingMin)
                    .collect(Collectors.toList());
        }
        if (oCreatorFilter.isPresent() && startDate.isEmpty() && endDate.isEmpty() && oRatingMin.isEmpty() && oRatingMax.isEmpty()) {
            String creatorFilter = oCreatorFilter.get();

            return searchList.stream()
                    .filter(n -> n.getOrganizer().getUsername().equals(creatorFilter))
                    .collect(Collectors.toList());
        }
        if (oCreatorFilter.isEmpty() && startDate.isPresent() && endDate.isPresent() && oRatingMin.isEmpty() && oRatingMax.isEmpty()) {
            LocalDateTime start = LocalDate.parse(startDate.get()).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate.get()).atStartOfDay();

            return searchList.stream()
                    .filter(n -> n.getStartDateTime().isAfter(start) && n.getStartDateTime().isBefore(end))
                    .filter(n -> n.getEndDateTime().isAfter(start) && n.getEndDateTime().isBefore(end))
                    .collect(Collectors.toList());
        }
        if (oCreatorFilter.isEmpty() && startDate.isEmpty() && endDate.isEmpty() && oRatingMin.isPresent() && oRatingMax.isPresent()) {
            Integer ratingMin = oRatingMin.get();
            Integer ratingMax = oRatingMax.get();
            return searchList.stream()
                    .filter(n -> ratingService.calculateAverageUserRating(n.getId()) <= ratingMax && ratingService.calculateAverageUserRating(n.getId()) >= ratingMin)
                    .collect(Collectors.toList());


        }
        return searchList;
    }

    //--------------------------SEARCH & FILTER VOLUNTEERS----------------
    //-----------------BASIC SEARCH METHOD----------------------

    public List<VolunteerProfileDTO> searchVolunteers(String text) {
        List<User> exactList = new ArrayList<>(userRepository.findFullTextSearchByText(text));
        List<User> descriptionList = new ArrayList<>(userRepository.findByDescriptionContainingIgnoreCase(text));
        List<Skill> skillFullTextSearch = new ArrayList<>(skillRepository.findFullTextSearchByText(text));
        List<Skill> skillList = new ArrayList<>(skillRepository.findBySkillContainingIgnoreCase(text));

        List<User> userList = mergeUserLists(exactList, skillFullTextSearch, descriptionList, skillList);
        if (userList.isEmpty()) {
            System.out.println(activitiesNotFound + " " + text);
            ExceptionThrower.badRequest(ErrorMessage.NO_VOLUNTEER_FOUND);

        }

        List<VolunteerProfileDTO> returnList = new ArrayList<>();
        for (User user : userList) {
            returnList.add(userToProfileDTOTranslator.toVolunteerProfileDTO(user));
        }

        return returnList;
    }

    public List<VolunteerProfileDTO> searchVolunteers(String searchKeyword, Optional<String> oPostalCode, Optional<Integer> oRatingMin, Optional<Integer> oRatingMax) {
        List<VolunteerProfileDTO> searchList = searchVolunteers(searchKeyword);
        if (oPostalCode.isPresent() && oRatingMin.isPresent() && oRatingMax.isPresent()) {
            String postalCode = oPostalCode.get();
            Integer ratingMin = oRatingMin.get();
            Integer ratingMax = oRatingMax.get();
            return searchList.stream()
                    .filter(n -> n.getPostalCode().equals(postalCode))
                    .filter(n -> ratingService.calculateAverageUserRating(userRepository.findByUsername(n.getUsername()).get().getId()) <= ratingMax && ratingService.calculateAverageUserRating(userRepository.findByUsername(n.getUsername()).get().getId()) >= ratingMin)
                    .collect(Collectors.toList());
        }
        if (oPostalCode.isPresent() && oRatingMin.isEmpty() && oRatingMax.isEmpty()) {
            String postalCode = oPostalCode.get();
            return searchList.stream()
                    .filter(n -> n.getPostalCode().equals(postalCode))
                    .collect(Collectors.toList());
        }
        if (oPostalCode.isEmpty() && oRatingMin.isPresent() && oRatingMax.isPresent()) {
            Integer ratingMin = oRatingMin.get();
            Integer ratingMax = oRatingMax.get();
            return searchList.stream()
                    .filter(n -> ratingService.calculateAverageUserRating(userRepository.findByUsername(n.getUsername()).get().getId()) <= ratingMax && ratingService.calculateAverageUserRating(userRepository.findByUsername(n.getUsername()).get().getId()) >= ratingMin)
                    .collect(Collectors.toList());

        }
        return searchList;
    }

//------------MERGE LIST METHODS------------------------------------------------------------------

    public List<Activity> mergeActivityLists(List<Activity> list1, List<Activity> list2, List<Activity> list3, List<Activity> list4) {
        for (Activity activity : list2) {
            if (!list1.contains(activity)) {
                list1.add(activity);
            }

        }
        for (Activity activity : list3) {
            if (!list1.contains(activity)) {
                list1.add(activity);
            }
        }
        for (Activity activity : list4) {
            if (!list1.contains(activity)) {
                list1.add(activity);
            }
        }
        return list1;
    }

    public List<User> mergeUserLists(List<User> list1, List<Skill> skillLFullList, List<User> list2, List<Skill> skillList) {
        for (Skill skill : skillLFullList) {
            if (!list1.contains(skill.getUser())) {
                list1.add(skill.getUser());
            }
        }
        for (User user : list2) {
            if (!list1.contains(user)) {
                list1.add(user);
            }
        }

        for (Skill skill : skillList) {
            if (!list1.contains(skill.getUser())) {
                list1.add(skill.getUser());
            }
        }

        return list1;
    }
}