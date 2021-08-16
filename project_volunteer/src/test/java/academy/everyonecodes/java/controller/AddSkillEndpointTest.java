package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.service.AddSkillService;
import academy.everyonecodes.java.service.SkillTranslator;
import org.springframework.boot.test.context.SpringBootTest;

//import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AddSkillEndpointTest {

    @Autowired
    TestRestTemplate template;

    @MockBean
    AddSkillService addSkillService;
    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private Authentication auth;
    @MockBean
    SkillTranslator translator;



    @Test
    @WithMockUser(username = "username", password = "test", authorities = {"ROLE_VOLUNTEER"})
    public void getSkill_Authorized() throws Exception {
        String username = "username";
        Long id = 1L;
        String url = "/addSkill/";
        SkillDTO skillDTO = new SkillDTO("skill");
        String skillDtoJson = createJson(skillDTO);
        Role role = new Role("ROLE_INDIVIDUAL");
        role.setId(id);
        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(role));

        Mockito.when(addSkillService.addSkill(username, skillDTO)).thenReturn(Optional.of(skillDTO));
        mvc.perform(post(url + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(skillDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(addSkillService).addSkill(username, skillDTO);
    }

    @Test
    @WithMockUser(username = "username", password = "test", authorities = {"ROLE_DICTATOR"})
    public void getSkill_Unauthorized() throws Exception {
        String username = "username";
        Long id = 1L;
        String url = "/addSkill/";
        SkillDTO skillDTO = new SkillDTO("skill");
        String skillDtoJson = createJson(skillDTO);
        Role role = new Role("ROLE_INDIVIDUAL");
        role.setId(id);
        User user = new User("username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(role));

        Mockito.when(addSkillService.addSkill(username, skillDTO)).thenReturn(Optional.of(skillDTO));
        mvc.perform(post(url + username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(skillDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        Mockito.verifyNoInteractions(addSkillService);
    }
    private String createJson(SkillDTO skilLDTO) {
        return "{" +
                "\"skill\": \"" + skilLDTO.getSkill() + "\"" + "}";
    }

}
