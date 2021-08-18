package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class ActivityDraftTranslator
{
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
