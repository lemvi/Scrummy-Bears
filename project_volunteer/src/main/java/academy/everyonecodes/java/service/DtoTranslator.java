package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.UserEntityBuilder;
import academy.everyonecodes.java.data.dtos.OrganizationDTO;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import org.springframework.stereotype.Service;

@Service
public class DtoTranslator
{
    protected User IndividualVolunteerToUser(IndividualVolunteerDTO individualVolunteerDTO)
    {
        return new UserEntityBuilder().setUsername(trimProperty(individualVolunteerDTO.getUsername())).setPassword(trimProperty(individualVolunteerDTO.getPassword())).setFirstNamePerson(trimProperty(individualVolunteerDTO.getFirstNamePerson())).setLastNamePerson(trimProperty(individualVolunteerDTO.getLastNamePerson())).setDateOfBirth(individualVolunteerDTO.getDateOfBirth()).setPostalCode(trimProperty(individualVolunteerDTO.getPostalCode())).setCity(trimProperty(individualVolunteerDTO.getCity())).setStreet(trimProperty(individualVolunteerDTO.getStreet())).setStreetNumber(trimProperty(individualVolunteerDTO.getStreetNumber())).setEmailAddress(trimProperty(individualVolunteerDTO.getEmailAddress())).setTelephoneNumber(trimProperty(individualVolunteerDTO.getTelephoneNumber())).setDescription(trimProperty(individualVolunteerDTO.getDescription())).setRoles(individualVolunteerDTO.getRoles()).createUser();
    }

    protected User OrganizationToUser(OrganizationDTO organizationDTO)
    {
        return new UserEntityBuilder().setUsername(trimProperty(organizationDTO.getUsername())).setPassword(trimProperty(organizationDTO.getPassword())).setOrganizationName(trimProperty(organizationDTO.getOrganizationName())).setPostalCode(trimProperty(organizationDTO.getPostalCode())).setCity(trimProperty(organizationDTO.getCity())).setStreet(trimProperty(organizationDTO.getStreet())).setStreetNumber(trimProperty(organizationDTO.getStreetNumber())).setEmailAddress(trimProperty(organizationDTO.getEmailAddress())).setTelephoneNumber(trimProperty(organizationDTO.getTelephoneNumber())).setDescription(trimProperty(organizationDTO.getDescription())).setRoles(organizationDTO.getRoles()).createUser();
    }

    private String trimProperty(String property)
    {
        if (property != null)
            property = property.trim();
        return property;
    }
}
