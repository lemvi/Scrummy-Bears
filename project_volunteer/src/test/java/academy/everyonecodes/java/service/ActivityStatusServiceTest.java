package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.repositories.ActivityStatusRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ActivityStatusServiceTest {

	@Autowired
	ActivityStatusService activityStatusService;

	@MockBean
	ActivityStatusRepository activityStatusRepository;

	@Test
	void getActivityStatus() {
		Long activityId = 1L;

		activityStatusService.getActivityStatus(activityId);

		Mockito.verify(activityStatusRepository).findById(activityId);
		Mockito.verifyNoMoreInteractions(activityStatusRepository);
	}

	@Test
	void changeActivityStatus() {
		Activity activity = new Activity();
		activity.setId(1L);
		Status status = Status.COMPLETED;

		activityStatusService.changeActivityStatus(activity, status);

		Mockito.verify(activityStatusRepository).save(new ActivityStatus(activity.getId(), activity, status));
		Mockito.verifyNoMoreInteractions(activityStatusRepository);
	}
}