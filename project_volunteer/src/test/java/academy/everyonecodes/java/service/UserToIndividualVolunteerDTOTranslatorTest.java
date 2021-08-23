package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
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
public class UserToIndividualVolunteerDTOTranslatorTest
{
    @Autowired
    UserToIndividualVolunteerDTOTranslator userToIndividualVolunteerDTOTranslator;


    @ParameterizedTest
    @MethodSource("inputData")
    void userToDTO_test(User input, IndividualVolunteerDTO expected) {

        IndividualVolunteerDTO result = userToIndividualVolunteerDTOTranslator.translateToDTO(input);
        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> inputData() {
        return Stream.of(
                Arguments.of(
                        new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of()),
                        new IndividualVolunteerDTO("test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of())));
    }

    @ParameterizedTest
    @MethodSource("inputDataTwo")
    void DTOToUser_test(IndividualVolunteerDTO input, User expected) {

        User result = userToIndividualVolunteerDTOTranslator.translateToUser(input);
        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> inputDataTwo() {
        return Stream.of(
                Arguments.of(
                        new IndividualVolunteerDTO("test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of()),
                        new User("test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of())));
    }

}
