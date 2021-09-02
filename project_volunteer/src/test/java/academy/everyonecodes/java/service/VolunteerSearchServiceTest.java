package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.repositories.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class VolunteerSearchServiceTest {

	@Autowired
	ActivityService activityService;

	@MockBean
	ActivityRepository activityRepository;

	@Test
	void getAllActivities() {
		activityService.getAllActivities(false);
		Mockito.verify(activityRepository).findAll();
		Mockito.verifyNoMoreInteractions(activityRepository);
	}
}