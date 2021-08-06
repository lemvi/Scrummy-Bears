package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class AgeCalculator
{
    private final DateProvider dateProvider;

    public AgeCalculator(DateProvider dateProvider)
    {
        this.dateProvider = dateProvider;
    }

    public Integer calculate(User user)
    {
        LocalDate currentDate = dateProvider.getNow();
        LocalDate birthDate = user.getDateOfBirth();
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return null;
        }
    }
}
