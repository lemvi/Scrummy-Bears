package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Draft draft = new DraftBuilder().setTitle("title").setDescription("description").setRecommendedSkills("recommendedSkills").setCategories(categoriesString).setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(true).setOrganizerUsername(null).createDraft();

        Activity expected = new ActivityBuilder().setTitle("title").setDescription("description").setRecommendedSkills("recommendedSkills").setCategories(categories).setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(true).setOrganizer(null).setApplicants(new HashSet<>()).setParticipants(new HashSet<>()).createActivity();

        var actual = translator.toActivity(draft);
        assertEquals(expected, actual);
        assertNoViolations(draft);
    }

    @Test
    void toActivity_valid_only_mandatory_fields()
    {
        Draft draft = new DraftBuilder().setTitle("title").setDescription("description").setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(true).setOrganizerUsername(null).createDraft();

        Activity expected = new ActivityBuilder().setTitle("title").setDescription("description").setRecommendedSkills(null).setCategories(new ArrayList<>()).setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(true).setOrganizer(null).setApplicants(new HashSet<>()).setParticipants(new HashSet<>()).createActivity();

        var actual = translator.toActivity(draft);
        assertEquals(expected, actual);
        assertNoViolations(draft);
    }

    @Test
    void toActivity_TITLE_empty__too_long() throws Exception
    {
        Draft draft = new DraftBuilder().setTitle("").setDescription("description").setRecommendedSkills("recommendedSkills").setCategories(categoriesString).setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(true).setOrganizerUsername(null).createDraft();

        assertViolations(draft);
        draft.setTitle("asgdhfjkasdgfktrqeotieüptipotjerqogjnsdfkjnkdfngbkjldshgtjkhgkjrehgrejkqhgrqeiotghewiutgerwiouzgiwerzvuioszviuoefziouqwezriouzreto");
        assertViolations(draft);
    }

    @Test
    void toActivity_DESCRIPTION_empty() throws Exception
    {
        Draft draft = new DraftBuilder().setTitle("title").setDescription("").setRecommendedSkills("recommendedSkills").setCategories(categoriesString).setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(true).setOrganizerUsername(null).createDraft();

        assertViolations(draft);
    }

    @Test
    void toActivity_START_DATE_TIME_empty__not_in_future() throws Exception
    {
        Draft draft = new DraftBuilder().setTitle("title").setDescription("description").setRecommendedSkills("recommendedSkills").setCategories(categoriesString).setStartDateTime(null).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(true).setOrganizerUsername(null).createDraft();

        assertViolations(draft);
        draft.setStartDateTime(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)));
        assertViolations(draft);
    }

    @Test
    void toActivity_END_DATE_TIME_empty__not_in_future() throws Exception
    {
        Draft draft = new DraftBuilder().setTitle("title").setDescription("description").setRecommendedSkills("recommendedSkills").setCategories(categoriesString).setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(null).setOpenEnd(true).setOrganizerUsername(null).createDraft();

        assertViolations(draft);
        draft.setEndDateTime(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)));
        assertViolations(draft);
    }

    @Test
    void toActivity_IS_OPEN_END_empty__false() throws Exception
    {
        Draft draft = new DraftBuilder().setTitle("title").setDescription("description").setRecommendedSkills("recommendedSkills").setCategories(categoriesString).setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(null).setOrganizerUsername(null).createDraft();
        assertViolations(draft);
        draft.setOpenEnd(false);
        assertNoViolations(draft);
    }

    @Test
    void toDraft_valid_all_fields()
    {
        Activity activity = new ActivityBuilder().setTitle("title").setDescription("description").setRecommendedSkills("recommendedSkills").setCategories(List.of("category1", "category2")).setStartDateTime(LocalDateTime.of(LocalDate.MIN, LocalTime.MIN)).setEndDateTime(LocalDateTime.of(LocalDate.MAX, LocalTime.MAX)).setOpenEnd(true).setOrganizer(new UserEntityBuilder().setUsername("username").createUser()).setApplicants(new HashSet<>()).setParticipants(new HashSet<>()).createActivity();

        Draft expected = new DraftBuilder().setTitle("title").setDescription("description").setRecommendedSkills("recommendedSkills").setCategories("category1;category2").setStartDateTime(LocalDateTime.of(LocalDate.MIN, LocalTime.MIN)).setEndDateTime(LocalDateTime.of(LocalDate.MAX, LocalTime.MAX)).setOpenEnd(true).setOrganizerUsername(null).createDraft();

        Draft actual = translator.toDraft(activity);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toDraft_valid_empty()
    {
        Activity activity = new ActivityBuilder().createActivity();

        Draft expected = new DraftBuilder().createDraft();

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
