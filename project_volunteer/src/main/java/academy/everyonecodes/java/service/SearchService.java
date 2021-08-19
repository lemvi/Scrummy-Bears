package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityRepository;
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

        for (int i = 0; i < list1.size() || i < list2.size() || i < list3.size() || i < list4.size(); i++) {

            if (list2.size() > i && !list1.contains(list2.get(i))) {
                list1.add(list2.get(i));
            }
            if (list3.size() > i && !list1.contains(list3.get(i))) {
                list1.add(list3.get(i));
            }
            if (list4.size() > i && !list1.contains(list4.get(i))) {
                list1.add(list4.get(i));
            }
        }
        return list1;
    }
}