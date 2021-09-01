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

    @GetMapping(value = "/activities/{text}", params = {"username", "startDate", "endDate", "ratingMin", "ratingMax"})
    @Secured("ROLE_VOLUNTEER")
    List<Activity> searchActivities(@PathVariable String text, @RequestParam Optional<String> username, @RequestParam Optional<String>  startDate,@RequestParam Optional<String>  endDate, @RequestParam Optional<Integer> ratingMin, @RequestParam Optional<Integer> ratingMax) {
        return  searchService.searchActivities(text, username, startDate, endDate, ratingMin, ratingMax);
    }
   /* @GetMapping(value = "/activities/{text}", params = {"username", "startDate", "endDate"})
    @Secured("ROLE_VOLUNTEER")
    List<Activity> searchActivities(@PathVariable String text, @RequestParam(required = false) String username, @RequestParam(required = false) String  startDate, @RequestParam(required = false) String endDate) {
        return  searchService.searchActivities(text, username, startDate, endDate);
    }
    @GetMapping(value = "/activities/{text}", params = {"username", "ratingMin", "ratingMax"})
    @Secured("ROLE_VOLUNTEER")
    List<Activity> searchActivities(@PathVariable String text, @RequestParam(required = false) String username, @RequestParam(required = false) Integer ratingMin, @RequestParam(required = false) Integer ratingMax) {
        return  searchService.searchActivities(text, username, ratingMin, ratingMax);
    }
    @GetMapping(value = "/activities/{text}", params = {"username"})
    @Secured("ROLE_VOLUNTEER")
    List<Activity> searchActivities(@PathVariable String text, @RequestParam(required = false) String username) {
        return  searchService.searchActivities(text, username);
    }

    */

    @GetMapping(value = "/volunteers/{text}", params = {"postalCode", "ratingMin", "ratingMax"})
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<VolunteerProfileDTO> searchVolunteers(@PathVariable String text, @RequestParam Optional<String> postalCode, @RequestParam Optional<Integer> ratingMin, @RequestParam Optional<Integer>ratingMax) {
        return  searchService.searchVolunteers(text, postalCode, ratingMin, ratingMax);
    }/*
    @GetMapping(value = "/volunteers/{text}", params = {"ratingMin", "ratingMax"})
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<VolunteerProfileDTO> searchVolunteers(@PathVariable String text, @RequestParam(required = false) Integer ratingMin, @RequestParam(required = false) Integer ratingMax) {
        return  searchService.searchVolunteers(text, ratingMin, ratingMax);
    }
    @GetMapping(value ="/volunteers/{text}",  params = {"postalCode"})
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<VolunteerProfileDTO> searchVolunteers(@PathVariable String text, @RequestParam(required = false) String postalCode) {
        return  searchService.searchVolunteers(text, postalCode);
    }
    @GetMapping(value ="/volunteers/{text}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<VolunteerProfileDTO> searchVolunteers(@PathVariable String text) {
        return  searchService.searchVolunteers(text);
    }
*/
 }
