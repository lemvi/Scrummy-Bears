package academy.everyonecodes.java;


import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ViewerEditorEndpointTest {
    @Autowired
    TestRestTemplate template;

    @MockBean
    ViewerEditorService viewerEditorService;

    @Test
    void getAccountInfo_found_test() {
        String input = "test";
        String url = "/account/";

        UserDTO userdto = new UserDTO("test", "test","test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());

        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.of(userdto));
        template.getForObject(url + input, UserDTO.class);
        Mockito.verify(viewerEditorService).getAccountInfo(input);

    }
    @Test
    void getAccountInfo_notFound_test() {
        String input = "test";
        String url = "/account/";


        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.empty());
        template.getForObject(url + input, UserDTO.class);
        Mockito.verify(viewerEditorService).getAccountInfo(input);
    }

    @Test
    void editAccountInfo_found_test() {
        String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        //Mockito.when(viewerEditorService.editAccountInfo(input, userdto)).thenReturn(Optional.of(userdto));
        template.put(url + input, userdto);
       // Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
    }
    @Test
    void editAccountInfo_notFound_test() {
        String input = "test";
        String url = "/account/";
        UserDTO userdto = new UserDTO("test", "test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test",Set.of());
        //Mockito.when(viewerEditorService.editAccountInfo(input, userdto)).thenReturn(Optional.empty());
        template.put(url + input, userdto);

        //Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
    }

}
