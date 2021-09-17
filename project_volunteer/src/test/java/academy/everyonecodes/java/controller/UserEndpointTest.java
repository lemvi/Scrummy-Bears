package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTOBuilder;
import academy.everyonecodes.java.data.dtos.OrganizationDTO;
import academy.everyonecodes.java.data.dtos.OrganizationDTOBuilder;
import academy.everyonecodes.java.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest
{

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    private final String expected400BadRequest = "400 BAD_REQUEST";
    private final String expected200OK = "200 OK";

    @Test
    void saveIndividual_valid_including_optional_fields()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username").setPassword("password").setFirstNamePerson("firstName").setLastNamePerson("lastName").setDateOfBirth(LocalDate.of(2000, 1, 1)).setPostalCode("12345").setCity("city").setStreet("street").setStreetNumber("streetnumber").setEmailAddress("email@email.com").setTelephoneNumber("0123456789").setDescription("description").setRoles(Set.of(new Role("ROLE_VOLUNTEER"))).createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected200OK, actual);
    }

    @Test
    void saveIndividual_valid_only_mandatory_fields()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder()
                .setUsername("username")
                .setPassword("password")
                .setFirstNamePerson("firstName")
                .setLastNamePerson("lastName")
                .setEmailAddress("email@email.com")
                .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected200OK, actual);
    }

    @Test
    void saveIndividual_USERNAME_empty__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);

        user.setUsername("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf");
        String actualTooLong = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualTooLong);
    }

    @Test
    void saveIndividual_PASSWORD_empty__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actualEmpty = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualEmpty);

        user.setPassword("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf");
        String actualTooLong = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualTooLong);
    }

    @Test
    void saveIndividual_FIRSTNAMEPERSON_empty__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actualEmpty = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualEmpty);

        user.setFirstNamePerson("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf");
        String actualTooLong = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualTooLong);
    }

    @Test
    void saveIndividual_LASTNAMEPERSON_empty__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actualEmpty = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualEmpty);

        user.setLastNamePerson("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf");
        String actualTooLong = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualTooLong);
    }

    @Test
    void saveIndividual_DATEOFBIRTH_date_in_future()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2030, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_POSTALCODE__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_CITY__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_STREET__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_STREETNUMBER__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_EMAIL_too_long__wrong_format()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("emailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemail@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actualEmpty = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualEmpty);

        user.setEmailAddress("email");
        String actualWrongFormat = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actualWrongFormat);
    }

    @Test
    void saveIndividual_TELEPHONENUMBER__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("46456456456456456489789789798789646545645645645645646545646545646545645645645645645645645645646545645645645645645646")
                                                                         .setDescription("description")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_DESCRIPTION___too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().setUsername("username")
                                                                         .setPassword("password")
                                                                         .setFirstNamePerson("firstName")
                                                                         .setLastNamePerson("lastName")
                                                                         .setDateOfBirth(LocalDate.of(2000, 1, 1))
                                                                         .setPostalCode("1111")
                                                                         .setCity("city")
                                                                         .setStreet("street")
                                                                         .setStreetNumber("streetnumber")
                                                                         .setEmailAddress("email@email.com")
                                                                         .setTelephoneNumber("0123456789")
                                                                         .setDescription("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                                                                                 "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                                                                                 "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                                                                                 "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                                                                                 "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                                                                                 "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                                                                                 "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                                                                                 "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf")
                                                                         .setRoles(Set.of(new Role("ROLE_VOLUNTEER")))
                                                                         .createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_invalid_empty_IndividualVolunteerDTO()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTOBuilder().createIndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveOrganization_ORGANIZATION_NAME_empty()
    {
        OrganizationDTO user = new OrganizationDTOBuilder().setUsername("username")
                                                           .setPassword("password")
                                                           .setOrganizationName("")
                                                           .setPostalCode("1111")
                                                           .setCity("city")
                                                           .setStreet("street")
                                                           .setStreetNumber("streetnumber")
                                                           .setEmailAddress("email@email.com")
                                                           .setTelephoneNumber("0123456789")
                                                           .setDescription("description")
                                                           .setRoles(Set.of(new Role("ROLE_ORGANIZATION")))
                                                           .createOrganizationDTO();


        String actual = restTemplate.postForEntity("/register?organization", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);

    }
}
