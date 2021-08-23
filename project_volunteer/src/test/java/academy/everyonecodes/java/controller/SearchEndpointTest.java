package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
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
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SearchEndpointTest {
    @Autowired
    TestRestTemplate template;

    @MockBean
    SearchService searchService;
    @Autowired
    private MockMvc mvc;
    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    public void search_Authorized() throws Exception {
        String input = "title";
        String url = "/search/";
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());

        List<Activity> expected = List.of(a1);
        Mockito.when(searchService.searchActivities(input)).thenReturn(expected);
        mvc.perform(get(url + input)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(searchService).searchActivities(input);
    }
    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_DICTATOR"})
    public void search_UnAuthorized() throws Exception {
        String input = "title";
        String url = "/search/";
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());

        List<Activity> expected = List.of(a1);
        Mockito.when(searchService.searchActivities(input)).thenReturn(expected);
        mvc.perform(get(url + input)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        Mockito.verifyNoInteractions(searchService);
    }
}
