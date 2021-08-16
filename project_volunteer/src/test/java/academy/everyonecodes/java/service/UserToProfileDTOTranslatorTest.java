package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ProfileDTO;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Year;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserToProfileDTOTranslatorTest
{
    @Autowired
    private UserToProfileDTOTranslator userToProfileDTOTranslator;

    @MockBean
    private AgeCalculator ageCalculator;

    @Test
    void toDTO_everything_existing()
    {
        ProfileDTO profileDTO = new ProfileDTO(
                "name",
                "First Last",
                "company",
               69,
                "description"
                );
        User user = new User(
                "name",
                "First",
                "Last",
                "company",
                LocalDate.of(2021, 8, 2),
                "description"
        );

        Mockito.when(ageCalculator.calculate(user))
                .thenReturn(69);

        var expected = profileDTO;
        var actual = userToProfileDTOTranslator.toDTO(user);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toDTO_no_company()
    {
        ProfileDTO profileDTO = new ProfileDTO(
                "name",
                "First Last",
                null,
                69,
                "description"
        );
        User user = new User(
                "name",
                "First",
                "Last",
                null,
                LocalDate.of(2021, 8, 2),
                "description"
        );

        Mockito.when(ageCalculator.calculate(user))
                .thenReturn(69);

        var expected = profileDTO;
        var actual = userToProfileDTOTranslator.toDTO(user);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toDTO_no_age()
    {
        ProfileDTO profileDTO = new ProfileDTO(
                "name",
                "First Last",
                "company",
                null,
                "description"
        );
        User user = new User(
                "name",
                "First",
                "Last",
                "company",
                null,
                "description"
        );

        Mockito.when(ageCalculator.calculate(user))
                .thenReturn(null);

        var expected = profileDTO;
        var actual = userToProfileDTOTranslator.toDTO(user);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toDTO_no_description()
    {
        ProfileDTO profileDTO = new ProfileDTO(
                "name",
                "First Last",
                "company",
                69,
                null
        );
        User user = new User(
                "name",
                "First",
                "Last",
                "company",
                LocalDate.of(2021, 8, 2),
                null
        );

        Mockito.when(ageCalculator.calculate(user))
                .thenReturn(69);

        var expected = profileDTO;
        var actual = userToProfileDTOTranslator.toDTO(user);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toDTO_only_mandatory()
    {
        ProfileDTO profileDTO = new ProfileDTO(
                "username",
                "First Last",
                "companyName",
                null,
                null
        );
        User user = new User(
                "username",
                "First",
                "Last",
                "companyName"
        );

        Mockito.when(ageCalculator.calculate(user))
                .thenReturn(null);

        var expected = profileDTO;
        var actual = userToProfileDTOTranslator.toDTO(user);
        Assertions.assertEquals(expected, actual);
    }
}