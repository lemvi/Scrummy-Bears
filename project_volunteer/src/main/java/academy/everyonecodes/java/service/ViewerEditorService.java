package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String currentPrincipalName = getAuthenticatedName();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && username.equals(currentPrincipalName)) {
            return user.map(userToUserDTOTranslator::translateToDTO);
        }
        return Optional.empty();
    }

    public Optional<UserDTO> editAccountInfo(String username, UserDTO userDTO) {
        String currentPrincipalName = getAuthenticatedName();

        Optional<User> oUser = userRepository.findByUsername(username);
        if (oUser.isPresent() && userDTO.getUsername().equals(username) && username.equals(currentPrincipalName)) {
            User userEdited = userToUserDTOTranslator.translateToUser(userDTO);
            User userDB = oUser.get();
            userEdited.setId(userDB.getId());
            userEdited.setPassword(passwordEncoder.encode(userEdited.getPassword()));
            User user = userRepository.save(userEdited);
            return  Optional.of(userToUserDTOTranslator.translateToDTO(userEdited));
        }
        return Optional.empty();
    }

    public UserDTO post(UserDTO userDTO) {
        return userToUserDTOTranslator.translateToDTO(userRepository.save(userToUserDTOTranslator.translateToUser(userDTO)));
    }

    private String getAuthenticatedName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
