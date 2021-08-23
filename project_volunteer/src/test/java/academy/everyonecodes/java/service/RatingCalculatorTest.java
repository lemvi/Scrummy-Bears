package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RatingServiceTest {

	@Autowired
	RatingService ratingService;

	@MockBean
	RatingRepository ratingRepository;

	@Test
	void calculateAverageUserRating_FindNoRatings() {
		Long userId = 1L;

		List<Rating> ratings = List.of();

		double expected = Double.NaN;

		Mockito.when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		Mockito.verify(ratingRepository).findByUser_Id(1L);
		Mockito.verifyNoMoreInteractions(ratingRepository);

		Assertions.assertEquals(expected, result);
	}

	@Test
	void calculateAverageUserRating_FindOneRating() {
		Long userId = 1L;

		List<Rating> ratings = List.of(
				new Rating(3)
		);

		double expected = 3;

		Mockito.when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		Mockito.verify(ratingRepository).findByUser_Id(1L);
		Mockito.verifyNoMoreInteractions(ratingRepository);

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

		Mockito.when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		Mockito.verify(ratingRepository).findByUser_Id(1L);
		Mockito.verifyNoMoreInteractions(ratingRepository);

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

		Mockito.when(ratingRepository.findByUser_Id(userId)).thenReturn(ratings);

		double result = ratingService.calculateAverageUserRating(1L);

		Mockito.verify(ratingRepository).findByUser_Id(1L);
		Mockito.verifyNoMoreInteractions(ratingRepository);

		Assertions.assertEquals(expected, result);
	}
}