package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class VolunteerSearchServiceTest {

	@Autowired
	VolunteerSearchService volunteerSearchService;

	@MockBean
	ActivityRepository activityRepository;

	@Test
	void getAllActivities() {
		volunteerSearchService.getAllActivities();
		Mockito.verify(activityRepository).findAll();
		Mockito.verifyNoMoreInteractions(activityRepository);
	}
}