package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @MockBean
    private ActivityRepository activityRepository;


    @Test
    void findById() {
        activityService.findById(1L);

        verify(activityRepository).findById(1L);
    }

    @Test
    void findAll() {
        activityService.findAll();

        verify(activityRepository).findAll();
    }
}