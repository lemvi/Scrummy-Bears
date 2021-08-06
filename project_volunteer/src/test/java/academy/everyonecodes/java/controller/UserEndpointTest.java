package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @MockBean
    private UserService userService;

    @ParameterizedTest
    @MethodSource("getParams")
    void saveTest(User input, String expectedStatusCode) {
        //Set<ConstraintViolation<User>> violations = validator.validate(input);
        //System.out.println(violations);
        //if (violations.isEmpty())
        when(userService.save(input)).thenReturn(input);

        var statusCode = restTemplate.postForEntity("/register", input, User.class).getStatusCode().toString();

        System.out.println(statusCode);
        //String expectedStatusCode = "400 BAD_REQUEST";
        Assertions.assertThrows(ConstraintViolationException.class, () -> userService.save(input));
        System.out.println("actual: " + statusCode + "\n expected: " + expectedStatusCode);
        //assertEquals(violationCount, violations.size());
/*
        if (expectedStatusCode) {
            verify(userService).save(input);
        }
        else
            verifyNoInteractions(userService);
*/
    }

    static Stream<Arguments> getParams() {
        //Set<Role> rolesVolunteer = new HashSet<>();
        //rolesVolunteer.add(new Role("ROLE_VOLUNTEER"));
        return Stream.of(
                Arguments.of( //1 valid user
                        new User(
                                "TheLegend29",
                                "123456",
                                "first",
                                "second",
                                "user@email.com",
                                Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"))
                        ),
                        "200 OK"
                ));/*,
                Arguments.of( //2 empty user
                        new User(), false, 6
                ),
                Arguments.of( //3 username null
                        new User(
                                null,
                                "password",
                                "firstName",
                                "lastName",
                                LocalDate.of(2021, 2, 2),
                                "1234",
                                "city",
                                "street",
                                "streetNumber",
                                "email@email.com",
                                "telephone",
                                "description",
                                rolesVolunteer
                        ), "400 BAD_REQUEST", 1
                ),
                Arguments.of( //4 no first name
                        new User(
                                "username",
                                "password",
                                "",
                                "lastName",
                                LocalDate.of(2021, 2, 2),
                                "1234",
                                "city",
                                "street",
                                "streetNumber",
                                "email@email.com",
                                "telephone",
                                "description",
                                null
                        ), "400 BAD_REQUEST", 3
                ),
                Arguments.of( //5 DoB in future
                        new User(
                                "username",
                                "password",
                                "firstName",
                                "lastName",
                                LocalDate.of(2021, 2, 2),
                                "1234",
                                "city",
                                "street",
                                "streetNumber",
                                "email@email.com",
                                "telephone",
                                "description",
                                rolesVolunteer
                        ),
                        "200 OK", 0
                ),
                Arguments.of( //6 postal Code size too big
                        new User(
                                "User",
                                "password",
                                "firstName",
                                "lastName",
                                LocalDate.of(2021, 2, 2),
                                "1234567891011",
                                "city",
                                "street",
                                "streetNumber",
                                "email@email.com",
                                "telephone",
                                "description",
                                rolesVolunteer
                        ), "400 BAD_REQUEST", 1
                ), //7 no city, but still street
                Arguments.of(
                        new User(
                                "username",
                                "password",
                                "firstName",
                                "lastName",
                                LocalDate.of(2021, 2, 2),
                                "1234",
                                "",
                                "street",
                                "streetNumber",
                                "email@email.com",
                                "telephone",
                                "description",
                                rolesVolunteer
                        ), // ERROR -> frontend?
                        "200 OK", 0
                ),
                Arguments.of(//8 email wrong format
                        new User(
                                "username",
                                "password",
                                "firstName",
                                "lastName",
                                LocalDate.of(2021, 2, 2),
                                "1234",
                                "city",
                                "street",
                                "streetNumber",
                                "email",
                                "telephone",
                                "description",
                                rolesVolunteer
                        ), "400 BAD_REQUEST", 2
                )
        );*/
    }
}
