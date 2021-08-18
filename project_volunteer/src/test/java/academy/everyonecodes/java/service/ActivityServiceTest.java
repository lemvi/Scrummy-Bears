package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityServiceTest {

    @Autowired
    ActivityService activityService;

    @MockBean
    private ActivityRepository activityRepository;

//    @Test
//    void postActivity_valid(){
//        User organizer = new User();
//        Set<User> applicants = new HashSet<>();
//        Set<User> participants = new HashSet<>();
//        Activity activity = new Activity("title", "descr", "skills", List.of(), LocalDateTime.now(), LocalDateTime.now().plusHours(6), false, organizer, applicants, participants);
//        Mockito.when(activityRepository.save(activity)).thenReturn(activity);
//        activityService.postActivity(activity);
//        Mockito.verify(activityRepository).save(activity);
//    }
}
