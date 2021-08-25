package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.service.StatusService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StatusEndpointTest
{
    @Autowired
    MockMvc mvc;

    @MockBean
    StatusService statusService;

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void acceptVolunteer_valid_credentials() throws Exception
    {
        mvc.perform(put("/activities/1/accept/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(statusService).acceptVolunteer(1L, 1L);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_WITCHER"})
    void acceptVolunteer_invalid_credentials() throws Exception
    {
        mvc.perform(put("/activities/1/accept/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verifyNoInteractions(statusService);
    }
}
