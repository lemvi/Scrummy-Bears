package academy.everyonecodes.java.controller;

import org.apache.tomcat.util.http.parser.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginEndpointTest
{
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "username", password = "password", authorities = "ROLE_VOLUNTEER")
    void getMessage_valid_output() throws Exception
    {
        String expected = "You were logged in";
        var actual = mockMvc.perform(get("/login")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expected, actual.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "username", password = "password", authorities = "ROLE_VOLUNTEER")
    void getMessage_invalid_output() throws Exception
    {
        String expected = "You were NOT logged in, MUAHHAHAAHAHAHHHAA!!! I WILL TAKE OVER THE WOOOOOORLD";
        var actual = mockMvc.perform(get("/login")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertNotEquals(expected, actual.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "username", password = "password", authorities = "ROLE_SON_OF_A_MOM")
    void getMessage_invalid_authorization() throws Exception
    {
        mockMvc.perform(get("/login")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
