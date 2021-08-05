package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ViewerEditorService {
    private final UserRepository userRepository;
    private final UserToUserDTOTranslator userToUserDTOTranslator;

    public ViewerEditorService(UserRepository userRepository, UserToUserDTOTranslator userToUserDTOTranslator) {
        this.userRepository = userRepository;
        this.userToUserDTOTranslator = userToUserDTOTranslator;
    }

    public Optional<UserDTO> getAccountInfo(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userToUserDTOTranslator::translateToDTO);
    }
    public Optional<UserDTO> editAccountInfo(String username, UserDTO userDTO) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (oUser.isPresent() && userDTO.getUsername().equals(username)) {
            User userEdited = userToUserDTOTranslator.translateToUser(userDTO);
            User userDB = oUser.get();
            userEdited.setId(userDB.getId());
            User user = userRepository.save(userEdited);
            return  Optional.of(userToUserDTOTranslator.translateToDTO(user));
        }
        return Optional.empty();
    }
    public UserDTO post(UserDTO userDTO) {
        return userToUserDTOTranslator.translateToDTO(userRepository.save(userToUserDTOTranslator.translateToUser(userDTO)));
    }
}
