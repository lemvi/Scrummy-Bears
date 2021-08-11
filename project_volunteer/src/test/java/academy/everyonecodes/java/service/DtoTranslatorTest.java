package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoTranslatorTest
{
    @Test
    void individualVolunteerToUser()
    {
        String validUsername = "user";
        String validPw = "123456";
        String validFirst = "first";
        String validSecond = "second";
        LocalDate dateOfBirth = LocalDate.of(2020, 5, 5);
        String validEmail = "user@email.com";
        String postalCode = "postalCode";
        String city = "city";
        String street = "street";
        String streetNumber = "streetNumber";
        String telephoneNumber = "telephoneNumber";
        String description = "description";
        new IndividualVolunteerDTO(
                " user ",
                " 123456 ",
                " first ",
                " second ",
                dateOfBirth,
                " postalCode ",
                " city ",
                " street ",
                " streetNumber ",
                " user@email.com ",
                " telephoneNumber ",
                " description ",
                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
        );
        new User(
                validUsername,
                validPw,
                validFirst,
                validSecond,
                dateOfBirth,
                postalCode,
                city,
                street,
                streetNumber,
                validEmail,
                telephoneNumber,
                description,
                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
        );
    }

    @Test
    void companyToUser()
    {
    }
}