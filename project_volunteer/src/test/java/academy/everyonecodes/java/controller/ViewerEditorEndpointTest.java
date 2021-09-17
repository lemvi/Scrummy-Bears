package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTOBuilder;
import academy.everyonecodes.java.service.ViewerEditorService;
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
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ViewerEditorEndpointTest {

    @MockBean
    ViewerEditorService viewerEditorService;

    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUser(username = "test", password = "test", authorities = {"ROLE_INDIVIDUAL"})
    public void getAccountInfo() throws Exception {
        String input = "username";
        String url = "/account/";
        IndividualVolunteerDTO userdto = new IndividualVolunteerDTOBuilder().setUsername("username").setPassword("pw").setFirstNamePerson("firstName").setLastNamePerson("lastName").setDateOfBirth(LocalDate.now()).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("streetnumber").setEmailAddress("email@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of(new Role(1L, "ROLE_INDIVIDUAL"))).createIndividualVolunteerDTO();
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
        IndividualVolunteerDTO userdto = new IndividualVolunteerDTOBuilder().setUsername("username").setPassword("pw").setFirstNamePerson("firstName").setLastNamePerson("lastName").setDateOfBirth(LocalDate.now()).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("streetnumber").setEmailAddress("email@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of(new Role(1L, "ROLE_INDIVIDUAL"))).createIndividualVolunteerDTO();
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
        IndividualVolunteerDTO userdto = new IndividualVolunteerDTOBuilder().setUsername("username").setPassword("pw").setFirstNamePerson("firstName").setLastNamePerson("lastName").setDateOfBirth(LocalDate.now()).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("streetnumber").setEmailAddress("email@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of(new Role(1L, "ROLE_INDIVIDUAL"))).createIndividualVolunteerDTO();

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
        IndividualVolunteerDTO userdto = new IndividualVolunteerDTOBuilder().setUsername("username").setPassword("pw").setFirstNamePerson("firstName").setLastNamePerson("lastName").setDateOfBirth(LocalDate.now()).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("streetnumber").setEmailAddress("email@email.com").setTelephoneNumber("phone").setDescription("description").setRoles(Set.of(new Role(1L, "ROLE_INDIVIDUAL"))).createIndividualVolunteerDTO();

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


    private String createJson(IndividualVolunteerDTO individualVolunteerDTO) {
        return "{" +
                "\"username\": \"" + individualVolunteerDTO.getUsername() + "\"," +
                "\"password\": \"" + individualVolunteerDTO.getPassword() + "\"," +
                "\"firstNamePerson\": \"" + individualVolunteerDTO.getFirstNamePerson() + "\"," +
                "\"lastNamePerson\": \"" + individualVolunteerDTO.getLastNamePerson() + "\"," +
                "\"dateOfBirth\": \"" + individualVolunteerDTO.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"," +
                "\"postalCode\": \"" + individualVolunteerDTO.getPostalCode() + "\"," +
                "\"city\": \"" + individualVolunteerDTO.getCity() + "\"," +
                "\"street\": \"" + individualVolunteerDTO.getStreet() + "\"," +
                "\"streetNumber\": \"" + individualVolunteerDTO.getStreetNumber() + "\"," +
                "\"emailAddress\": \"" + individualVolunteerDTO.getEmailAddress() + "\"," +
                "\"telephoneNumber\": \"" + individualVolunteerDTO.getTelephoneNumber() + "\"," +
                "\"description\": \"" + individualVolunteerDTO.getDescription() + "\"," +
                "\"roles\": " + createJsonPartForRoles(individualVolunteerDTO.getRoles()) +
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
                "\"role\": \"" + role.getRole() + "\"}";
    }

}
