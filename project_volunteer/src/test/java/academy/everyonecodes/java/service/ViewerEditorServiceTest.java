package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
public class ViewerEditorServiceTest {

    @Autowired
    ViewerEditorService viewerEditorService;

    @MockBean
    UserToIndividualVolunteerDTOTranslator userToIndividualVolunteerDTOTranslator;

    @MockBean
    UserRepository userRepository;

    @Mock
    private Authentication auth;

    @BeforeEach
    public void initSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getAccountInfo_found_usernameISEqual_test() {
        String input = "test";
        User user = new User( "test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        IndividualVolunteerDTO individualVolunteerDTO = new IndividualVolunteerDTO("test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());

        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(userToIndividualVolunteerDTOTranslator.translateToDTO(user)).thenReturn(individualVolunteerDTO);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(input);

        Optional<IndividualVolunteerDTO> oIndividualVolunteerDTO = viewerEditorService.getAccountInfo(input);

        Assertions.assertEquals(Optional.of(individualVolunteerDTO), oIndividualVolunteerDTO);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verify(userToIndividualVolunteerDTOTranslator).translateToDTO(user);
    }

    @Test
    void getAccountInfo_found_usernameISEqual_WrongAuthentication_test() {
        String input = "test";
        User user = new User( "test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        IndividualVolunteerDTO userdto = new IndividualVolunteerDTO("test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());



        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(userToIndividualVolunteerDTOTranslator.translateToDTO(user)).thenReturn(userdto);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("Wrong");

        Optional<IndividualVolunteerDTO> oUserDTO = viewerEditorService.getAccountInfo(input);

        Assertions.assertEquals(Optional.empty(), oUserDTO);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoInteractions(userToIndividualVolunteerDTOTranslator);
    }


    @Test
    void getAccountInfo_notFound_test() {
        String input = "test";
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.empty());

        viewerEditorService.getAccountInfo(input);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userToIndividualVolunteerDTOTranslator);
    }

    @Test
    void editAccountInfo_UserFOUND_usernameISEqual_test() {
        String input = "test";
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role()));
        IndividualVolunteerDTO individualVolunteerDTO = new IndividualVolunteerDTO("test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role()));
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(userToIndividualVolunteerDTOTranslator.translateToUser(individualVolunteerDTO)).thenReturn(user);
        Mockito.when(userToIndividualVolunteerDTOTranslator.translateToDTO(user)).thenReturn(individualVolunteerDTO);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(input);


        Mockito.when(userRepository.save(user)).thenReturn(user);

        viewerEditorService.editAccountInfo(input, individualVolunteerDTO);
        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verify(userToIndividualVolunteerDTOTranslator).translateToUser(individualVolunteerDTO);
        Mockito.verify(userToIndividualVolunteerDTOTranslator).translateToDTO(user);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void editAccountInfo_UserFOUND_usernameISEqual_WrongAuthentication_test() {
        String input = "test";
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role()));
        IndividualVolunteerDTO individualVolunteerDTO = new IndividualVolunteerDTO("test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of(new Role()));
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("Wrong");


        Mockito.when(userRepository.save(user)).thenReturn(user);

        viewerEditorService.editAccountInfo(input, individualVolunteerDTO);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoInteractions(userToIndividualVolunteerDTOTranslator);
    }

    @Test
    void editAccountInfo_UserNotFOUND_editAccount_test() {
        String input = "test";
        IndividualVolunteerDTO individualVolunteerDTO = new IndividualVolunteerDTO("test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.empty());
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(input);


        viewerEditorService.editAccountInfo(input, individualVolunteerDTO);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userToIndividualVolunteerDTOTranslator);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
    @Test
    void editAccount_usernameNotEqual() {
        String input = "test";
        IndividualVolunteerDTO userFalseUsername = new IndividualVolunteerDTO("false", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        User user = new User("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());

        Mockito.when(userRepository.findByUsername(input)).thenReturn(Optional.of(user));
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(input);


        viewerEditorService.editAccountInfo(input, userFalseUsername);

        Mockito.verify(userRepository).findByUsername(input);
        Mockito.verifyNoInteractions(userToIndividualVolunteerDTOTranslator);
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}

