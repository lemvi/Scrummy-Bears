package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

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
                        LocalDate.of(2000,1,1),
                        "12345",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
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
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actual);

        user.setUsername("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf");
        String actualTooLong = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actualEmpty = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actualEmpty);

        user.setPassword("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf");
        String actualTooLong = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actualEmpty = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actualEmpty);

        user.setFirstNamePerson("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf");
        String actualTooLong = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actualEmpty = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actualEmpty);

        user.setLastNamePerson("ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf");
        String actualTooLong = restTemplate.postForEntity("/register/individual", user, User.class)
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
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "ahfjkslhfsdjakflhasdjfahsdjkflhasdklfjhasdkjlfhasdkjlfhsdajklfhasdljkfhasdkjfhsdajkfhsdakjlfhasdjklfhasdfkjlashfkjlsdhfkjlsahfkjlashfjklashf",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "emailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemailemail@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actualEmpty = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actualEmpty);

        user.setEmailAddress("email");
        String actualWrongFormat = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "46456456456456456489789789798789646545645645645645646545646545646545645645645645645645645645646545645645645645645646",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
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
                        LocalDate.of(2000,1,1),
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
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_ROLES_volunteer()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "",
                        Set.of(new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected200OK, actual);
    }

    @Test
    void saveIndividual_ROLES_individual()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "",
                        Set.of(new Role("ROLE_INDIVIDUAL"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected200OK, actual);
    }

    @Test
    void saveIndividual_ROLES_individual_and_volunteer()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "",
                        Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected200OK, actual);
    }

    @Test
    void saveIndividual_ROLES_individual_volunteer_company()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"), new Role("ROLE_COMPANY"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_ROLES_company()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_COMPANY"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_ROLES_individual_company()
    //TODO war eine idee das umzusetzen, aber nach näherem überlegen...
    // brauchen wir die überprüfung der rollen überhaupt im endpoint? wir haben keine validation dafür und das sollte sich im service easy testen lassen,
    // vorallem weil ja dort die magic passiert und wirs hier nur mocken. also das hier ist eigentlich kein aussagekräftiger test für die rollen!
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))
                );

        User expectedUser = new User
                (
                        "username",
                        "encrypted",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_INDIVIDUAL"))
                );
//        user.setId(1L);
        when(userService.translateIndividualVolunteerDtoAndSaveUser(user))
                .thenReturn(expectedUser);
         
        var actual = restTemplate.postForEntity("/register/individual", user, User.class);
        assertEquals(expected200OK, actual.getStatusCode().toString());

        User actualUser = restTemplate.postForObject("/register/individual", user, User.class);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void saveIndividual_ROLES_volunteer_company()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_COMPANY"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_ROLES_invalid()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO
                (
                        "username",
                        "password",
                        "firstName",
                        "lastName",
                        LocalDate.of(2000,1,1),
                        "1111",
                        "city",
                        "street",
                        "streetnumber",
                        "email@email.com",
                        "0123456789",
                        "description",
                        Set.of(new Role("ROLE_ADMIN"))
                );
//        user.setId(1L);
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actual);
    }

    @Test
    void saveIndividual_invalid_empty_IndividualVolunteerDTO()
    {
        IndividualVolunteerDTO user = new IndividualVolunteerDTO();
//        when(userService.save(user))
//                .thenReturn(user);
         
        String actual = restTemplate.postForEntity("/register/individual", user, User.class)
                .getStatusCode().toString();
         assertEquals(expected400BadRequest, actual);
    }
}
