package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.CompanyDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DtoTranslatorTest
{
    @Autowired
    DtoTranslator dtoTranslator;

    @Test
    void individualVolunteerToUser()
    {

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