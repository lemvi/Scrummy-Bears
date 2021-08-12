package academy.everyonecodes.java;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ViewerEditorEndpointTest {

    @Autowired
    TestRestTemplate template;

    @MockBean
    ViewerEditorService viewerEditorService;

    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    public void getAccountInfo() throws Exception {
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
        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.of(userdto));
        mvc.perform(get(url + input)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(viewerEditorService).getAccountInfo(input);
        // assertTrue("expected status code = 200 ; current status code = " + status, status == 200);
        // assertTrue("expected status code = 403 ; current status code = " + status, status == 403);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_DICTATOR"})
    public void getAccountInfo_ForbiddenAuthority() throws Exception {
        String input = "username";
        String url = "/account/";
        UserDTO userdto = new UserDTO("username", "pw", "firstName", "lastName", "company", LocalDate.now(), "postalCode", "city", "street", "streetnumber", "email@email.com", "phone", "description", Set.of(new Role(1L, "ROLE_INDIVIDUAL")));
        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.of(userdto));
        mvc.perform(get(url + input)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isForbidden());

        Mockito.verifyNoInteractions(viewerEditorService);
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

        String userDtoJson = createJson(userdto);
        System.out.println(userDtoJson);

        // TODO: IF TIME, TRY TO FIND A WAY THAT JACKSON UNDERSTANDS LOCALDATE, SO WE DO NOT NEED TO CREATE OUR OWN JSON OBJECT:
/*
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonBody = ow.writeValueAsString(userdto);
 */

         //String jsonBody = new Gson().toJson(userdto); //NO GSON NEEDED, IF WE USE JACKSON

        mvc.perform(put(url + input)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                        //.content(new ObjectMapper().writeValueAsString(userdto))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                //.andExpect(status().isBadRequest());
        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_DICTATOR")
    public void editAccountInfo_ForbiddenAuthority() throws Exception{
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

        String userDtoJson = createJson(userdto);
        System.out.println(userDtoJson);

        // TODO: IF TIME, TRY TO FIND A WAY THAT JACKSON UNDERSTANDS LOCALDATE, SO WE DO NOT NEED TO CREATE OUR OWN JSON OBJECT:
/*
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonBody = ow.writeValueAsString(userdto);
 */

        //String jsonBody = new Gson().toJson(userdto); //NO GSON NEEDED, IF WE USE JACKSON

        mvc.perform(put(url + input)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                        //.content(new ObjectMapper().writeValueAsString(userdto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        //.andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(viewerEditorService);
    }


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
