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
    private final UserAndSkillTranslator userAndSkillTranslator;
    private final PasswordEncoder passwordEncoder;

    public ViewerEditorService(UserRepository userRepository, UserAndSkillTranslator userAndSkillTranslator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAndSkillTranslator = userAndSkillTranslator;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDTO> getAccountInfo(String username, Principal principal) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && username.equals(principal.getName())) {
            return user.map(userAndSkillTranslator::translateToUserDTO);
        }
        return Optional.empty();
    }
    public Optional<UserDTO> editAccountInfo(String username, UserDTO userDTO, Principal principal) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (oUser.isPresent() && userDTO.getUsername().equals(username) && username.equals(principal.getName())) {
            User userEdited = userAndSkillTranslator.translateToUser(userDTO);
            User userDB = oUser.get();
            userEdited.setId(userDB.getId());
            userEdited.setPassword(passwordEncoder.encode(userEdited.getPassword()));
            User user = userRepository.save(userEdited);
            return  Optional.of(userAndSkillTranslator.translateToUserDTO(userEdited));
        }
        return Optional.empty();
    }
    public UserDTO post(UserDTO userDTO) {
        return userAndSkillTranslator.translateToUserDTO(userRepository.save(userAndSkillTranslator.translateToUser(userDTO)));
    }
}
