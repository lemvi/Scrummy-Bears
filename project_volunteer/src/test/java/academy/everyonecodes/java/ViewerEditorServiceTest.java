package academy.everyonecodes.java;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.UserTranslator;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ViewerEditorServiceTest {

    @Autowired
    ViewerEditorService viewerEditorService;
    @MockBean
    UserTranslator userTranslator;
    @MockBean
    UserRepository userRepository;

    @Test
    void getAccountInfo_found_test() {
        String input = "test";
        User user = new User("test", "test", Set.of("test"), "test", "test", "test", "0000.00.00", "test", "test", "test", "test", "test", "test", "test")
        UserDTO userdto = new UserDTO("test", "test", Set.of("test"), "test", "test", "test","0000.00.00", "test", "test", "test", "test", "test", "test", "test")

        Mockito.when(userRepository.findByUsername()).thenReturn(Optional.of(user));
        Mockito.when(userTranslator.translateToDTO(user)).thenReturn(userdto);
        viewerEditorService.getAccountInfo(input);
        Mockito.verify(userRepository).findByUsername(Optional.of(user));
        Mockito.verify(userTranslator).translateToDTO(user);
    }
    @Test
    void getAccountInfo_notFound_test() {
        String input = "test";
        Mockito.when(userRepository.findByUsername()).thenReturn(Optional.empty());

        viewerEditorService.getAccountInfo(input);
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userTranslator);
    }

    @Test
    void editAccountInfo_UserFOUND_test() {
        String input = "test";
        User user = new User("test", "test", Set.of("test"), "test", "test", "test", "0000.00.00", "test", "test", "test", "test", "test", "test", "test")
        UserDTO userdto = new UserDTO("test", "test", Set.of("test"), "test", "test", "test","0000.00.00", "test", "test", "test", "test", "test", "test", "test")
        Mockito.when(userRepository.findByUsername()).thenReturn(Optional.of(user));
        Mockito.when(userTranslator.translateToUser(userdto)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        viewerEditorService.editAccountInfo(input, userdto);
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verify(userTranslator).translateToUser(userdto);
        Mockito.verify(userRepository).save(user);
    }
    @Test
    void editAccountInfo_UserNotFOUND_test() {
        String input = "test";
        UserDTO userdto = new UserDTO("test", "test", Set.of("test"), "test", "test", "test","0000.00.00", "test", "test", "test", "test", "test", "test", "test")
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.empty());


        viewerEditorService.editAccountInfo(input, userdto);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userTranslator);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}

