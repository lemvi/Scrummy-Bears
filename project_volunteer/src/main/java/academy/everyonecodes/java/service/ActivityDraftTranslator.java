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

    private List<String> getCategories(String categoriesString)
    {
        List<String> categories = new ArrayList<>();
        if (categoriesString != null && !categoriesString.isEmpty())
            categories = Arrays.asList(categoriesString.split(";"));
        return categories;
    }
}
