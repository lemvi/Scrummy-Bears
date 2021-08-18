package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class ActivityDraftTranslator
{
    /*
    public Draft toDraft(Activity activity)
    {
        return new Draft(
                activity.getTitle(),
                activity.getDescription(),
                activity.getRecommendedSkills(),
                activity.getCategories().stream()
                        .collect(Collectors.joining(";")),
                activity.getStartDateTime(),
                activity.getEndDateTime(),
                activity.isOpenEnd(),
                activity.getOrganizer().getUsername()
        );
    }

     */

    public Activity toActivity(Draft draft)
    {
        return new Activity(
                draft.getTitle(),
                draft.getDescription(),
                draft.getRecommendedSkills(),
                Arrays.asList(draft.getCategories().split(";")),
                draft.getStartDateTime(),
                draft.getEndDateTime(),
                draft.isOpenEnd(),
                null,
                new HashSet<>(),
                new HashSet<>()
        );
    }
}
