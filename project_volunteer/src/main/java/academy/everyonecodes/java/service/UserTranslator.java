package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;

public class UserTranslator {

    public User translateToUser(UserDTO dto) {
        return new User(dto.getUsername(), dto.getPassword(), dto.getRoles(),dto.getFirstNamePerson(), dto.getLastNamePerson(), dto.getCompany(), dto.getDateOfBirth(), dto.getAddressPLZ(), dto.getAddressCity(), dto.getAddressStreet(), dto.getAddressStreetNumber(), dto.getEmailAddress(), dto.getTelephoneNumber(), dto.getDescription());
    }
    public UserDTO translateToDTO(User user) {
        return new UserDTO(user.getUsername(), user.getPassword(), user.getRoles(),user.getFirstNamePerson(), user.getLastNamePerson(), user.getCompany(), user.getDateOfBirth(), user.getAddressPLZ(), user.getAddressCity(), user.getAddressStreet(), user.getAddressStreetNumber(), user.getEmailAddress(), user.getTelephoneNumber(), user.getDescription());
    }
}
