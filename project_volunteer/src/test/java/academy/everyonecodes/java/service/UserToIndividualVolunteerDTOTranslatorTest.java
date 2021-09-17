package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.UserEntityBuilder;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTOBuilder;
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
                        new UserEntityBuilder().setUsername("test").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setOrganizationName("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createUser(),
                        new IndividualVolunteerDTOBuilder().setUsername("test").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createIndividualVolunteerDTO()));
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
                        new IndividualVolunteerDTOBuilder().setUsername("test").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createIndividualVolunteerDTO(),
                        new UserEntityBuilder().setUsername("test").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createUser()));
    }

}
