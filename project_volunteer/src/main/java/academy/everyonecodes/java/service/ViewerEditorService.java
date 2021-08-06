package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class ViewerEditorService {
    private final UserRepository userRepository;
    private final UserToUserDTOTranslator userToUserDTOTranslator;
    private final PasswordEncoder passwordEncoder;

    public ViewerEditorService(UserRepository userRepository, UserToUserDTOTranslator userToUserDTOTranslator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userToUserDTOTranslator = userToUserDTOTranslator;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDTO> getAccountInfo(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userToUserDTOTranslator::translateToDTO);
    }
    public Optional<UserDTO> editAccountInfo(String username, UserDTO userDTO, Principal principal) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (oUser.isPresent() && userDTO.getUsername().equals(username) &&username.equals(principal.getName())) {
            User userEdited = userToUserDTOTranslator.translateToUser(userDTO);
            User userDB = oUser.get();
            userEdited.setId(userDB.getId());
            passwordEncoder.encode(userEdited.getPassword());
            User user = userRepository.save(userEdited);
            return  Optional.of(userToUserDTOTranslator.translateToDTO(user));
        }
        return Optional.empty();
    }
    public UserDTO post(UserDTO userDTO) {
        return userToUserDTOTranslator.translateToDTO(userRepository.save(userToUserDTOTranslator.translateToUser(userDTO)));
    }
}
