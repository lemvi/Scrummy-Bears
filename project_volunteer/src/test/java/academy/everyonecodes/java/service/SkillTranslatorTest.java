package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.DTOs.SkillDTO;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SkillTranslatorTest {
    @Autowired
    SkillTranslator skillTranslator;


    @ParameterizedTest
    @MethodSource("inputDataThree")
    void skillToDTO_test(Skill input, SkillDTO expected) {

        SkillDTO result = skillTranslator.translateToSkillDTO(input);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> inputDataThree() {
        return Stream.of(Arguments.of(new Skill(new User(), "test"), new SkillDTO("test")));
    }

    @ParameterizedTest
    @MethodSource("inputDataFour")
    void dTOToSkill_test(Skill expected, SkillDTO input) {

        Skill result = skillTranslator.translateToSkill(input);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> inputDataFour() {
        return Stream.of(Arguments.of(new Skill("test"), new SkillDTO("test")));
    }
}
