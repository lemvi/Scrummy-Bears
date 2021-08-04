package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserTranslator {

    public User translateToUser(UserDTO dto) {
        return new User(dto.getUsername(), dto.getPassword(),dto.getFirstNamePerson(), dto.getLastNamePerson(), dto.getCompanyName(), dto.getDateOfBirth(), dto.getPostalCode(), dto.getCity(), dto.getStreet(), dto.getStreetNumber(), dto.getEmailAddress(), dto.getTelephoneNumber(), dto.getDescription(), dto.getRoles());
    }
    public UserDTO translateToDTO(User user) {
        return new UserDTO(user.getUsername(), user.getPassword(),user.getFirstNamePerson(), user.getLastNamePerson(), user.getCompanyName(), user.getDateOfBirth(), user.getPostalCode(), user.getCity(), user.getStreet(), user.getStreetNumber(), user.getEmailAddress(), user.getTelephoneNumber(), user.getDescription(), user.getRoles());
    }
}
