package academy.everyonecodes.java;

import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.data.UserRepository;
import academy.everyonecodes.java.service.UserToUserDTOTranslator;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ViewerEditorServiceTest {

    @Autowired
    ViewerEditorService viewerEditorService;
    @MockBean
    UserToUserDTOTranslator userToUserDTOTranslator;
    @MockBean
    UserRepository userRepository;

    @Test
    void getAccountInfo_found_usernameISEqual_test() {
        String input = "test";
        User user = new User( "test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());

        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(userToUserDTOTranslator.translateToDTO(user)).thenReturn(userdto);
        Optional<UserDTO> oUserDTO = viewerEditorService.getAccountInfo(input);

        Assertions.assertEquals(oUserDTO, Optional.of(userdto));
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verify(userToUserDTOTranslator).translateToDTO(user);
    }
    @Test
    void getAccountInfo_notFound_test() {
        String input = "test";
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.empty());

        viewerEditorService.getAccountInfo(input);
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userToUserDTOTranslator);
    }

    @Test
    void editAccountInfo_UserFOUND_usernameISEqual_test() {
        String input = "test";
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role()));
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role()));
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(userToUserDTOTranslator.translateToUser(userdto)).thenReturn(user);
        Mockito.when(userToUserDTOTranslator.translateToDTO(user)).thenReturn(userdto);

        Mockito.when(userRepository.save(user)).thenReturn(user);

        viewerEditorService.editAccountInfo(input, userdto);
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verify(userToUserDTOTranslator).translateToUser(userdto);
        Mockito.verify(userToUserDTOTranslator).translateToDTO(user);
        Mockito.verify(userRepository).save(user);
    }
    @Test
    void editAccountInfo_UserNotFOUND_editAccount_test() {
        String input = "test";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.empty());


        viewerEditorService.editAccountInfo(input, userdto);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userToUserDTOTranslator);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
    @Test
    void editAccount_usernameNotEqual() {
        String input = "test";
        UserDTO userFalseUsername = new UserDTO("false", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        User user = new User("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());

        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        viewerEditorService.editAccountInfo(input, userFalseUsername);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userToUserDTOTranslator);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}

