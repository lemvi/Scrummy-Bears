package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityBuilder;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.DraftBuilder;
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

    Draft draft = new DraftBuilder().setTitle("title").setDescription("description").setStartDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setEndDateTime(LocalDateTime.of(LocalDate.of(2100, 1, 1), LocalTime.of(10, 10, 10))).setOpenEnd(true).setOrganizerUsername(null).createDraft();

    Activity activity = new ActivityBuilder().createActivity();

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void getAllActivities_valid_credentials() throws Exception
    {
        assertGetAllActivitiesIsOK();
        Mockito.verify(activityService).getAllActivities(false);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_DICTATOR"})
    void getAllActivities_invalid_credentials() throws Exception
    {
        assertGetAllActivitiesIsForbidden();
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void getActivitiesOfOrganizer_valid_credentials() throws Exception
    {
        assertGetActivitiesOfOrganizerIsOK();
        Mockito.verify(activityService).getActivitiesOfOrganizer("test");
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_DICTATOR"})
    void getActivitiesOfOrganizer_invalid_credentials() throws Exception
    {
        assertGetActivitiesOfOrganizerIsForbidden();
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void findActivityById_valid_credentials() throws Exception
    {
        assertFindActivityByIdIsOK();
        Mockito.verify(activityService).findActivityById(1L);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_DICTATOR"})
    void findActivityById_invalid_credentials() throws Exception
    {
        assertFindActivityByIdIsForbidden();
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void postActivity_valid_credentials() throws Exception
    {
        assertPostActivityIsOK(draft);
        Mockito.verify(activityService).postActivity(draft);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
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
    void editActivity_valid_credentials() throws Exception
    {
        assertEditActivityIsOK(activity);
        Mockito.verify(activityService).editActivity(activity);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void editActivity_invalid_credentials() throws Exception
    {
        assertEditActivityIsForbidden(activity);
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void deleteActivity_valid_credentials() throws Exception
    {
        assertDeleteActivityIsOK();
        Mockito.verify(activityService).deleteActivity(1L);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void deleteActivity_invalid_credentials() throws Exception
    {
        assertDeleteActivityIsForbidden();
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
    void findDraftById_valid_credentials() throws Exception
    {
        draft.setId(1L);
        Long draftId = draft.getId();
        assertFindDraftByIdIsOK(draftId);
        Mockito.verify(activityService).findDraftById(draftId);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_DICTATOR"})
    void findDraftById_invalid_credentials() throws Exception
    {
        draft.setId(1L);
        Long draftId = draft.getId();
        assertFindDraftByIdIsForbidden(draftId);
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
        assertSaveDraftAsActivityIsOK();
        Mockito.verify(activityService).saveDraftAsActivity(1L);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void saveDraftAsActivity_invalid_credentials() throws Exception
    {
        assertSaveDraftAsActivityIsForbidden();
        Mockito.verifyNoInteractions(activityService);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void deleteDraft_valid_credentials() throws Exception
    {
        assertDeleteDraftIsOK();
        Mockito.verify(activityService).deleteDraft(1L);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_VOLUNTEER"})
    void deleteDraft_invalid_credentials() throws Exception
    {
        assertDeleteDraftIsForbidden();
        Mockito.verifyNoInteractions(activityService);
    }



    private void assertGetAllActivitiesIsOK() throws Exception
    {
        mvc.perform(get("/activities", String.class)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertGetAllActivitiesIsForbidden() throws Exception
    {
        mvc.perform(get("/activities")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertGetActivitiesOfOrganizerIsOK() throws Exception
    {
        mvc.perform(get("/activities/test", String.class)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertGetActivitiesOfOrganizerIsForbidden() throws Exception
    {
        mvc.perform(get("/activities/test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertFindActivityByIdIsOK() throws Exception
    {
        mvc.perform(get("/activities/find/1", String.class)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertFindActivityByIdIsForbidden() throws Exception
    {
        mvc.perform(get("/activities/find/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    private void assertPostActivityIsOK(Draft draft) throws Exception
    {
        mvc.perform(post("/activities/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertPostActivityIsForbidden(Draft draft) throws Exception
    {
        mvc.perform(post("/activities/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertEditActivityIsOK(Activity activity) throws Exception
    {
        mvc.perform(put("/activities/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfActivity(activity))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertEditActivityIsForbidden(Activity activity) throws Exception
    {
        mvc.perform(put("/activities/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfActivity(activity))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertDeleteActivityIsOK() throws Exception
    {
        mvc.perform(delete("/activities/1/delete")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertDeleteActivityIsForbidden() throws Exception
    {
        mvc.perform(delete("/activities/1/delete")
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

    private void assertFindDraftByIdIsOK(Long draftId) throws Exception
    {
        mvc.perform(get("/drafts/find/" + draftId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertFindDraftByIdIsForbidden(Long draftId) throws Exception
    {
        mvc.perform(get("/drafts/find/" + draftId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertPostDraftIsOK(Draft draft) throws Exception
    {
        mvc.perform(post("/drafts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertPostDraftIsForbidden(Draft draft) throws Exception
    {
        mvc.perform(post("/drafts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertEditDraftIsOK(Draft draft) throws Exception
    {
        mvc.perform(put("/drafts/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertEditDraftIsForbidden(Draft draft) throws Exception
    {
        mvc.perform(put("/drafts/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonOfDraft(draft))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertSaveDraftAsActivityIsOK() throws Exception
    {
        mvc.perform(put("/drafts/1/save_as_activity")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertSaveDraftAsActivityIsForbidden() throws Exception
    {
        mvc.perform(put("/drafts/1/save_as_activity")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertDeleteDraftIsOK() throws Exception
    {
        mvc.perform(delete("/drafts/1/delete")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void assertDeleteDraftIsForbidden() throws Exception
    {
        mvc.perform(delete("/drafts/1/delete")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }



    private String getJsonOfDraft(Draft draft) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(draft);
    }

    private String getJsonOfActivity(Activity activity) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(activity);
    }
}