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

    @Test
    void toDraft()
    {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_INDIVIDUAL"));
        User organizer = new User("organizer", "password", "email@email.com", roles);
        Set<User> applicants = new HashSet<>();
        Set<User> participants = new HashSet<>();
        Activity activity = new Activity(
                "title",
                "descr",
                "skills",
                List.of("categoryOne", "categoryTwo"),
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
                "categoryOne;categoryTwo",
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                false,
                "organizer");
        Assertions.assertEquals(expected, actual);
    }
}
