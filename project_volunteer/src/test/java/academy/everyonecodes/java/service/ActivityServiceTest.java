package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.DraftRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityServiceTest {

    @Autowired
    ActivityService activityService;

    @MockBean
    private ActivityRepository activityRepository;

    @MockBean
    private ActivityDraftTranslator translator;

    @MockBean
    private DraftRepository draftRepository;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private Authentication auth;

    @BeforeEach
    public void initSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

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
    LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10));
    LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10));

    @Test
    void postActivity_valid(){

        Activity activity = new Activity(
                "title",
                "descr",
                "skills",
                List.of(categories),
                startDateTime,
                endDateTime,
                false,
                organizer,
                applicants,
                participants);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(organizer.getUsername());
        Mockito.when(userRepository.findByUsername(organizer.getUsername())).thenReturn(Optional.of(organizer));
        Mockito.when(activityRepository.save(activity)).thenReturn(activity);
        activityService.postActivity(activity);
        Mockito.verify(userRepository).findByUsername(organizer.getUsername());
        Mockito.verify(activityRepository).save(activity);
    }
}
