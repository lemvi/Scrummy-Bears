package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.service.ActivityViewerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        String url = "/" + username + "/activities?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteer(username);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_INDIVIDUAL"})
    void getMyActivities_asVolunteer_invalidRole_individual() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verifyNoInteractions(activityViewerService);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_ORGANIZATION"})
    void getMyActivities_asVolunteer_invalidRole_organization() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verifyNoInteractions(activityViewerService);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_MAFIABOSS"})
    void getMyActivities_asVolunteer_invalidRole_roleNonExistent() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verifyNoInteractions(activityViewerService);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_pending_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/pending?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, Status.PENDING);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_active_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/active?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, Status.ACTIVE);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_completed_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/completed?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, Status.COMPLETED);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_applied_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/applied?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, Status.APPLIED);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asVolunteer_rejected_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/rejected?volunteer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificVolunteerFilteredByStatus(username, Status.REJECTED);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_INDIVIDUAL"})
    void getMyActivities_asIndividual_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificIndividualOrOrganization(username);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asIndividualOrganization_usingVolunteer_forbidden() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_MAFIABOSS"})
    void getMyActivities_asIndividualOrganization_usingMafiaBoss_forbidden() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_ORGANIZATION"})
    void getMyActivities_asOrganization_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificIndividualOrOrganization(username);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_INDIVIDUAL"})
    void getMyActivities_asIndividual_active_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/active?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificIndividualOrOrganizationFilteredByStatus(username, Status.ACTIVE);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_ORGANIZATION"})
    void getMyActivities_asOrganization_active_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/active?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificIndividualOrOrganizationFilteredByStatus(username, Status.ACTIVE);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void getMyActivities_asIndividualOrganization_usingVolunteer_filteredForActive_forbidden() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/active?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_MAFIABOSS"})
    void getMyActivities_asIndividualOrganization_usingMafiaBoss_filteredForActive_forbidden() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/active?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_INDIVIDUAL"})
    void getMyActivities_asIndividual_draft_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/draft?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificIndividualOrOrganizationFilteredByStatus(username, Status.DRAFT);
    }

    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_INDIVIDUAL"})
    void getMyActivities_asIndividual_completed_valid() throws Exception {
        String username = "username";
        String url = "/" + username + "/activities/completed?organizer";
        mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(activityViewerService).getListOfActivityViewDTOsForSpecificIndividualOrOrganizationFilteredByStatus(username, Status.COMPLETED);
    }


}