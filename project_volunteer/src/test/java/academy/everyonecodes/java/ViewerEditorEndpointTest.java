package academy.everyonecodes.java;


import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
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
    void getAccountInfo_found_test() {
        String input = "test";
        String url = "/account/";

        UserDTO userdto = new UserDTO("test", "test","test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());

        //Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(input);
        //Mockito.when(viewerEditorService.getAccountInfo(input, principal)).thenReturn(Optional.of(userdto));

        template.getForObject(url + input, UserDTO.class);

        Mockito.verify(principal).getName();
        Mockito.verify(viewerEditorService).getAccountInfo(input, principal);


    }
    @Test
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
    void editAccountInfo_found_test() {
        String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Mockito.when(principal.getName()).thenReturn(input);
        Mockito.when(viewerEditorService.editAccountInfo(input, userdto, principal)).thenReturn(Optional.of(userdto));
        template.put(url + input, userdto);
        Mockito.verify(principal).getName();
        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto, principal);
    }
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
