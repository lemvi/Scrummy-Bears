package academy.everyonecodes.java.Service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.UserRepository;
import academy.everyonecodes.java.service.UserTranslator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ViewerEditorService {
    private final UserRepository userRepository;
    private final UserTranslator userTranslator;

    public ViewerEditorService(UserRepository userRepository, UserTranslator userTranslator) {
        this.userRepository = userRepository;
        this.userTranslator = userTranslator;
    }

    public Optional<UserDTO> getAccountInfo(UserDTO userDTO) {
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
        return user.map(userTranslator::translateToDTO);
    }
    public Optional<UserDTO> editAccountInfo(UserDTO userDTO) {
        Optional<User> oUser = userRepository.findByUsername(userDTO.getUsername());
        User userEdited = userTranslator.translateToUser(userDTO);
        if (oUser.isPresent()) {
            User user = userRepository.save(userEdited);
            return Optional.of(userTranslator.translateToDTO(user));
        }
        return Optional.empty();
    }
}
