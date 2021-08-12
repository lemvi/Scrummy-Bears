package academy.everyonecodes.java;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.service.AddSkillService;
import org.springframework.boot.test.context.SpringBootTest;

import academy.everyonecodes.java.service.ViewerEditorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
//import com.google.gson.Gson;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.accept;


    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @AutoConfigureMockMvc
    public class AddSkillEndpointTest {

        @Autowired
        TestRestTemplate template;

        @MockBean
        AddSkillService addSkillService;

        //@Autowired
        //WebApplicationContext context;

        @Autowired
        private MockMvc mvc;

    /*
    @Before("test")
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

     */


        @Test
        @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
        public void getAccountInfo() throws Exception {
            String username = "username";
            String url = "/account/";
            Skill skill = new Skill("skill");
            SkillDTO skillDTO = new SkillDTO("skill");

            UserDTO userdto = new UserDTO("test", "test","test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role(1L,"ROLE_INDIVIDUAL")));
            Mockito.when(addSkillService.addSkill(username, skillDTO)).thenReturn(Optional.of(skillDTO));
            mvc.perform(get(url + username)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            Mockito.verify(addSkillService).addSkill(username, skillDTO);
            // assertTrue("expected status code = 200 ; current status code = " + status, status == 200);
            // assertTrue("expected status code = 403 ; current status code = " + status, status == 403);
        }
}
