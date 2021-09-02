package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.service.SearchService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/search")
public class SearchEndpoint {
    private final SearchService searchService;

    public SearchEndpoint(SearchService searchService) {
        this.searchService = searchService;

    }

    @GetMapping( "/activities/{text}")
    @Secured("ROLE_VOLUNTEER")
    List<Activity> searchActivities(@PathVariable String text, @RequestParam(required = false) Optional<String> username, @RequestParam(required = false) Optional<String>  startDate,@RequestParam(required = false) Optional<String>  endDate, @RequestParam(required = false) Optional<Integer> ratingMin, @RequestParam(required = false) Optional<Integer> ratingMax) {
        return  searchService.searchActivities(text, username, startDate, endDate, ratingMin, ratingMax);
    }


    @GetMapping("/volunteers/{text}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<VolunteerProfileDTO> searchVolunteers(@PathVariable String text, @RequestParam(required = false) Optional<String> postalCode, @RequestParam(required = false) Optional<Integer> ratingMin, @RequestParam(required = false) Optional<Integer>ratingMax) {
        return  searchService.searchVolunteers(text, postalCode, ratingMin, ratingMax);
    }
 }
