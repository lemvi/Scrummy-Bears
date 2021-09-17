package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.dtos.SkillDTO;
import academy.everyonecodes.java.data.repositories.SkillRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AddSkillServiceTest {
    @Autowired
    SkillService skillService;
    @MockBean
    SkillRepository skillRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    private Authentication auth;
    @MockBean
    SkillTranslator translator;
    @BeforeEach
    public void initSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void addSkill_userNotFound_test() {
        String username = "test";
        String skillToAdd = "skill";
        SkillDTO skilldto = new SkillDTO(skillToAdd);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        skillService.addSkill(username, skilldto);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verifyNoInteractions(skillRepository);
    }

    @Test
    void addSkill_userFound_createNewSkill_noNumNoSpecialCh_test() {
        String username = "test";
        String skillToAdd = "skill";
        Long id = 1L;
        User user = new UserEntityBuilder().setUsername("test").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setOrganizationName("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createUser();
        user.setId(id);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(auth.getName()).thenReturn(username);
        Mockito.when(skillRepository.findById(id)).thenReturn(Optional.empty());
        Skill skill = new Skill(id, user, skillToAdd);
        SkillDTO skilldto = new SkillDTO(skillToAdd);
        Mockito.when(translator.translateToSkill(skilldto)).thenReturn(skill);
        Mockito.when(skillRepository.save(skill)).thenReturn(skill);
        Mockito.when(translator.translateToSkillDTO(skill)).thenReturn(skilldto);

        skillService.addSkill(username, skilldto);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(auth).getName();
        Mockito.verify(skillRepository).findById(id);
        Mockito.verify(translator).translateToSkill(skilldto);
        Mockito.verify(skillRepository).save(skill);
        Mockito.verify(translator).translateToSkillDTO(skill);

    }
    @Test
    void addSkill_userFound_createNewSkill_withNumOrSpecialCh_test() {
        String username = "test";
        String skillToAdd = "34234*#3234";
        Long id = 1L;
        User user = new UserEntityBuilder().setUsername("test").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setOrganizationName("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createUser();
        user.setId(id);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(auth.getName()).thenReturn(username);
        Mockito.when(skillRepository.findById(id)).thenReturn(Optional.empty());
        Skill skill = new Skill(id, user, skillToAdd);
        SkillDTO skilldto = new SkillDTO(skillToAdd);

        skillService.addSkill(username, skilldto);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(auth).getName();
        Mockito.verify(skillRepository).findById(id);
        Mockito.verifyNoInteractions(translator);
        Mockito.verifyNoMoreInteractions(skillRepository);
    }
    @Test
    void addSkill_userFound_addToExistingSkill_noNumNoSpecialCh_test() {
        String username = "test";
        String existingSkill = "existingSkill";
        String skillToAdd = "skill";

        Long id = 1L;
        User user = new UserEntityBuilder().setUsername("test").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setOrganizationName("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createUser();
        user.setId(id);
        Skill skill = new Skill(id, user, existingSkill);
        SkillDTO skilldto = new SkillDTO(skillToAdd);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(auth.getName()).thenReturn(username);
        Mockito.when(skillRepository.findById(id)).thenReturn(Optional.of(skill));
        existingSkill = existingSkill + ";" + skillToAdd;
        Skill skillToSave = new Skill(id, user, existingSkill);
        SkillDTO skillDTOToSave = new SkillDTO(existingSkill);

        Mockito.when(skillRepository.save(skillToSave)).thenReturn(skillToSave);
        Mockito.when(translator.translateToSkillDTO(skillToSave)).thenReturn(skillDTOToSave);

        Optional<SkillDTO> result = skillService.addSkill(username, skilldto);
        Assertions.assertEquals(Optional.of(skillDTOToSave), result);
        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(auth).getName();
        Mockito.verify(skillRepository).findById(id);
        Mockito.verify(skillRepository).save(skillToSave);
        Mockito.verify(translator).translateToSkillDTO(skillToSave);

    }
    @Test
    void addSkill_userFound_addToExistingSkill_withNumOrSpecialCh_test() {
        String username = "test";
        String skillToAdd = "34234*#3234";
        Long id = 1L;
        User user = new UserEntityBuilder().setUsername("test").setPassword("test").setFirstNamePerson("test").setLastNamePerson("test").setOrganizationName("test").setDateOfBirth(LocalDate.of(2021, 2, 2)).setPostalCode("test").setCity("test").setStreet("test").setStreetNumber("test").setEmailAddress("test").setTelephoneNumber("test").setDescription("test").setRoles(Set.of()).createUser();
        user.setId(id);
        Skill skill = new Skill(id, user, skillToAdd);
        SkillDTO skilldto = new SkillDTO(skillToAdd);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(auth.getName()).thenReturn(username);
        Mockito.when(skillRepository.findById(id)).thenReturn(Optional.of(skill));

        skillService.addSkill(username, skilldto);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(auth).getName();
        Mockito.verify(skillRepository).findById(id);
        Mockito.verifyNoInteractions(translator);
        Mockito.verifyNoMoreInteractions(skillRepository);
    }
}
