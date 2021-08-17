package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ActivityEndpointTest
{

    @Autowired
    MockMvc mvc;

    @MockBean
    private ActivityService activityService;

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_valid_including_optional_fields() throws Exception
    {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2L, "ROLE_INDIVIDUAL"));
        User organizer = new User(
                "username",
                "password",
                "email@email.com",
                roles
        );
        organizer.setId(1L);
        Set<User> applicants = new HashSet<>();
        Set<User> participants = new HashSet<>();
        List<String> categories = List.of("oneCategory");

        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer,
                applicants,
                participants
        );

        String activityJson = createJson(activity);
        System.out.println(activityJson);

        mvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String createJson(Activity activity)
    {
        return "{" +
                "\"title\": \"" + activity.getTitle() + "\"," +
                "\"description\": \"" + activity.getDescription() + "\"," +
                "\"recommendedSkills\": \"" + activity.getRecommendedSkills() + "\"," +
                "\"categories\": " + createJsonPartForCategories(activity.getCategories()) + "," +
                "\"startDateTime\": \"" + activity.getStartDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\"," +
                "\"endDateTime\": \"" + activity.getEndDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\"," +
                "\"openEnd\": \"" + activity.isOpenEnd() + "\"," +
                "\"organizer\": " + createJsonPartForOneUser(activity.getOrganizer()) + "," +
                "\"applicants\": " + createJsonPartForUsers(activity.getApplicants()) + "," +
                "\"participants\": " + createJsonPartForUsers(activity.getParticipants()) +
                "}";
    }

    private String createJsonPartForUsers(Set<User> users)
    {
        return "[" +
                users.stream()
                        .map(this::createJsonPartForOneUser)
                        .collect(Collectors.joining(","))
                + "]";
    }

    private String createJsonPartForOneUser(User user)
    {
        return "{" +
                "\"id\": " + user.getId() + "," +
                "\"username\": \"" + user.getUsername() + "\"," +
                "\"password\": \"" + user.getPassword() + "\"," +
                "\"email\": \"" + user.getEmailAddress() + "\"," +
                "\"roles\": " + createJsonPartForRoles(user.getRoles()) +
                "}";
    }

    private String createJsonPartForRoles(Set<Role> roles)
    {
        return "[" +
                roles.stream()
                        .map(this::createJsonPartForOneRole)
                        .collect(Collectors.joining(","))
                + "]";
    }

    private String createJsonPartForOneRole(Role role)
    {
        return "{" +
                "\"id\": " + role.getId() + "," +
                "\"role\": \"" + role.getRole() +
                "\"}";
    }

    private String createJsonPartForCategories(List<String> categories)
    {
        return "[" +
                categories.stream()
                        .map(category -> category = "\"" + category + "\"")
                        .collect(Collectors.joining(","))
                + "]";
    }

}
