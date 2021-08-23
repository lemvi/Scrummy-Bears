package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import org.springframework.stereotype.Service;

@Service
public class UserToIndividualVolunteerDTOTranslator
{

    public User translateToUser(IndividualVolunteerDTO individualVolunteerDTO) {
        return new User(individualVolunteerDTO.getUsername(),
                individualVolunteerDTO.getPassword(),
                individualVolunteerDTO.getFirstNamePerson(),
                individualVolunteerDTO.getLastNamePerson(),
                individualVolunteerDTO.getDateOfBirth(),
                individualVolunteerDTO.getPostalCode(),
                individualVolunteerDTO.getCity(),
                individualVolunteerDTO.getStreet(),
                individualVolunteerDTO.getStreetNumber(),
                individualVolunteerDTO.getEmailAddress(),
                individualVolunteerDTO.getTelephoneNumber(),
                individualVolunteerDTO.getDescription(),
                individualVolunteerDTO.getRoles());
    }
    public IndividualVolunteerDTO translateToDTO(User user) {
        return new IndividualVolunteerDTO(user.getUsername(),
                user.getPassword(),
                user.getFirstNamePerson(),
                user.getLastNamePerson(),
                user.getDateOfBirth(),
                user.getPostalCode(),
                user.getCity(),
                user.getStreet(),
                user.getStreetNumber(),
                user.getEmailAddress(),
                user.getTelephoneNumber(),
                user.getDescription(),
                user.getRoles());
    }
}
