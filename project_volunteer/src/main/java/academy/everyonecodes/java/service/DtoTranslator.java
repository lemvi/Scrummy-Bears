package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.dtos.OrganizationDTO;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import org.springframework.stereotype.Service;

@Service
public class DtoTranslator
{
    protected User IndividualVolunteerToUser(IndividualVolunteerDTO individualVolunteerDTO)
    {
        return new User(
                trimProperty(individualVolunteerDTO.getUsername()),
                trimProperty(individualVolunteerDTO.getPassword()),
                trimProperty(individualVolunteerDTO.getFirstNamePerson()),
                trimProperty(individualVolunteerDTO.getLastNamePerson()),
                individualVolunteerDTO.getDateOfBirth(),
                trimProperty(individualVolunteerDTO.getPostalCode()),
                trimProperty(individualVolunteerDTO.getCity()),
                trimProperty(individualVolunteerDTO.getStreet()),
                trimProperty(individualVolunteerDTO.getStreetNumber()),
                trimProperty(individualVolunteerDTO.getEmailAddress()),
                trimProperty(individualVolunteerDTO.getTelephoneNumber()),
                trimProperty(individualVolunteerDTO.getDescription()),
                individualVolunteerDTO.getRoles()
        );
    }

    protected User OrganizationToUser(OrganizationDTO organizationDTO)
    {
        return new User(
                trimProperty(organizationDTO.getUsername()),
                trimProperty(organizationDTO.getPassword()),
                trimProperty(organizationDTO.getOrganizationName()),
                trimProperty(organizationDTO.getPostalCode()),
                trimProperty(organizationDTO.getCity()),
                trimProperty(organizationDTO.getStreet()),
                trimProperty(organizationDTO.getStreetNumber()),
                trimProperty(organizationDTO.getEmailAddress()),
                trimProperty(organizationDTO.getTelephoneNumber()),
                trimProperty(organizationDTO.getDescription()),
                organizationDTO.getRoles()
        );
    }

    private String trimProperty(String property)
    {
        if (property != null)
            property = property.trim();
        return property;
    }
}
