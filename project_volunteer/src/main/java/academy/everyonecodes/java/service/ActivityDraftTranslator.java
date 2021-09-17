package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityBuilder;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.DraftBuilder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ActivityDraftTranslator
{
    public Activity toActivity(Draft draft)
    {
        return new ActivityBuilder().setTitle(draft.getTitle()).setDescription(draft.getDescription()).setRecommendedSkills(draft.getRecommendedSkills()).setCategories(getCategories(draft.getCategories())).setStartDateTime(draft.getStartDateTime()).setEndDateTime(draft.getEndDateTime()).setOpenEnd(draft.isOpenEnd()).setOrganizer(null).setApplicants(new HashSet<>()).setParticipants(new HashSet<>()).createActivity();
    }

    public Draft toDraft(Activity activity)
    {
        return new DraftBuilder().setTitle(activity.getTitle()).setDescription(activity.getDescription()).setRecommendedSkills(activity.getRecommendedSkills()).setCategories(getCategoriesString(activity.getCategories())).setStartDateTime(activity.getStartDateTime()).setEndDateTime(activity.getEndDateTime()).setOpenEnd(activity.isOpenEnd()).setOrganizerUsername(null).createDraft();
    }

    private List<String> getCategories(String categoriesString)
    {
        List<String> categories = new ArrayList<>();
        if (categoriesString != null && !categoriesString.isEmpty())
            categories = Arrays.asList(categoriesString.split(";"));
        return categories;
    }

    private String getCategoriesString(List<String> categories)
    {
        String categoriesString = null;
        if (categories != null && !categories.isEmpty())
            categoriesString = categories.stream().collect(Collectors.joining(";"));
        return categoriesString;
    }
}
