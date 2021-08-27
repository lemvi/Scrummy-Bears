package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RatingServiceTest {

	@Autowired
	private RatingService ratingService;

	@MockBean
	private RatingRepository ratingRepository;

	@MockBean
	private UserService userService;

	@MockBean
	private ActivityService activityService;

	@MockBean
	private ActivityStatusService activityStatusService;

	@MockBean
	private EmailServiceImpl emailService;

	@Value("${ratingEmail.subject}")
	private String ratedSubject;

	@Value("${ratingEmail.text}")
	private String ratedText;

	@Mock
	private Authentication auth;

	@BeforeEach
	public void initSecurityContext()
	{
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	void calculateAverageUserRating_FindNoRatings() {
		Long userId = 1L;

		List<Rating> ratings = List.of();

		double expected = Double.NaN;

		when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		verify(ratingRepository).findByUser_Id(1L);
		verifyNoMoreInteractions(ratingRepository);

		assertEquals(expected, result);
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

		assertEquals(expected, result);
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

		assertEquals(expected, result);
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

		assertEquals(expected, result);
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
		Rating rating = new Rating();
		when(activityService.findActivityById(activityId)).thenReturn(activity);
		when(activityStatusService.findByActivity_id(activityId)).thenReturn(Optional.empty());

		Exception exception = assertThrows(HttpStatusCodeException.class, () -> { ratingService.rateUserForActivity(rating, activityId); });
		assertTrue(exception.getMessage().contains(ExceptionThrower.extractString(ErrorMessage.NO_STATUS_FOUND)));
	}

	@Test
	void rateUserForActivity_activityNotCompletedYet() {
		Long activityId = 1L;
		Activity activity = new Activity();
		Rating rating = new Rating();
		ActivityStatus activityStatus = new ActivityStatus();
		activityStatus.setStatus(Status.ACTIVE);
		when(activityService.findActivityById(activityId)).thenReturn(activity);
		when(activityStatusService.findByActivity_id(activityId)).thenReturn(Optional.of(activityStatus));

		Exception exception = assertThrows(HttpStatusCodeException.class, () -> { ratingService.rateUserForActivity(rating, activityId); }, ErrorMessage.ACTIVITY_NOT_COMPLETED_YET.name());
		assertTrue(exception.getMessage().contains(ExceptionThrower.extractString(ErrorMessage.ACTIVITY_NOT_COMPLETED_YET)));
	}

	@Test
	void rateUserForActivity_loggedInUserNotFound() {
		Long activityId = 1L;
		Activity activity = new Activity();
		Rating rating = new Rating();
		User loggedInUser = new User();
		String loggedInUsername = "username";
		ActivityStatus activityStatus = new ActivityStatus();
		activityStatus.setStatus(Status.COMPLETED);

		when(activityService.findActivityById(activityId)).thenReturn(activity);
		when(activityStatusService.findByActivity_id(activityId)).thenReturn(Optional.of(activityStatus));
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(loggedInUsername);
		when(userService.findByUsername(loggedInUsername)).thenReturn(Optional.empty());

		Exception exception = assertThrows(HttpStatusCodeException.class, () -> { ratingService.rateUserForActivity(rating, activityId); }, ErrorMessage.USERNAME_NOT_FOUND.name());
		assertTrue(exception.getMessage().contains(ExceptionThrower.extractString(ErrorMessage.USERNAME_NOT_FOUND)));
	}

	@Test
	void rateUserForActivity_userToRateNotFound() {
		Long activityId = 1L;
		Activity activity = new Activity();
		activity.setParticipants(Set.of());
		Rating rating = new Rating();
		activity.setOrganizer(new User());
		User loggedInUser = new User();
		String loggedInUsername = "username";
		loggedInUser.setUsername(loggedInUsername);
		ActivityStatus activityStatus = new ActivityStatus();
		activityStatus.setStatus(Status.COMPLETED);

		when(activityService.findActivityById(activityId)).thenReturn(activity);
		when(activityStatusService.findByActivity_id(activityId)).thenReturn(Optional.of(activityStatus));
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(loggedInUsername);
		when(userService.findByUsername(loggedInUsername)).thenReturn(Optional.of(loggedInUser));

		Exception exception = assertThrows(HttpStatusCodeException.class, () -> { ratingService.rateUserForActivity(rating, activityId); }, ErrorMessage.USER_NOT_INVOLVED_IN_ACTIVITY.name());
		assertTrue(exception.getMessage().contains(ExceptionThrower.extractString(ErrorMessage.USER_NOT_INVOLVED_IN_ACTIVITY)));
	}

	@Test
	void rateUserForActivity_valid() {
		Long activityId = 1L;
		User userToRate = new User();
		String email = "email@email.com";
		userToRate.setEmailAddress(email);
		userToRate.setId(activityId);
		userToRate.setUsername("organizer");

		Rating rating = new Rating();
		User loggedInUser = new User();
		String loggedInUsername = "username";
		loggedInUser.setUsername(loggedInUsername);

		Activity activity = new Activity();
		activity.setId(activityId);
		activity.setParticipants(Set.of(loggedInUser));
		activity.setOrganizer(userToRate);

		ActivityStatus activityStatus = new ActivityStatus();
		activityStatus.setStatus(Status.COMPLETED);
		String text = "\n Activity Id: " + activity.getId() +
				"\n Activity title: " + activity.getTitle() +
				"\n Rating: " + rating.getRating() +
				"\n Feedback: " + rating.getFeedback();

		when(activityService.findActivityById(activityId)).thenReturn(activity);
		when(activityStatusService.findByActivity_id(activityId)).thenReturn(Optional.of(activityStatus));
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(loggedInUsername);
		when(userService.findByUsername(loggedInUsername)).thenReturn(Optional.of(loggedInUser));

		ratingService.rateUserForActivity(rating, activityId);

		verify(emailService).sendSimpleMessage(email, ratedSubject, ratedText + text);
		verify(ratingRepository).save(rating);

	}
}