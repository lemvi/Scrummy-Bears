package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityDraftTranslatorTest
{
    @Autowired
    ActivityDraftTranslator translator;

    @Autowired
    Validator validator;

    String categoriesString = "oneCategory";

    List<String> categories = List.of(categoriesString);

    @Test
    void toActivity_valid_all_fields()
    {
        Draft draft = new Draft(
                "title",
                "description",
                "recommendedSkills",
                categoriesString,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                null);

        Activity expected = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                null,
                new HashSet<>(),
                new HashSet<>()
        );

        var actual = translator.toActivity(draft);
        assertEquals(expected, actual);
        assertNoViolations(draft);
    }

    @Test
    void toActivity_valid_only_mandatory_fields()
    {
        Draft draft = new Draft(
                "title",
                "description",
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                null);

        Activity expected = new Activity(
                "title",
                "description",
                null,
                new ArrayList<>(),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                null,
                new HashSet<>(),
                new HashSet<>()
        );

        var actual = translator.toActivity(draft);
        assertEquals(expected, actual);
        assertNoViolations(draft);
    }

    @Test
    void toActivity_TITLE_empty__too_long() throws Exception
    {
        Draft draft = new Draft(
                "",
                "description",
                "recommendedSkills",
                categoriesString,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                null);

        assertViolations(draft);
        draft.setTitle("asgdhfjkasdgfktrqeotieüptipotjerqogjnsdfkjnkdfngbkjldshgtjkhgkjrehgrejkqhgrqeiotghewiutgerwiouzgiwerzvuioszviuoefziouqwezriouzreto");
        assertViolations(draft);
    }

    @Test
    void toActivity_DESCRIPTION_empty() throws Exception
    {
        Draft draft = new Draft(
                "title",
                "",
                "recommendedSkills",
                categoriesString,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                null);

        assertViolations(draft);
    }

    @Test
    void toActivity_START_DATE_TIME_empty__not_in_future() throws Exception
    {
        Draft draft = new Draft(
                "title",
                "description",
                "recommendedSkills",
                categoriesString,
                null,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                null);

        assertViolations(draft);
        draft.setStartDateTime(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)));
        assertViolations(draft);
    }

    @Test
    void toActivity_END_DATE_TIME_empty__not_in_future() throws Exception
    {
        Draft draft = new Draft(
                "title",
                "description",
                "recommendedSkills",
                categoriesString,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                null,
                true,
                null);

        assertViolations(draft);
        draft.setEndDateTime(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)));
        assertViolations(draft);
    }

    @Test
    void toActivity_IS_OPEN_END_empty__false() throws Exception
    {
        Draft draft = new Draft(
                "title",
                "description",
                "recommendedSkills",
                categoriesString,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                null,
                null);
        assertViolations(draft);
        draft.setOpenEnd(false);
        assertNoViolations(draft);
    }

    @Test
    void toDraft_valid_all_fields()
    {
        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                List.of("category1", "category2"),
                LocalDateTime.of(LocalDate.MIN, LocalTime.MIN),
                LocalDateTime.of(LocalDate.MAX, LocalTime.MAX),
                true,
                new User("username"),
                new HashSet<>(),
                new HashSet<>()
        );

        Draft expected = new Draft(
                "title",
                "description",
                "recommendedSkills",
                "category1;category2",
                LocalDateTime.of(LocalDate.MIN, LocalTime.MIN),
                LocalDateTime.of(LocalDate.MAX, LocalTime.MAX),
                true,
                null
        );

        Draft actual = translator.toDraft(activity);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toDraft_valid_empty()
    {
        Activity activity = new Activity();

        Draft expected = new Draft();

        Draft actual = translator.toDraft(activity);
        Assertions.assertEquals(expected, actual);
    }

    private void assertNoViolations(Draft draft)
    {
        Set<ConstraintViolation<Activity>> constraintViolations = validator.validate(translator.toActivity(draft));
        assertTrue(constraintViolations.isEmpty());
    }

    private void assertViolations(Draft draft)
    {
        Set<ConstraintViolation<Activity>> constraintViolations = validator.validate(translator.toActivity(draft));
        assertFalse(constraintViolations.isEmpty());
    }
}
