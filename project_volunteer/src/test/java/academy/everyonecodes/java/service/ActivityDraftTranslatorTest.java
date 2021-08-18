package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityDraftTranslatorTest
{
    @Autowired
    ActivityDraftTranslator translator;

    Set<Role> roles = new HashSet<>(List.of(new Role(2L, "ROLE_INDIVIDUAL")));
    User organizer = new User(
            "username",
            "password",
            "email@email.com",
            roles
    );
    Set<User> applicants = new HashSet<>();
    Set<User> participants = new HashSet<>();
    String categories = "oneCategory";

    @Test
    void toActivity_all_fields() {
        Draft draft = new Draft(
                "title",
                "descr",
                "skills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                organizer.getUsername());

        Activity expected = new Activity(
                "title",
                "descr",
                "skills",
                List.of(categories),
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
    /*
    @Test
    void toDraft_all_fields()
    {
        Activity activity = new Activity(
                "title",
                "descr",
                "skills",
                List.of(categories),
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                organizer,
                applicants,
                participants);

        Draft actual = translator.toDraft(activity);
        Draft expected = new Draft(
                "title",
                "descr",
                "skills",
                categories,
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                organizer.getUsername());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toDraft_all_left_out_fields() {

        Activity activity = new Activity(
                null,
                null,
                null,
                List.of(),
                null,
                null,
                true,
                organizer,
                applicants,
                participants);
        Draft actual = translator.toDraft(activity);
        Draft expected = new Draft(
                null,
                null,
                null,
                "",
                null,
                null,
                true,
                organizer.getUsername());
        Assertions.assertEquals(expected, actual);
    }

     */

}
