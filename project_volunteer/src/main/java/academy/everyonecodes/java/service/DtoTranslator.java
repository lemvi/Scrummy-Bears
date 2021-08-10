package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import org.springframework.stereotype.Service;

@Service
public class IndividualVolunteerDTOTranslator {
    public User toUser(IndividualVolunteerDTO individualVolunteerDTO) {
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

    private String trimProperty(String property)
    {
        if (property != null)
            property = property.trim();
        return property;
    }
}
