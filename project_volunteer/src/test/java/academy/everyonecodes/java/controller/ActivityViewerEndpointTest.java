package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.service.ActivityViewerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ActivityViewerEndpointTest {

//    @Autowired
//    private TestRestTemplate restTemplate;                              // Probably don't need this

    @MockBean
    private ActivityViewerService activityViewerService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteer(username);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_INDIVIDUAL"})
    void getMyActivities_asVolunteer_invalidRole() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verifyNoInteractions(activityViewerService);
    }
}