package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.IndividualVolunteerDTO;
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
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository repository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @ParameterizedTest
    @MethodSource("getValidParams")
    void translateIndividualVolunteerDTOAndSaveUserTest_validRoles(IndividualVolunteerDTO input, User expected) {
        String password = input.getPassword();

        when(passwordEncoder.encode(password))
                .thenReturn("encrypted");
        when(repository.save(expected))
                .thenReturn(expected);

        User actual = userService.translateIndividualVolunteerDTOAndSaveUser(input);

        assertEquals(expected, actual);
        verify(passwordEncoder).encode(password);
        verify(repository).save(expected);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParams")
    void translateIndividualVolunteerDTOAndSaveUserTest_invalidRoles(IndividualVolunteerDTO input) {
        assertThrows(HttpStatusCodeException.class, () -> userService.translateIndividualVolunteerDTOAndSaveUser(input));
    }

    static Stream<Arguments> getValidParams() {
        String validUsername = "user";
        String validPw = "123456";
        String validEncryptedPw = "encrypted";
        String validFirst = "first";
        String validSecond = "second";
        String validEmail = "user@email.com";
        return Stream.of(
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_VOLUNTEER"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_VOLUNTEER"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
                        ),
                        new User(
                                validUsername,
                                validEncryptedPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
                        )
                )
        );
    }

    static Stream<Arguments> getInvalidParams() {
        String validUsername = "user";
        String validPw = "123456";
        String validEncryptedPw = "encrypted";
        String validFirst = "first";
        String validSecond = "second";
        String validEmail = "user@email.com";
        return Stream.of(
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_COMPANY"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_COMPANY"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_VOLUNTEER"), new Role("ROLE_COMPANY"))
                        )
                ),
                Arguments.of(
                        new IndividualVolunteerDTO(
                                validUsername,
                                validPw,
                                validFirst,
                                validSecond,
                                validEmail,
                                Set.of(new Role("ROLE_MAFIABOSS"))
                        )
                )
        );
    }
}

