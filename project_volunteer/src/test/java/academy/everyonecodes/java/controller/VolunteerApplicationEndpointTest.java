package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.service.VolunteerApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class VolunteerApplicationEndpointTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    VolunteerApplicationService volunteerApplicationService;

    @Value("${text800Chars}")
    String text800Chars;


    @Test
    @WithMockUser(username = "username", password = "pw", authorities = {"ROLE_VOLUNTEER"})
    void apply_credentials_valid() throws Exception {

        String url = "/activities/" + 1L + "/" + 1L;
        mvc.perform(post(url)
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(text800Chars)
                        .accept(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk());
        Mockito.verify(volunteerApplicationService).applyForActivityWithEmailAndText(1L, 1L, text800Chars);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    void apply_credentials_notValid() throws Exception {
        String url = "/activities/" + 1L + "/" + 1L;
        mvc.perform(post(url)
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(text800Chars)
                        .accept(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isForbidden());
        Mockito.verifyNoInteractions(volunteerApplicationService);
    }
}
