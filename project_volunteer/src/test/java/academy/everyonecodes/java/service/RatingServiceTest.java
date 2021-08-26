package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RatingServiceTest {

	@Autowired
	RatingService ratingService;

	@MockBean
	RatingRepository ratingRepository;

	@MockBean
	UserService userService;

	@MockBean
	ActivityService activityService;

	@MockBean
	ActivityStatusService activityStatusService;

	@MockBean
	EmailServiceImpl emailService;

	@Test
	void calculateAverageUserRating_FindNoRatings() {
		Long userId = 1L;

		List<Rating> ratings = List.of();

		double expected = Double.NaN;

		when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		verify(ratingRepository).findByUser_Id(1L);
		verifyNoMoreInteractions(ratingRepository);

		Assertions.assertEquals(expected, result);
	}

	@Test
	void calculateAverageUserRating_FindOneRating() {
		Long userId = 1L;

		List<Rating> ratings = List.of(
				new Rating(3)
		);

		double expected = 3;

		when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		verify(ratingRepository).findByUser_Id(1L);
		verifyNoMoreInteractions(ratingRepository);

		Assertions.assertEquals(expected, result);
	}

	@Test
	void calculateAverageUserRating_FindSeveralRatings_ResultIsNatural() {
		Long userId = 1L;

		List<Rating> ratings = List.of(
				new Rating(2),
				new Rating(2),
				new Rating(2)
		);

		double expected = 2;

		when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		verify(ratingRepository).findByUser_Id(1L);
		verifyNoMoreInteractions(ratingRepository);

		Assertions.assertEquals(expected, result);
	}

	@Test
	void calculateAverageUserRating_FindSeveralRatings_ResultIsDecimal() {
		Long userId = 1L;

		List<Rating> ratings = List.of(
				new Rating(2),
				new Rating(2),
				new Rating(3),
				new Rating(3)
		);

		double expected = 2.5;

		when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		verify(ratingRepository).findByUser_Id(1L);
		verifyNoMoreInteractions(ratingRepository);

		Assertions.assertEquals(expected, result);
	}

	@Test
	void findByActivityAndUser() {
		Activity activity = new Activity();
		User user = new User();

		ratingService.findByActivityAndUser(activity, user);

		verify(ratingRepository).findByActivityAndUser(activity, user);
	}

	@Test
	void rateUserForActivity_noActivityStatusFound() {
		Long activityId = 1L;
		Activity activity = new Activity();
		when(activityService.findActivityById(activityId)).thenReturn(activity);
		when(activityStatusService.findByActivity_id(activityId)).thenReturn(Optional.empty());
	}
}