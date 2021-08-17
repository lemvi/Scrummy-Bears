package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.DTOs.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
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
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "12345",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected200OK, actual);
    }

    @Test
    void saveIndividual_valid_without_optional_fields()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        "email@email.com",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected200OK, actual);
    }

    @Test
    void saveIndividual_USERNAME_empty__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


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
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


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
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


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
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


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
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2030, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_POSTALCODE__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_CITY__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_STREET__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_STREETNUMBER__too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_EMAIL_too_long__wrong_format()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "emailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemail@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


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
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "46456456456456456489789789798789646545645645645645646545646545646545645645645645645645645645646545645645645645645646",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_DESCRIPTION___too_long()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000, 1, 1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf" +
                                "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_invalid_empty_IndividualVolunteerDTO()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO();


        String actual = restTemplate.postForEntity("/register?individual", user, User.class)
                .getStatusCode().toString();
        assertEquals(expected400BadRequest, actual);
    }
}
