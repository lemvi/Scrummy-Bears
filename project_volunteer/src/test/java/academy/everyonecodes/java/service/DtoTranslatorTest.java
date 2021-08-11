package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.CompanyDTO;
import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DtoTranslatorTest
{
    @Autowired
    DtoTranslator dtoTranslator;

    @Test
    void individualVolunteerToUser()
    {
        LocalDate dateOfBirth = LocalDate.of(2020, 5, 5);
        IndividualVolunteerDTO input = new IndividualVolunteerDTO(
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
        User expected = new User(
                "user",
                "123456",
                "first",
                "second",
                dateOfBirth,
                "postalCode",
                "city",
                "street",
                "streetNumber",
                "user@email.com",
                "telephoneNumber",
                "description",
                Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))
        );
        var actual = dtoTranslator.IndividualVolunteerToUser(input);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void companyToUser()
    {
        CompanyDTO companyDTO = new CompanyDTO(
                "  username  ",
                "   password  ",
                "  companyName  ",
                "  postalCode  ",
                "  city  ",
                "  street  ",
                "  streetnumber  ",
                "  email@email.com  ",
                "  telephone  ",
                "  description  ",
                Set.of(new Role("ROLE_COMPANY"))
        );

        var expected = new User(
                "username",
                "password",
                "companyName",
                "postalCode",
                "city",
                "street",
                "streetnumber",
                "email@email.com",
                "telephone",
                "description",
                Set.of(new Role("ROLE_COMPANY"))
        );
        var actual = dtoTranslator.CompanyToUser(companyDTO);

        Assertions.assertEquals(expected, actual);
    }
}