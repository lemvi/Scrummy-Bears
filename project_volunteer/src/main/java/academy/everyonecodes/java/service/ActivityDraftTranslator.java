package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.Validator;
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
        return new Activity(
                draft.getTitle(),
                draft.getDescription(),
                draft.getRecommendedSkills(),
                getCategories(draft.getCategories()),
                draft.getStartDateTime(),
                draft.getEndDateTime(),
                draft.isOpenEnd(),
                null,
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public Draft toDraft(Activity activity)
    {
        return new Draft(
                activity.getTitle(),
        activity.getDescription(),
        activity.getRecommendedSkills(),
        getCategoriesString(activity.getCategories()),
        activity.getStartDateTime(),
        activity.getEndDateTime(),
        activity.isOpenEnd(),
        activity.getOrganizer().getUsername()
        );
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
        String categoriesString = "";
        if (categories != null && !categories.isEmpty())
            categoriesString = categories.stream().collect(Collectors.joining(";"));
        return categoriesString;
    }
}
