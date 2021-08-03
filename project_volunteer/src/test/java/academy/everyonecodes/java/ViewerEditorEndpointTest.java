package academy.everyonecodes.java;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserDTO;
import academy.everyonecodes.java.service.ViewerEditorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

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

        UserDTO userdto = new UserDTO("test", "test", Set.of("test"), "test", "test", "test","0000.00.00", "test", "test", "test", "test", "test", "test", "test")

        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.of(userdto));
        template.getForObject(url + input, UserDTO.class);
        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
    }
    @Test
    void getAccountInfo_notFound_test() {
        String input = "test";
        String url = "/account/";

        UserDTO userdto = new UserDTO("test", "test", Set.of("test"), "test", "test", "test","0000.00.00", "test", "test", "test", "test", "test", "test", "test")

        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.empty());
        template.getForObject(url + input, UserDTO.class);
        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
    }

    @Test
    void editAccountInfo_found_test() {
        String input = "test";
        String url = "/account/";
       //User user = new User("test", "test", Set.of("test"), "test", "test", "test", "0000.00.00", "test", "test", "test", "test", "test", "test", "test")
        UserDTO userdto = new UserDTO("test", "test", Set.of("test"), "test", "test", "test","0000.00.00", "test", "test", "test", "test", "test", "test", "test")

        Mockito.when(viewerEditorService.editAccountInfo(input, userdto)).thenReturn(Optional.of(userdto));
        template.postForObject(url + input, userdto, UserDTO.class);
        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
    }
    @Test
    void editAccountInfo_notFound_test() {
        String input = "test";
        UserDTO userdto = new UserDTO("test", "test", Set.of("test"), "test", "test", "test","0000.00.00", "test", "test", "test", "test", "test", "test", "test")

        Mockito.when(viewerEditorService.editAccountInfo(input, userdto)).thenReturn(Optional.empty());

        Mockito.verify(viewerEditorService).editAccountInfo(input, userdto);
    }

}
