package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.CompanyDTO;
import academy.everyonecodes.java.data.IndividualVolunteerDTO;
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

    protected User CompanyToUser(CompanyDTO companyDTO)
    {
        return new User(
                trimProperty(companyDTO.getUsername()),
                trimProperty(companyDTO.getPassword()),
                trimProperty(companyDTO.getCompanyName()),
                trimProperty(companyDTO.getPostalCode()),
                trimProperty(companyDTO.getCity()),
                trimProperty(companyDTO.getStreet()),
                trimProperty(companyDTO.getStreetNumber()),
                trimProperty(companyDTO.getEmailAddress()),
                trimProperty(companyDTO.getTelephoneNumber()),
                trimProperty(companyDTO.getDescription()),
                companyDTO.getRoles()
        );
    }

    private String trimProperty(String property)
    {
        if (property != null)
            property = property.trim();
        return property;
    }
}
