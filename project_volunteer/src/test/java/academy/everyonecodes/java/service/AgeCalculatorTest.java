package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AgeCalculatorTest
{
    @Autowired
    private AgeCalculator ageCalculator;

    @MockBean
    private DateProvider dateProvider;

    @Test
    void calculate_age_existing()
    {
        Mockito.when(dateProvider.getNow())
                .thenReturn(LocalDate.of(2021, 8, 2));
        User user = new UserEntityBuilder().setDateOfBirth(LocalDate.of(1952, 8, 1)).createUser();

        var expected = 69;
        var actual = ageCalculator.calculate(user);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void calculate_age_not_existing()
    {
        Mockito.when(dateProvider.getNow())
                .thenReturn(LocalDate.of(2021, 8, 2));
        User user = new UserEntityBuilder().setUsername(null).setDateOfBirth(null).createUser();

        var actual = ageCalculator.calculate(user);
        Assertions.assertEquals(0, actual);
    }
}