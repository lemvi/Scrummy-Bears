package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AddSkillServiceTest {
    @Autowired
    AddSkillService addSkillService;
    @MockBean
    SkillRepository skillRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    Principal principal;
    @Test
    void addSkill_userFound_test() {
        String username = "test";
        String skillToAdd = "skill";
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        SkillDTO skilldto = new SkillDTO(skillToAdd);
        Skill skill = new Skill(user, skillToAdd);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(principal.getName()).thenReturn(username);
        Mockito.when(skillRepository.save(skill)).thenReturn(skill);

        addSkillService.addSkill(username, skilldto, principal);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(principal).getName();
        Mockito.verify(skillRepository).save(skill);


    }
}
