package academy.everyonecodes.java;


import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//TODO: Did not work, needs more research how to test a secured Endpoint & with Principal.
/*
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ViewerEditorEndpointTest {

    @Autowired
    TestRestTemplate template;
    @MockBean
    ViewerEditorService viewerEditorService;
    @MockBean
    Principal principal;




        @Test
        @WithMockUser(username = "test", password = "test", authorities = "ROLE_INDIVIDUAL")
        void getAccountInfo_found_test() {
            String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test","test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        //Principal principal = Mockito.mock(Principal.class);
        //Mockito.when(principal.getName()).thenReturn(input);
        //Mockito.when(viewerEditorService.getAccountInfo(input, principal)).thenReturn(Optional.of(userdto));
        template.getForObject(url + input, UserDTO.class);
        //Mockito.verify(principal).getName();
        Mockito.verify(viewerEditorService).getAccountInfo(input);


    }
    @Test
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_INDIVIDUAL")
    void getAccountInfo_notFound_test() {
        String input = "test";
        String url = "/account/";


        Mockito.when(viewerEditorService.getAccountInfo(input, principal)).thenReturn(Optional.empty());
        Mockito.when(principal.getName()).thenReturn(input);

        template.getForObject(url + input, UserDTO.class);
        Mockito.verify(viewerEditorService).getAccountInfo(input, principal);
        Mockito.verifyNoInteractions(principal.getName());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = {"INDIVIDUAL"})
    void editAccountInfo_found_test() {
        String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        //Mockito.when(principal.getName()).thenReturn(input);
        //Mockito.when(viewerEditorService.editAccountInfo(input, userdto, principal)).thenReturn(Optional.of(userdto));
        template.put(url + input, userdto);
        //Mockito.verify(principal).getName();
        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
    }/*
    @Test
    void editAccountInfo_notFound_test() {
        String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test",Set.of());

        Mockito.when(viewerEditorService.editAccountInfo(input, userdto, principal)).thenReturn(Optional.empty());
        Mockito.when(principal.getName()).thenReturn(input);
        template.put(url + input, userdto);

        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto, principal);
    }


}*/
