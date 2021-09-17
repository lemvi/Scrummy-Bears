package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityBuilder;
import academy.everyonecodes.java.data.ActivityStatus;
import academy.everyonecodes.java.data.Status;
import academy.everyonecodes.java.data.repositories.ActivityStatusRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

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
	void changeActivityStatus_ActivityStatus_existing() {
		Activity activity = new ActivityBuilder().createActivity();
		activity.setId(1L);
		Status status = Status.COMPLETED;
		ActivityStatus activityStatusNotCompleted = new ActivityStatus(activity, Status.ACTIVE);
		ActivityStatus activityStatus = new ActivityStatus(activity, status);

		Mockito.when(activityStatusRepository.findById(activity.getId())).thenReturn(Optional.of(activityStatusNotCompleted));
		Mockito.when(activityStatusRepository.save(activityStatus)).thenReturn(activityStatus);

		activityStatusService.changeActivityStatus(activity, status);

		Mockito.verify(activityStatusRepository).findById(activity.getId());
		Mockito.verify(activityStatusRepository).save(activityStatus);

		Mockito.verifyNoMoreInteractions(activityStatusRepository);
	}

	@Test
	void changeActivityStatus_ActivityStatus_empty() {
		Activity activity = new ActivityBuilder().createActivity();
		activity.setId(1L);
		Status status = Status.COMPLETED;
		ActivityStatus activityStatus = new ActivityStatus(activity, status);

		Mockito.when(activityStatusRepository.findById(activity.getId())).thenReturn(Optional.empty());

		activityStatusService.changeActivityStatus(activity, status);

		Mockito.verify(activityStatusRepository).findById(activity.getId());
		Mockito.verify(activityStatusRepository).save(new ActivityStatus(activity, status));
		Mockito.verifyNoMoreInteractions(activityStatusRepository);
	}
}