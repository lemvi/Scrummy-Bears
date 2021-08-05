package academy.everyonecodes.java.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateProvider       // NO TESTING REQUIRED -> JUST A WORKAROUND CLASS TO MOCK LOCALDATE
{
    public LocalDate getNow()
    {
        return LocalDate.now();
    }
}
