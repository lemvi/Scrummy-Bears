package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private MethodValidationPostProcessor postProcessor;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @MockBean
    private UserRepository repository;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @ParameterizedTest
    @MethodSource("getParams")
    void saveTest(User input, User expected, int violationCount) {
        Set<ConstraintViolation<User>> violations = validator.validate(input);
        System.out.println(violations);
        String password = input.getPassword();
        when(passwordEncoder.encode(password)).thenReturn("encrypted");
        when(repository.save(input)).thenReturn(expected);

        User actual = userService.save(input);
        System.out.println(actual);

        assertEquals(expected, actual);
        assertEquals(violationCount, violations.size());

        verify(repository).save(input);
        verify(passwordEncoder).encode(password);
    }

    static Stream<Arguments> getParams() {
        Set<Role> rolesVolunteer = new HashSet<>();
        rolesVolunteer.add(new Role("ROLE_VOLUNTEER"));
        return Stream.of(
                Arguments.of( //1 valid user
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
                        ), null,
//                        new User(
//                        "username",
//                        "encrypted",
//                        "firstName",
//                        "lastName",
//                        LocalDate.of(2021, 2, 2),
//                        "1234",
//                        "city",
//                        "street",
//                        "streetNumber",
//                        "email@email.com",
//                        "telephone",
//                        "description",
//                        rolesVolunteer
//                        ),
                        0
                ),
                Arguments.of( //2 empty user
                        new User(), null, 5
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
                        ), null, 1
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
                                rolesVolunteer
                        ), null, 2
                ),
                Arguments.of( //5 DoB in future
                        new User(
                                "username",
                                "password",
                                "firstName",
                                "lastName",
                                LocalDate.of(2022, 2, 2),
                                "1234",
                                "city",
                                "street",
                                "streetNumber",
                                "email@email.com",
                                "telephone",
                                "description",
                                rolesVolunteer
                        ), null, 0
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
                        ), null, 1
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
                        new User(
                                "username",
                                "encrypted",
                                "firstName",
                                "lastName",
                                LocalDate.of(2021, 2, 2),
                                "1234",
                                "",
                                "street",
                                "streetNumber",
                                "email",
                                "telephone",
                                "description",
                                rolesVolunteer
                        ), 0
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
                        ),
                        new User(
                                "username",
                                "encrypted",
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
                        ), 2
                )
        );
    }

}

