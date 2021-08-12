package academy.everyonecodes.java;

import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.SkillDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.UserAndSkillTranslator;
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
public class UserAndSkillTranslatorTest {
    @Autowired
    UserAndSkillTranslator userAndSkillTranslator;


    @ParameterizedTest
    @MethodSource("inputData")
    void userToDTO_test(User input, UserDTO expected) {

        UserDTO result = userAndSkillTranslator.translateToUserDTO(input);
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

        User result = userAndSkillTranslator.translateToUser(input);
        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> inputDataTwo() {
        return Stream.of(
                Arguments.of(
                        new UserDTO("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of()),
                        new User("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of())));
    }

    @ParameterizedTest
    @MethodSource("inputDataThree")
    void skillToDTO_test(Skill input,SkillDTO expected) {

        SkillDTO result = userAndSkillTranslator.translateToSkillDTO(input);
        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> inputDataThree() {
        return Stream.of(
                Arguments.of(
                        new Skill(new User(), "test"),
                        new SkillDTO("test"))
        );
    }

    @ParameterizedTest
    @MethodSource("inputDataFour")
    void dTOToSkill_test(Skill expected,SkillDTO input) {

        Skill result = userAndSkillTranslator.translateToSkill(input);
        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> inputDataFour() {
        return Stream.of(
                Arguments.of(
                        new Skill("test"),
                        new SkillDTO("test"))
        );
    }
}
