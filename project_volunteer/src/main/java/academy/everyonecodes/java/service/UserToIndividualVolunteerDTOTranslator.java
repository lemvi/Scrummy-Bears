package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.UserEntityBuilder;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTOBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserToIndividualVolunteerDTOTranslator
{

    public User translateToUser(IndividualVolunteerDTO individualVolunteerDTO) {
        return new UserEntityBuilder().setUsername(individualVolunteerDTO.getUsername()).setPassword(individualVolunteerDTO.getPassword()).setFirstNamePerson(individualVolunteerDTO.getFirstNamePerson()).setLastNamePerson(individualVolunteerDTO.getLastNamePerson()).setDateOfBirth(individualVolunteerDTO.getDateOfBirth()).setPostalCode(individualVolunteerDTO.getPostalCode()).setCity(individualVolunteerDTO.getCity()).setStreet(individualVolunteerDTO.getStreet()).setStreetNumber(individualVolunteerDTO.getStreetNumber()).setEmailAddress(individualVolunteerDTO.getEmailAddress()).setTelephoneNumber(individualVolunteerDTO.getTelephoneNumber()).setDescription(individualVolunteerDTO.getDescription()).setRoles(individualVolunteerDTO.getRoles()).createUser();
    }
    public IndividualVolunteerDTO translateToDTO(User user) {
        return new IndividualVolunteerDTOBuilder().setUsername(user.getUsername()).setPassword(user.getPassword()).setFirstNamePerson(user.getFirstNamePerson()).setLastNamePerson(user.getLastNamePerson()).setDateOfBirth(user.getDateOfBirth()).setPostalCode(user.getPostalCode()).setCity(user.getCity()).setStreet(user.getStreet()).setStreetNumber(user.getStreetNumber()).setEmailAddress(user.getEmailAddress()).setTelephoneNumber(user.getTelephoneNumber()).setDescription(user.getDescription()).setRoles(user.getRoles()).createIndividualVolunteerDTO();
    }
}
