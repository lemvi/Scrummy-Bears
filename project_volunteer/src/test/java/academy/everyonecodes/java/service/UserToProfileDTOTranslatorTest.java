//package academy.everyonecodes.java.service;
//
//import academy.everyonecodes.java.data.ProfileDTO;
//import academy.everyonecodes.java.data.User;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.time.LocalDate;
//import java.time.Year;
//import java.util.Optional;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//class UserToProfileDTOTranslatorTest
//{
//    @Autowired
//    private UserToProfileDTOTranslator userToProfileDTOTranslator;
//
//    @MockBean
//    private AgeCalculator ageCalculator;
//
//    @MockBean
//    private RatingService ratingService;
//
//    @Test
//    void toDTO_everything_existing()
//    {
//        ProfileDTO profileDTO = new ProfileDTO(
//                "name",
//                "First Last",
//                "organization",
//                69,
//                "description",
//                2.0
//        );
//        User user = new User(
//                "name",
//                "First",
//                "Last",
//                "organization",
//                LocalDate.of(2021, 8, 2),
//                "description"
//        );
//
//        user.setId(1L);
//
//        Mockito.when(ageCalculator.calculate(user))
//                .thenReturn(69);
//        Mockito.when(ratingService.calculateAverageUserRating(user.getId()))
//                .thenReturn(2.0);
//
//        var expected = profileDTO;
//        var actual = userToProfileDTOTranslator.toDTO(user);
//
//        System.out.println(expected);
//        System.out.println(actual);
//        Assertions.assertEquals(expected, actual);
//
//        Mockito.verify(ageCalculator).calculate(user);
//        Mockito.verify(ratingService).calculateAverageUserRating(user.getId());
//        Mockito.verifyNoMoreInteractions(ageCalculator, ratingService);
//    }
//
//    @Test
//    void toDTO_no_organization()
//    {
//        ProfileDTO profileDTO = new ProfileDTO(
//                "name",
//                "First Last",
//                null,
//                69,
//                "description",
//                2.0
//        );
//        User user = new User(
//                "name",
//                "First",
//                "Last",
//                null,
//                LocalDate.of(2021, 8, 2),
//                "description"
//        );
//
//        Mockito.when(ageCalculator.calculate(user))
//                .thenReturn(69);
//        Mockito.when(ratingService.calculateAverageUserRating(user.getId()))
//                .thenReturn(2.0);
//
//        var expected = profileDTO;
//        var actual = userToProfileDTOTranslator.toDTO(user);
//        Assertions.assertEquals(expected, actual);
//
//        Mockito.verify(ageCalculator).calculate(user);
//        Mockito.verify(ratingService).calculateAverageUserRating(user.getId());
//        Mockito.verifyNoMoreInteractions(ageCalculator, ratingService);
//    }
//
//    @Test
//    void toDTO_no_age()
//    {
//        ProfileDTO profileDTO = new ProfileDTO(
//                "name",
//                "First Last",
//                "organization",
//                null,
//                "description",
//                2.0
//        );
//        User user = new User(
//                "name",
//                "First",
//                "Last",
//                "organization",
//                null,
//                "description"
//        );
//
//        Mockito.when(ageCalculator.calculate(user))
//                .thenReturn(null);
//        Mockito.when(ratingService.calculateAverageUserRating(user.getId()))
//                .thenReturn(2.0);
//
//        var expected = profileDTO;
//        var actual = userToProfileDTOTranslator.toDTO(user);
//        Assertions.assertEquals(expected, actual);
//
//        Mockito.verify(ageCalculator).calculate(user);
//        Mockito.verify(ratingService).calculateAverageUserRating(user.getId());
//        Mockito.verifyNoMoreInteractions(ageCalculator, ratingService);
//    }
//
//    @Test
//    void toDTO_no_description()
//    {
//        ProfileDTO profileDTO = new ProfileDTO(
//                "name",
//                "First Last",
//                "organization",
//                69,
//                null,
//                2.0
//        );
//        User user = new User(
//                "name",
//                "First",
//                "Last",
//                "organization",
//                LocalDate.of(2021, 8, 2),
//                null
//        );
//
//        Mockito.when(ageCalculator.calculate(user))
//                .thenReturn(69);
//        Mockito.when(ratingService.calculateAverageUserRating(user.getId()))
//                .thenReturn(2.0);
//
//        var expected = profileDTO;
//        var actual = userToProfileDTOTranslator.toDTO(user);
//        Assertions.assertEquals(expected, actual);
//
//        Mockito.verify(ageCalculator).calculate(user);
//        Mockito.verify(ratingService).calculateAverageUserRating(user.getId());
//        Mockito.verifyNoMoreInteractions(ageCalculator, ratingService);
//    }
//
//    @Test
//    void toDTO_only_mandatory()
//    {
//        ProfileDTO profileDTO = new ProfileDTO(
//                "username",
//                "First Last",
//                "organizationName",
//                null,
//                null,
//                2.0
//        );
//        User user = new User(
//                "username",
//                "First",
//                "Last",
//                "organizationName"
//        );
//
//        Mockito.when(ageCalculator.calculate(user))
//                .thenReturn(null);
//        Mockito.when(ratingService.calculateAverageUserRating(user.getId()))
//                .thenReturn(2.0);
//
//        var expected = profileDTO;
//        var actual = userToProfileDTOTranslator.toDTO(user);
//        Assertions.assertEquals(expected, actual);
//
//        Mockito.verify(ageCalculator).calculate(user);
//        Mockito.verify(ratingService).calculateAverageUserRating(user.getId());
//        Mockito.verifyNoMoreInteractions(ageCalculator, ratingService);
//    }
//
//    @Test
//    void toDTO_everything_NoRatingReturned()
//    {
//        ProfileDTO profileDTO = new ProfileDTO(
//                "name",
//                "First Last",
//                "organization",
//                69,
//                "description",
//                Double.NaN
//        );
//        User user = new User(
//                "name",
//                "First",
//                "Last",
//                "organization",
//                LocalDate.of(2021, 8, 2),
//                "description"
//        );
//
//        user.setId(1L);
//
//        Mockito.when(ageCalculator.calculate(user))
//                .thenReturn(69);
//        Mockito.when(ratingService.calculateAverageUserRating(user.getId()))
//                .thenReturn(Double.NaN);
//
//        var expected = profileDTO;
//        var actual = userToProfileDTOTranslator.toDTO(user);
//
//        System.out.println(expected);
//        System.out.println(actual);
//        Assertions.assertEquals(expected, actual);
//
//        Mockito.verify(ageCalculator).calculate(user);
//        Mockito.verify(ratingService).calculateAverageUserRating(user.getId());
//        Mockito.verifyNoMoreInteractions(ageCalculator, ratingService);
//    }
//}