package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SearchEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SearchService searchService;

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    public void search_Authorized() throws Exception {
        String searchKeyword = "title";
        String organization = "organization";
        String startDate = "2020-08-08";
        String endDate = "2022-08-08";
        int ratingMin = 1;
        int ratingMax = 5;

        String url = "/search/activities/title?username=organization&startDate=2020-08-08&endDate=2022-08-08&ratingMin=1&ratingMax=5";
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());

        List<Activity> expected = List.of(a1);
        Mockito.when(searchService.searchActivities(searchKeyword, Optional.of(organization), Optional.of(startDate), Optional.of(endDate), Optional.of(ratingMin), Optional.of(ratingMax))).thenReturn(expected);
        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(searchService).searchActivities(searchKeyword, Optional.of(organization), Optional.of(startDate), Optional.of(endDate), Optional.of(ratingMin), Optional.of(ratingMax));
    }
    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_DICTATOR"})
    public void search_UnAuthorized() throws Exception {
        String searchKeyword = "title";
        String organization = "organization";
        String startDate = "2020-08-08";
        String endDate = "2022-08-08";
        int ratingMin = 1;
        int ratingMax = 5;
        String url = "/search/activities/title?username=organization&startDate=2020-08-08&endDate=2022-08-08&ratingMin=1&ratingMax=5";
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());

        List<Activity> expected = List.of(a1);
        Mockito.when(searchService.searchActivities(searchKeyword, Optional.of(organization), Optional.of(startDate), Optional.of(endDate), Optional.of(ratingMin), Optional.of(ratingMax))).thenReturn(expected);
        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        Mockito.verifyNoInteractions(searchService);
    }
    //---------------SEARCH VOLUNTEER TESTS-----------------------------------------------------
    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    public void searchVolunteer_Authorized() throws Exception {

        String searchKeyword = "title";
        String postalCode = "123";
        int ratingMin = 1;
        int ratingMax = 5;
        String url = "/search/volunteers/title?postalCode=123&ratingMin=1&ratingMax=5";

        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(searchService).searchVolunteers(searchKeyword, Optional.of(postalCode), Optional.of(ratingMin), Optional.of(ratingMax));
        Mockito.verifyNoMoreInteractions(searchService);
    }
    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    public void searchVolunteer_UnAuthorized() throws Exception {

        String url = "/search/volunteers/title?postalCode=postalCode&ratingMin=1&ratingMax=5";

        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        Mockito.verifyNoInteractions(searchService);
    }

}
