package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private final ActivityRepository activityRepository;
    private final String activitisNotFound;

    public SearchService(ActivityRepository activityRepository, @Value("${errorMessages.activitisNotFound}") String activitisNotFound) {
        this.activityRepository = activityRepository;
        this.activitisNotFound = activitisNotFound;
    }
    public List<Activity> searchActivities(String text) {
        List<Activity> exactList = activityRepository.findFullTextSearchByText(text);

        List<Activity> titleList = activityRepository.findByTitleContainingIgnoreCase(text);
        List<Activity> descriptionList = activityRepository.findByDescriptionContainingIgnoreCase(text);
        List<Activity> recommendedSkillList = activityRepository.findByRecommendedSkillsContainingIgnoreCase(text);

        List<Activity> returnList =  mergeLists(exactList, titleList, descriptionList, recommendedSkillList);
        if (returnList.isEmpty()) {
            System.out.println(activitisNotFound + " " + text);
        }
        return returnList;
    }

    public List<Activity> mergeLists(List<Activity> list1, List<Activity> list2, List<Activity> list3, List<Activity> list4) {
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
}