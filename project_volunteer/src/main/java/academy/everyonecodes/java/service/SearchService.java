package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private final ActivityRepository activityRepository;
    private final String activitiesNotFound;

    public SearchService(ActivityRepository activityRepository, @Value("${errorMessages.activitiesNotFound}") String activitiesNotFound) {
        this.activityRepository = activityRepository;
        this.activitiesNotFound = activitiesNotFound;
    }
    public List<Activity> searchActivities(String text) {
        List<Activity> exactList = activityRepository.findFullTextSearchByText(text);

        List<Activity> titleList = activityRepository.findByTitleContainingIgnoreCase(text);
        List<Activity> descriptionList = activityRepository.findByDescriptionContainingIgnoreCase(text);
        List<Activity> recommendedSkillList = activityRepository.findByRecommendedSkillsContainingIgnoreCase(text);

        List<Activity> returnList =  mergeLists(exactList, titleList, descriptionList, recommendedSkillList);
        if (returnList.isEmpty()) {
            System.out.println(activitiesNotFound + " " + text);
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