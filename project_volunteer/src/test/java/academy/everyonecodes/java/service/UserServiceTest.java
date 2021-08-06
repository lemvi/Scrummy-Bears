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

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void saveTest(User input) {
        when(passwordEncoder.encode(input.getPassword()))
                .thenReturn("encrypted");
        when(repository.save(input))
                .thenReturn(input);
        assertThrows(ConstraintViolationException.class, () -> userService.save(input));
    }

    static Stream<Arguments> getParams() {
        return Stream.of(
                Arguments.of(
                        new User(
                                "",
                                "123456",
                                "first",
                                "second",
                                "user@email.com",
                                Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_INDIVIDUAL"))
                        )
                ));
    }
}

