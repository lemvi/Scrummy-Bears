package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.SkillRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserToProfileDTOTranslator userToProfileDTOTranslator;
    private final String activitiesNotFound;

    public SearchService(ActivityRepository activityRepository, UserRepository userRepository, SkillRepository skillRepository, UserToProfileDTOTranslator userToProfileDTOTranslator, @Value("${errorMessages.activitiesNotFound}") String activitiesNotFound) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userToProfileDTOTranslator = userToProfileDTOTranslator;
        this.activitiesNotFound = activitiesNotFound;
    }
    public List<Activity> searchActivities(String text) {
        List<Activity> exactList = activityRepository.findFullTextSearchByText(text);

        List<Activity> titleList = activityRepository.findByTitleContainingIgnoreCase(text);
        List<Activity> descriptionList = activityRepository.findByDescriptionContainingIgnoreCase(text);
        List<Activity> recommendedSkillList = activityRepository.findByRecommendedSkillsContainingIgnoreCase(text);

        List<Activity> returnList =  mergeActvityLists(exactList, titleList, descriptionList, recommendedSkillList);
        if (returnList.isEmpty()) {
            System.out.println(activitiesNotFound + " " + text);
        }
        return returnList;
    }

    public List<Activity> mergeActvityLists(List<Activity> list1, List<Activity> list2, List<Activity> list3, List<Activity> list4) {
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

    public List<VolunteerProfileDTO> searchVolunteers(String text) {
        List<User> exactList = userRepository.findFullTextSearchByText(text);
        List<User> descriptionList = userRepository.findByDescriptionContainingIgnoreCase(text);
        List<Skill> skillFullTextSearch = skillRepository.findFullTextSearchByText(text);
        List<Skill> skillList = skillRepository.findBySkillContainingIgnoreCase(text);

        List<User> userList =  mergeUserLists(exactList,skillFullTextSearch, descriptionList, skillList);
        if (userList.isEmpty()) {
            System.out.println(activitiesNotFound + " " + text);
            //TODO: throw bad request
        }

        List<VolunteerProfileDTO> returnList = new ArrayList<>();
        for (User user : userList) {
            returnList.add(userToProfileDTOTranslator.toVolunteerProfileDTO(user));
        }

        return returnList;
    }
    public List<User> mergeUserLists(List<User> list1, List<Skill> skillLFullList,List<User> list2, List<Skill> skillList) {
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