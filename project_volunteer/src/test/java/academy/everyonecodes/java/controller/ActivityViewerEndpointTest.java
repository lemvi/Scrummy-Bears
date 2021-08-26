package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Status;
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
    void getMyActivities_asVolunteer_invalidRole_individual() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verifyNoInteractions(activityViewerService);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_ORGANIZATION"})
    void getMyActivities_asVolunteer_invalidRole_organization() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verifyNoInteractions(activityViewerService);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_MAFIABOSS"})
    void getMyActivities_asVolunteer_invalidRole_roleNonExistent() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verifyNoInteractions(activityViewerService);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_pending_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/pending";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteer(username, Status.PENDING);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_active_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/active";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteer(username, Status.ACTIVE);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_completed_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/completed";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteer(username, Status.COMPLETED);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_applied_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/applied";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteer(username, Status.APPLIED);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_rejected_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/rejected";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteer(username, Status.REJECTED);
    }

}