package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityDraftTranslatorTest
{
    @Autowired
    ActivityDraftTranslator translator;

    @Autowired
    Validator validator;

    Set<Role> roles = new HashSet<>(List.of(new Role(2L, "ROLE_INDIVIDUAL")));
    User organizer = new User(
            "username",
            "password",
            "email@email.com",
            roles
    );
    Set<User> applicants = new HashSet<>();
    Set<User> participants = new HashSet<>();
    String categoriesString = "oneCategory";

    List<String> categories = List.of(categoriesString);

    @Test
    void toActivity_valid_all_fields() {
        Draft draft = new Draft(
                "title",
                "descr",
                "skills",
                categoriesString,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                organizer.getUsername());

        Activity expected = new Activity(
                "title",
                "descr",
                "skills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                null,
                applicants,
                participants);

        Activity actual = translator.toActivity(draft);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toActivity_valid_only_mandatory_fields() {
        Draft draft = new Draft(
                "title",
                "descr",
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                organizer.getUsername());

        Activity expected = new Activity(
                "title",
                "descr",
                null,
                new ArrayList<>(),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                null,
                applicants,
                participants);

        Activity actual = translator.toActivity(draft);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_TITLE_empty__too_long() throws Exception
    {
        Draft draft = new Draft(
                "",
                "descr",
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                organizer.getUsername());



        Set<ConstraintViolation<Activity>> constraintViolations = validator.validate(translator.toActivity(draft));
        System.out.println(constraintViolations);
        System.out.println("\n" + constraintViolations.size());
        assertTrue(!constraintViolations.isEmpty());
//        draft.setTitle("asgdhfjkasdgfktrqeotieüptipotjerqogjnsdfkjnkdfngbkjldshgtjkhgkjrehgrejkqhgrqeiotghewiutgerwiouzgiwerzvuioszviuoefziouqwezriouzreto");

//        assertThrows(ValidationException.class, () -> {
//            translator.toActivity(draft);
//        });
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_DESCRIPTION_empty() throws Exception
    {
        Activity activity = new Activity(
                "title",
                "",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer,
                applicants,
                participants
        );

    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_START_DATE_TIME_empty__not_in_future() throws Exception
    {
        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                null,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer,
                applicants,
                participants
        );

        activity.setStartDateTime(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_END_DATE_TIME_empty__not_in_future() throws Exception
    {
        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                null,
                true,
                organizer,
                applicants,
                participants
        );

        activity.setEndDateTime(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_IS_OPEN_END_empty__false() throws Exception
    {

        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                null,
                organizer,
                applicants,
                participants
        );

        activity.setOpenEnd(false);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_ORGANIZER_is_null() throws Exception
    {
        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                null,
                applicants,
                participants
        );

    }

    @Test
    void toActivity_all_left_out_fields() {
        Draft draft = new Draft(
                null,
                null,
                null,
                "",
                null,
                null,
                true,
                null);

        Activity expected = new Activity(
                null,
                null,
                null,
                List.of(""),
                null,
                null,
                true,
                null,
                applicants,
                participants);

        Activity actual = translator.toActivity(draft);
        Assertions.assertEquals(expected, actual);
    }

    private void assertNoViolations(Draft draft)
    {
        Set<ConstraintViolation<Activity>> constraintViolations = validator.validate(translator.toActivity(draft));
        System.out.println(constraintViolations);
        System.out.println("\n" + constraintViolations.size());
        assertTrue(!constraintViolations.isEmpty());
    }

    private void assertViolations(Draft draft)
    {

    }
}
