package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.DTOs.SkillDTO;
import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.SkillRepository;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SkillService
{
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final SkillTranslator skillTranslator;

    public SkillService(SkillRepository skillRepository, UserRepository userRepository, SkillTranslator skillTranslator)
    {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.skillTranslator = skillTranslator;
    }
    public Optional<SkillDTO> addSkill(String username, SkillDTO skillDTO) {
        String currentPrincipalName = getAuthenticatedName();
        Optional<User> oUserToAddSkill = userRepository.findByUsername(username);
        if (oUserToAddSkill.isPresent() && username.equals(currentPrincipalName)) {
            User userToAddSkill = oUserToAddSkill.get();
            Optional<Skill> oSkill = skillRepository.findById(userToAddSkill.getId());
            if (oSkill.isPresent()) {
                return addToExistingSkill(skillDTO, oSkill.get(), userToAddSkill);
            } else {
                return createSkill(skillDTO, userToAddSkill);
            }
        }
        return Optional.empty();
    }
    private Optional<SkillDTO> createSkill(SkillDTO skillToAddDTO,User userToAddSkill) {
        if (checkIfOnlyLetters(skillToAddDTO.getSkill())) {
            Skill skillToAdd = skillTranslator.translateToSkill(skillToAddDTO);
            skillToAdd.setUser(userToAddSkill);
            skillRepository.save(skillToAdd);
            return Optional.of(skillTranslator.translateToSkillDTO(skillToAdd));
        }
        return Optional.empty();

    }
    private Optional<SkillDTO> addToExistingSkill(SkillDTO skillDTO, Skill skillToAddTO, User userToAddSkill) {
        String skillToAddString = skillDTO.getSkill();
        String skillToAddTOString = skillToAddTO.getSkill();
        if (checkIfOnlyLetters(skillToAddString)){
            String stringToAdd = skillToAddTOString + ";" + skillToAddString;
            skillToAddTO.setSkill(stringToAdd);
            Skill skillToSave = new Skill(skillToAddTO.getId(), userToAddSkill, stringToAdd);
            skillRepository.save(skillToSave);
            return Optional.of(skillTranslator.translateToSkillDTO(skillToSave));
        }
        return Optional.empty();
    }

    private boolean checkIfOnlyLetters(String wordToCheck) {
        Pattern p = Pattern.compile("^[ A-Za-z]+$");
        Matcher m = p.matcher(wordToCheck);
        return m.matches();
    }
    private String getAuthenticatedName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Optional<String> collect(User user)
    {
        Optional<User> oUser = userRepository.findByUsername(user.getUsername());
        if (oUser.isPresent()) {
            Optional<Skill> oSkill = skillRepository.findById(oUser.get().getId());
            if (oSkill.isPresent()) {
                return Optional.of(oSkill.get().getSkill());
            }
        }
        return Optional.empty();
    }
}
