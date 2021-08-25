package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Rating;
import academy.everyonecodes.java.service.RatingService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class RatingEndpoint {

    private final RatingService ratingService;

    public RatingEndpoint(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/activities/{activityId}/rate")
    Rating postRating(@PathVariable Long activityId, @RequestBody @Valid Rating rating) {
        return ratingService.rateUserForActivity(rating, activityId);
    }

    @PostMapping("/rate")
    Rating postRating(@RequestBody @Valid Rating rating) {
        return ratingService.saveRating(rating);
    }
}
