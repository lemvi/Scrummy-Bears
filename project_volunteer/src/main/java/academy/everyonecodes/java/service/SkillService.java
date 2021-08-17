package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.repositories.SkillRepository;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService
{
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillService(SkillRepository skillRepository, UserRepository userRepository)
    {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
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
