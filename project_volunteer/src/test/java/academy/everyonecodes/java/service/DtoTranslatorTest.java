package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.UserEntityBuilder;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTOBuilder;
import academy.everyonecodes.java.data.dtos.OrganizationDTO;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.OrganizationDTOBuilder;
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
        IndividualVolunteerDTO input = new IndividualVolunteerDTOBuilder().setUsername(" user ").setPassword(" 123456 ").setFirstNamePerson(" first ").setLastNamePerson(" second ").setDateOfBirth(dateOfBirth).setPostalCode(" postalCode ").setCity(" city ").setStreet(" street ").setStreetNumber(" streetNumber ").setEmailAddress(" user@email.com ").setTelephoneNumber(" telephoneNumber ").setDescription(" description ").setRoles(Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))).createIndividualVolunteerDTO();
        User expected = new UserEntityBuilder().setUsername("user").setPassword("123456").setFirstNamePerson("first").setLastNamePerson("second").setDateOfBirth(dateOfBirth).setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("streetNumber").setEmailAddress("user@email.com").setTelephoneNumber("telephoneNumber").setDescription("description").setRoles(Set.of(new Role("ROLE_INDIVIDUAL"), new Role("ROLE_VOLUNTEER"))).createUser();
        var actual = dtoTranslator.IndividualVolunteerToUser(input);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void organizationToUser()
    {
        OrganizationDTO organizationDTO = new OrganizationDTOBuilder().setUsername("  username  ").setPassword("   password  ").setOrganizationName("  organizationName  ").setPostalCode("  postalCode  ").setCity("  city  ").setStreet("  street  ").setStreetNumber("  streetnumber  ").setEmailAddress("  email@email.com  ").setTelephoneNumber("  telephone  ").setDescription("  description  ").setRoles(Set.of(new Role("ROLE_ORGANIZATION"))).createOrganizationDTO();

        var expected = new UserEntityBuilder().setUsername("username").setPassword("password").setOrganizationName("organizationName").setPostalCode("postalCode").setCity("city").setStreet("street").setStreetNumber("streetnumber").setEmailAddress("email@email.com").setTelephoneNumber("telephone").setDescription("description").setRoles(Set.of(new Role("ROLE_ORGANIZATION"))).createUser();
        var actual = dtoTranslator.OrganizationToUser(organizationDTO);

        Assertions.assertEquals(expected, actual);
    }
}