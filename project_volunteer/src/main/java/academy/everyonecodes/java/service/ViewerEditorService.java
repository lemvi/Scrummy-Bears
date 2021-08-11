package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class ViewerEditorService {
    private final UserRepository userRepository;
    private final UserToIndividualVolunteerDTOTranslator userToIndividualVolunteerDTOTranslator;
    private final PasswordEncoder passwordEncoder;

    public ViewerEditorService(UserRepository userRepository, UserToIndividualVolunteerDTOTranslator userToIndividualVolunteerDTOTranslator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userToIndividualVolunteerDTOTranslator = userToIndividualVolunteerDTOTranslator;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<IndividualVolunteerDTO> getAccountInfo(String username, Principal principal) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && username.equals(principal.getName())) {
            return user.map(userToIndividualVolunteerDTOTranslator::translateToDTO);
        }
        return Optional.empty();
    }
    public Optional<IndividualVolunteerDTO> editAccountInfo(String username, IndividualVolunteerDTO individualVolunteerDTO, Principal principal) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (oUser.isPresent() && individualVolunteerDTO.getUsername().equals(username) && username.equals(principal.getName())) {
            User userEdited = userToIndividualVolunteerDTOTranslator.translateToUser(individualVolunteerDTO);
            User userDB = oUser.get();
            userEdited.setId(userDB.getId());
            userEdited.setPassword(passwordEncoder.encode(userEdited.getPassword()));
            User user = userRepository.save(userEdited);
            return  Optional.of(userToIndividualVolunteerDTOTranslator.translateToDTO(userEdited));
        }
        return Optional.empty();
    }
    public IndividualVolunteerDTO post(IndividualVolunteerDTO individualVolunteerDTO) {
        return userToIndividualVolunteerDTOTranslator.translateToDTO(userRepository.save(userToIndividualVolunteerDTOTranslator.translateToUser(individualVolunteerDTO)));
    }
}
