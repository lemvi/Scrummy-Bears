package academy.everyonecodes.java;


//TODO: Did not work, needs more research how to test a secured Endpoint & with Principal.

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
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
public class ViewerEditorEndpointTestBeta {

    @Autowired
    TestRestTemplate template;

    @MockBean
    ViewerEditorService viewerEditorService;

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
        String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test","test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role(1L, "ROLE_INDIVIDUAL")));
        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.of(userdto));
        mvc.perform(get(url + input)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(viewerEditorService).getAccountInfo(input);
        // assertTrue("expected status code = 200 ; current status code = " + status, status == 200);
        // assertTrue("expected status code = 403 ; current status code = " + status, status == 403);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_INDIVIDUAL")
    public void editAccountInfo() throws Exception{
        String input = "username";
        String url = "/account/";
        UserDTO userdto = new UserDTO(
                "username",
                "pw",
                "firstName",
                "lastName",
                "company",
                LocalDate.now(),
                "postalCode",
                "city",
                "street",
                "streetnumber",
                "email@email.com",
                "phone",
                "description",
                Set.of(new Role(1L, "ROLE_INDIVIDUAL")));
        //template.put(url + input, userdto);
        //ObjectMapper Obj = new ObjectMapper();
        //String jsonBody = Obj.writeValueAsString(userdto);

        String userDtoJson = createJson(userdto);
        System.out.println(userDtoJson);
/*
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonBody = ow.writeValueAsString(userdto);

 */

         //String jsonBody = new Gson().toJson(userdto);

        /*mvc.perform(put(url + input)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());*/

        ResultActions action = mvc.perform(put(url + input)
                        //.with(user("test").password("test").roles("INDIVIDUAL")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson));
        int status = action.andReturn().getResponse().getStatus();
        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
        assertTrue("expected status code = 200 ; current status code = " + status, status == 200);
       //assertTrue("expected status code = 404 ; current status code = " + status, status == 404);
    }

/*
        @Test
        @WithMockUser(username = "test", password = "test", authorities = "ROLE_INDIVIDUAL")
        void getAccountInfo_found_test() {
            String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test","test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        //Principal principal = Mockito.mock(Principal.class);
        //Mockito.when(principal.getName()).thenReturn(input);
        //Mockito.when(viewerEditorService.getAccountInfo(input, principal)).thenReturn(Optional.of(userdto));
        template.getForObject(url + input, UserDTO.class);
        //Mockito.verify(principal).getName();
        Mockito.verify(viewerEditorService).getAccountInfo(input, principal);


    }
    @Test
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_INDIVIDUAL")
    void getAccountInfo_notFound_test() {
        String input = "test";
        String url = "/account/";


        Mockito.when(viewerEditorService.getAccountInfo(input, principal)).thenReturn(Optional.empty());
        Mockito.when(principal.getName()).thenReturn(input);

        template.getForObject(url + input, UserDTO.class);
        Mockito.verify(viewerEditorService).getAccountInfo(input, principal);
        Mockito.verifyNoInteractions(principal.getName());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = {"INDIVIDUAL"})
    void editAccountInfo_found_test() {
        String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        //Mockito.when(principal.getName()).thenReturn(input);
        //Mockito.when(viewerEditorService.editAccountInfo(input, userdto, principal)).thenReturn(Optional.of(userdto));
        template.put(url + input, userdto);
        //Mockito.verify(principal).getName();
        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto, principal);
    }/*
    @Test
    void editAccountInfo_notFound_test() {
        String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test",Set.of());

        Mockito.when(viewerEditorService.editAccountInfo(input, userdto, principal)).thenReturn(Optional.empty());
        Mockito.when(principal.getName()).thenReturn(input);
        template.put(url + input, userdto);

        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto, principal);
    }
*/

    private String createJson(UserDTO userDto) {
        return "{" +
                "\"username\": \"" + userDto.getUsername() + "\"," +
                "\"password\": \"" + userDto.getPassword() + "\"," +
                "\"firstNamePerson\": \"" + userDto.getFirstNamePerson() + "\"," +
                "\"lastNamePerson\": \"" + userDto.getLastNamePerson() + "\"," +
                "\"companyName\": \"" + userDto.getCompanyName() + "\"," +
                "\"dateOfBirth\": \"" + userDto.getDateOfBirth() + "\"," +
                "\"postalCode\": \"" + userDto.getPostalCode() + "\"," +
                "\"city\": \"" + userDto.getCity() + "\"," +
                "\"street\": \"" + userDto.getStreet() + "\"," +
                "\"streetNumber\": \"" + userDto.getStreetNumber() + "\"," +
                "\"emailAddress\": \"" + userDto.getEmailAddress() + "\"," +
                "\"telephoneNumber\": \"" + userDto.getTelephoneNumber() + "\"," +
                "\"description\": \"" + userDto.getDescription() + "\"," +
                "\"roles\": " + createJsonPartForRoles(userDto.getRoles()) +
                "}";
    }

    private String createJsonPartForRoles(Set<Role> roles) {
        return "[" +
                roles.stream()
                        .map(this::createJsonPartForOneRole)
                        .collect( Collectors.joining( "," ))
                + "]";


    }

    private String createJsonPartForOneRole(Role role) {
        return "{" +
                "\"id\": " + role.getId() + "," +
                "\"role\": \"" + role.getRole() +
                "\"}";
    }

}
