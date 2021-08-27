package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.service.RatingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RatingEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    private Rating rating;

    @BeforeEach
    void resetRating()
    {
        rating = new Rating(1, "feedback");
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void postRating_valid_authorities() throws Exception {
        mockMvc.perform(post("/activities/1/rate", Long.class)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfRating(rating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_MAFIABOSS"})
    void postRating_invalid_authorities() throws Exception {
        mockMvc.perform(post("/activities/1/rate", Long.class)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfRating(rating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void postRating_invalid_feedback_too_long() throws Exception {
        rating.setFeedback("Curabitur lectus libero, finibus quis luctus sit amet, blandit non nisi. " +
                "Nam lacinia nulla vitae dolor condimentum, lobortis porta massa consectetur. " +
                "Cras molestie felis at turpis vehicula tincidunt. Sed tincidunt volutpat metus, quis tempor neque efficitur et. " +
                "Morbi a venenatis lorem. Interdum et malesuada fames ac ante ipsum primis in faucibus. " +
                "Etiam placerat tortor ut lacus sollicitudin condimentum. Praesent rhoncus nulla eros, quis congue ante lobortis non. " +
                "Nunc sollicitudin, dolor sed luctus porttitor, nunc leo luctus dui, vel ornare felis ante dictum nibh. " +
                "Integer in dolor ac sapien venenatis pretium. Proin dictum mi eu libero posuere sagittis. " +
                "Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec faucibus mauris ut enim laoreet malesuada consequat et augue. " +
                "Integer tortor ex, pretium vitae ante vitae, tincidunt ultricies elit. In hac habitasse platea dictumst. Donec quis tincidunt augue.");
        mockMvc.perform(post("/activities/1/rate", Long.class)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfRating(rating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void postRating_invalid_rating_out_of_bounds_below() throws Exception {
        rating.setRatingValue(0);
        mockMvc.perform(post("/activities/1/rate", Long.class)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfRating(rating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void postRating_invalid_rating_out_of_bounds_above() throws Exception {
        rating.setRatingValue(6);
        mockMvc.perform(post("/activities/1/rate", Long.class)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfRating(rating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String getJsonOfRating(Rating rating) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(rating);
    }
}