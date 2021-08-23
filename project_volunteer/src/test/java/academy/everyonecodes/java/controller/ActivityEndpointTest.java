package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.service.ActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ActivityEndpointTest
{
    @Autowired
    MockMvc mvc;

    @MockBean
    private ActivityService activityService;

    Draft draft = new Draft(
            "title",
            "description",
            LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
            LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10)),
            true,
            null);

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_valid_credentials() throws Exception
    {
        assertPostActivityIsOK(draft);
        Mockito.verify(activityService).postActivity(draft);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL", "ROLE_COMPANY"})
    void postActivity_valid_credentials_two_roles() throws Exception
    {
        assertPostActivityIsOK(draft);
        Mockito.verify(activityService).postActivity(draft);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL", "ROLE_VOLUNTEER"})
    void postActivity_valid_credentials_one_role_valid_one_role_invalid() throws Exception
    {
        assertPostActivityIsOK(draft);
        Mockito.verify(activityService).postActivity(draft);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void postActivity_invalid_credentials() throws Exception
    {
        assertPostActivityIsForbidden(draft);
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void getActivitiesOfOrganizer_valid_credentials() throws Exception
    {
        assertGetActivitiesOfOrganizerIsOK();
        Mockito.verify(activityService).getActivitiesOfOrganizer();
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void getActivitiesOfOrganizer_invalid_credentials() throws Exception
    {
        assertGetActivitiesOfOrganizerIsForbidden();
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postDraft_valid_credentials() throws Exception
    {
        assertPostDraftIsOK(draft);
        Mockito.verify(activityService).postDraft(draft);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void postDraft_invalid_credentials() throws Exception
    {
        assertPostDraftIsForbidden(draft);
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void getDraftsOfOrganizer_valid_credentials() throws Exception
    {
        assertGetDraftsOfOrganizerIsOK();
        Mockito.verify(activityService).getDraftsOfOrganizer();
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void getDraftsOfOrganizer_invalid_credentials() throws Exception
    {
        assertGetDraftsOfOrganizerIsForbidden();
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void editDraft_valid_credentials() throws Exception
    {
        assertEditDraftIsOK(draft);
        Mockito.verify(activityService).editDraft(draft);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void editDraft_invalid_credentials() throws Exception
    {
        assertEditDraftIsForbidden(draft);
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void saveDraftAsActivity_valid_credentials() throws Exception
    {
        assertSaveDraftAsActivityIsOK(1L);
        Mockito.verify(activityService).saveDraftAsActivity(1L);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void saveDraftAsActivity_invalid_credentials() throws Exception
    {
        assertSaveDraftAsActivityIsForbidden(1L);
        Mockito.verifyNoInteractions(activityService);
    }

    private void assertPostActivityIsOK(Draft draft) throws Exception
    {
        mvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertPostActivityIsForbidden(Draft draft) throws Exception
    {
        mvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertGetActivitiesOfOrganizerIsOK() throws Exception
    {
        mvc.perform(get("/activities")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertGetActivitiesOfOrganizerIsForbidden() throws Exception
    {
        mvc.perform(get("/activities")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertPostDraftIsOK(Draft draft) throws Exception
    {
        mvc.perform(post("/drafts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertPostDraftIsForbidden(Draft draft) throws Exception
    {
        mvc.perform(post("/drafts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertGetDraftsOfOrganizerIsOK() throws Exception
    {
        mvc.perform(get("/drafts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertGetDraftsOfOrganizerIsForbidden() throws Exception
    {
        mvc.perform(get("/drafts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertEditDraftIsOK(Draft draft) throws Exception
    {
        mvc.perform(put("/drafts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertEditDraftIsForbidden(Draft draft) throws Exception
    {
        mvc.perform(put("/drafts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertSaveDraftAsActivityIsOK(Long draftId) throws Exception
    {
        mvc.perform(put("/drafts/" + draftId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertSaveDraftAsActivityIsForbidden(Long draftId) throws Exception
    {
        mvc.perform(put("/drafts/" + draftId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private String getJsonOfDraft(Draft draft) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(draft);
    }
}