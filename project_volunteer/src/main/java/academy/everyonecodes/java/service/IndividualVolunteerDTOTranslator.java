package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import org.springframework.stereotype.Service;

@Service
public class IndividualVolunteerDTOTranslator {
    public User translateDTOtoUser(IndividualVolunteerDTO individualVolunteerDTO) {
        return new User(
                individualVolunteerDTO.getUsername().trim(),
                individualVolunteerDTO.getPassword().trim(),
                individualVolunteerDTO.getFirstNamePerson().trim(),
                individualVolunteerDTO.getLastNamePerson().trim(),
                individualVolunteerDTO.getDateOfBirth(),
                individualVolunteerDTO.getPostalCode().trim(),
                individualVolunteerDTO.getCity().trim(),
                individualVolunteerDTO.getStreet().trim(),
                individualVolunteerDTO.getStreetNumber().trim(),
                individualVolunteerDTO.getEmailAddress().trim(),
                individualVolunteerDTO.getTelephoneNumber().trim(),
                individualVolunteerDTO.getDescription().trim(),
                individualVolunteerDTO.getRoles()
        );
    }
}
