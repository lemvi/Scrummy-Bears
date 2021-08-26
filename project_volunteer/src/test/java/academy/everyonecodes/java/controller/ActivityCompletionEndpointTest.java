package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.service.ActivityCompletionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityCompletionEndpointTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private ActivityCompletionService activityCompletionService;

    Draft draft = new Draft(
            "title",
            "description",
            LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
            LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
            true,
            null);

    Activity activity = new Activity();

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void completeActivity_invalid_Rating_isLowerThanMin() throws Exception {
        Long activityId = 1L;
        Rating rating = new Rating(0);

        assert_completeActivity_IsBadRequest(activityId, rating);
        Mockito.verifyNoInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void completeActivity_invalid_Feedback_MoreThan800Characters() throws Exception {
        Long activityId = 1L;
        Rating rating = new Rating(5);
        rating.setFeedback("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies.");

        assert_completeActivity_IsBadRequest(activityId, rating);
        Mockito.verifyNoInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void completeActivity_invalid_Feedback_NotNull_LowerThan800() throws Exception {
        Long activityId = 1L;
        Rating rating = new Rating(5);
        rating.setFeedback("Lorem ipsum dolor sit amet, consectetibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies.");

        assert_completeActivity_IsOK(activityId, rating);
        Mockito.verify(activityCompletionService).completeActivity(activityId, rating);
        Mockito.verifyNoMoreInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void completeActivity_invalid_Rating_isHigherThanMax() throws Exception {
        Long activityId = 1L;
        Rating rating = new Rating(6);

        assert_completeActivity_IsBadRequest(activityId, rating);
        Mockito.verifyNoInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void completeActivity_invalid_Rating_isNull() throws Exception {
        Long activityId = 1L;
        Rating rating = null;

        assert_completeActivity_IsBadRequest(activityId, rating);
        Mockito.verifyNoInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void completeActivity_Authorized_Individual() throws Exception {
        Long activityId = 1L;
        Rating rating = new Rating(5);

        assert_completeActivity_IsOK(activityId, rating);
        Mockito.verify(activityCompletionService).completeActivity(activityId, rating);
        Mockito.verifyNoMoreInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void completeActivity_invalid_credentials_Volunteer() throws Exception {
        Long activityId = 1L;
        Rating rating = new Rating(5);

        assert_completeActivity_IsForbidden(activityId, rating);

        Mockito.verifyNoInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_ORGANIZATION"})
    void completeActivity_valid_credentials_Organization() throws Exception
    {
        Long activityId = 1L;
        Rating rating = new Rating(5);

        assert_completeActivity_IsOK(activityId, rating);
        Mockito.verify(activityCompletionService).completeActivity(activityId, rating);
        Mockito.verifyNoMoreInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL", "ROLE_VOLUNTEER"})
    void completeActivity_valid_credentials_Multiple() throws Exception
    {
        Long activityId = 1L;
        Rating rating = new Rating(5);

        assert_completeActivity_IsOK(activityId, rating);
        Mockito.verify(activityCompletionService).completeActivity(activityId, rating);
        Mockito.verifyNoMoreInteractions(activityCompletionService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_DICTATOR"})
    void completeActivity_invalid_credentials_NonExistingRole() throws Exception
    {
        Long activityId = 1L;
        Rating rating = new Rating(5);

        assert_completeActivity_IsForbidden(activityId, rating);
        Mockito.verifyNoInteractions(activityCompletionService);
    }


    private void assert_completeActivity_IsOK(Long activityId, Rating rating) throws Exception
    {
        mvc.perform(put("/activities/complete/" + activityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfRating(rating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assert_completeActivity_IsForbidden(Long activityId, Rating rating) throws Exception
    {
        mvc.perform(put("/activities/complete/" + activityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfRating(rating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assert_completeActivity_IsBadRequest(Long activityId, Rating rating) throws Exception
    {
        mvc.perform(put("/activities/complete/" + activityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfRating(rating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String getJsonOfRating(Rating rating) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(rating);
    }
}
