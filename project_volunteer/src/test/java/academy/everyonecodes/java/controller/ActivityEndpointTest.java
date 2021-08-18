package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.ActivityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    ObjectMapper objectMapper = new ObjectMapper();
    Set<Role> roles = new HashSet<>(List.of(new Role(2L, "ROLE_INDIVIDUAL")));
    User organizer = new User(
            "username",
            "password",
            "email@email.com",
            roles
    );
    Set<User> applicants = new HashSet<>();
    Set<User> participants = new HashSet<>();
    List<String> categories = List.of("oneCategory");

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_valid_including_optional_fields() throws Exception
    {
        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer,
                applicants,
                participants
        );

        assertIsOK(activity);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_valid_only_mandatory_fields() throws Exception
    {
        Activity activity = new Activity(
                "title",
                "description",
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer
        );

        assertIsOK(activity);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_TITLE_empty__too_long() throws Exception
    {
        Activity activity = new Activity(
                "",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer,
                applicants,
                participants
        );

        assertIsBadRequest(activity);
        activity.setTitle("asgdhfjkasdgfktrqeotieüptipotjerqogjnsdfkjnkdfngbkjldshgtjkhgkjrehgrejkqhgrqeiotghewiutgerwiouzgiwerzvuioszviuoefziouqwezriouzreto");
        assertIsBadRequest(activity);
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

        assertIsBadRequest(activity);
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
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer,
                applicants,
                participants
        );

        assertIsBadRequest(activity);
        activity.setStartDateTime(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)));
        assertIsBadRequest(activity);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_END_DATE_TIME_including_optional_fields() throws Exception
    {
        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)),
                null,
                true,
                organizer,
                applicants,
                participants
        );

        assertIsBadRequest(activity);
        activity.setEndDateTime(LocalDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(10, 10, 10)));
        assertIsBadRequest(activity);
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

        assertIsBadRequest(activity);
        activity.setOpenEnd(false);
        assertIsOK(activity);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_ORGANIZER_including_optional_fields() throws Exception
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

        assertIsBadRequest(activity);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_APPLICANTS_not_Empty() throws Exception
    {
        applicants.add(organizer);
        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer,
                applicants,
                participants
        );

        assertIsBadRequest(activity);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_PARTICIPANTS_not_Empty() throws Exception
    {
        participants.add(organizer);
        Activity activity = new Activity(
                "title",
                "description",
                "recommendedSkills",
                categories,
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
                true,
                organizer,
                applicants,
                participants
        );

        assertIsBadRequest(activity);

    }

    private void assertIsOK(Activity activity) throws Exception
    {
        organizer.setId(1L);
        objectMapper.registerModule(new JavaTimeModule());
        String activityJson = objectMapper.writeValueAsString(activity);
        System.out.println(activityJson);

        mvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertIsBadRequest(Activity activity) throws Exception
    {
        organizer.setId(1L);
        objectMapper.registerModule(new JavaTimeModule());
        String activityJson = objectMapper.writeValueAsString(activity);
        System.out.println(activityJson);

        mvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void assertIsUnauthorized(Activity activity) throws Exception
    {
        organizer.setId(1L);
        objectMapper.registerModule(new JavaTimeModule());
        String activityJson = objectMapper.writeValueAsString(activity);
        System.out.println(activityJson);

        mvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private void assertIsForbidden(Activity activity) throws Exception
    {
        organizer.setId(1L);
        objectMapper.registerModule(new JavaTimeModule());
        String activityJson = objectMapper.writeValueAsString(activity);
        System.out.println(activityJson);

        mvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}


//TODO FutureDate has to be present or future OF STARTDATE
//TODO shouldnt be able to create activity with either applicants or participants in it
//TODO Alle anderen methods testen
//TODO authorization testen