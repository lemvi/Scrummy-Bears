package academy.everyonecodes.java;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.data.UserRepository;
import academy.everyonecodes.java.service.UserAndSkillTranslator;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ViewerEditorServiceTest {

    @Autowired
    ViewerEditorService viewerEditorService;
    @MockBean
    UserAndSkillTranslator userAndSkillTranslator;
    @MockBean
    UserRepository userRepository;
    @MockBean
    Principal principal;

    @Test
    void getAccountInfo_found_usernameISEqual_test() {
        String input = "test";
        User user = new User( "test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());

        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(userAndSkillTranslator.translateToUserDTO(user)).thenReturn(userdto);
        Mockito.when(principal.getName()).thenReturn(input);
        Optional<UserDTO> oUserDTO = viewerEditorService.getAccountInfo(input, principal);

       Assertions.assertEquals(oUserDTO, Optional.of(userdto));
       Mockito.verify(principal).getName();
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verify(userAndSkillTranslator).translateToUserDTO(user);
    }
    @Test
    void getAccountInfo_notFound_test() {
        String input = "test";
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.empty());
        Mockito.when(principal.getName()).thenReturn(input);

        viewerEditorService.getAccountInfo(input, principal);
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userAndSkillTranslator);
    }

    @Test
    void editAccountInfo_UserFOUND_usernameISEqual_test() {
        String input = "test";
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role()));
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role()));
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(userAndSkillTranslator.translateToUser(userdto)).thenReturn(user);
        Mockito.when(userAndSkillTranslator.translateToUserDTO(user)).thenReturn(userdto);
        Mockito.when(principal.getName()).thenReturn(input);


        Mockito.when(userRepository.save(user)).thenReturn(user);

        viewerEditorService.editAccountInfo(input, userdto, principal);
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verify(userAndSkillTranslator).translateToUser(userdto);
        Mockito.verify(userAndSkillTranslator).translateToUserDTO(user);
        Mockito.verify(userRepository).save(user);
    }
    @Test
    void editAccountInfo_UserNotFOUND_editAccount_test() {
        String input = "test";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.empty());
        Mockito.when(principal.getName()).thenReturn(input);


        viewerEditorService.editAccountInfo(input, userdto, principal);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userAndSkillTranslator);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
    @Test
    void editAccount_usernameNotEqual() {
        String input = "test";
        UserDTO userFalseUsername = new UserDTO("false", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        User user = new User("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());

        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(principal.getName()).thenReturn(input);


        viewerEditorService.editAccountInfo(input, userFalseUsername, principal);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userAndSkillTranslator);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}

