//TODO ALTE VERSION
//
//
//
// package academy.everyonecodes.java.controller;
//
//
//import academy.everyonecodes.java.data.IndividualVolunteerDTO;
//import academy.everyonecodes.java.service.ViewerEditorService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//
//import java.time.LocalDate;
//import java.util.Optional;
//import java.util.Set;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class ViewerEditorEndpointTest {
//    @Autowired
//    TestRestTemplate template;
//
//    @MockBean
//    ViewerEditorService viewerEditorService;
//
//    @Test
//    void getAccountInfo_found_test() {
//        String input = "test";
//        String url = "/account/";
//
//        IndividualVolunteerDTO individualVolunteerDTO = new IndividualVolunteerDTO("test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
//
//        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.of(individualVolunteerDTO));
//        template.getForObject(url + input, IndividualVolunteerDTO.class);
//        Mockito.verify(viewerEditorService).getAccountInfo(input);
//
//    }
//    @Test
//    void getAccountInfo_notFound_test() {
//        String input = "test";
//        String url = "/account/";
//
//
//        Mockito.when(viewerEditorService.getAccountInfo(input)).thenReturn(Optional.empty());
//        template.getForObject(url + input, IndividualVolunteerDTO.class);
//        Mockito.verify(viewerEditorService).getAccountInfo(input);
//    }
//
//    @Test
//    void editAccountInfo_found_test() {
//        String input = "test";
//        String url = "/account/";
//        IndividualVolunteerDTO individualVolunteerDTO = new IndividualVolunteerDTO("test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
//        Mockito.when(viewerEditorService.editAccountInfo(input, individualVolunteerDTO)).thenReturn(Optional.of(individualVolunteerDTO));
//        template.put(url + input, individualVolunteerDTO);
//        Mockito.verify(viewerEditorService).editAccountInfo(input, individualVolunteerDTO);
//    }
//    @Test
//    void editAccountInfo_notFound_test() {
//        String input = "test";
//        String url = "/account/";
//        IndividualVolunteerDTO individualVolunteerDTO = new IndividualVolunteerDTO("test", "test", "test", "test",LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test",Set.of());
//        Mockito.when(viewerEditorService.editAccountInfo(input, individualVolunteerDTO)).thenReturn(Optional.empty());
//        template.put(url + input, individualVolunteerDTO);
//
//        Mockito.verify(viewerEditorService).editAccountInfo(input, individualVolunteerDTO);
//    }
//
//}
//