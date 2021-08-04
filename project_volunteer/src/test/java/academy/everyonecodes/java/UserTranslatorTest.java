package academy.everyonecodes.java;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.UserTranslator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserTranslatorTest {
    @Autowired
    UserTranslator userTranslator;


    @ParameterizedTest
    @MethodSource("inputData")
    void userToDTO_test(User input, UserDTO expected) {

        UserDTO result = userTranslator.translateToDTO(input);
        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> inputData() {
        return Stream.of(
                Arguments.of(
                        new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of()),
                        new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of())));
    }

    @ParameterizedTest
    @MethodSource("inputDataTwo")
    void DTOToUser_test(UserDTO input, User expected) {

        User result = userTranslator.translateToUser(input);
        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> inputDataTwo() {
        return Stream.of(
                Arguments.of(
                        new UserDTO("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of()),
                        new User("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of())));
    }

}
