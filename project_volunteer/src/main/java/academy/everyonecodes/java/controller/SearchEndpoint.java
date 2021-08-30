package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.dtos.IndividualVolunteerDTO;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.service.SearchService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/search")
public class SearchEndpoint {
    private final SearchService searchService;

    public SearchEndpoint(SearchService searchService) {
        this.searchService = searchService;

    }

    @GetMapping("/activities/{text}")
    @Secured("ROLE_VOLUNTEER")
    List<Activity> searchActivities(@PathVariable String text) {
        return  searchService.searchActivities(text);
    }
    @GetMapping("/volunteers/{text}")
    @Secured({"ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    List<VolunteerProfileDTO> searchVolunteers(@PathVariable String text) {
        return  searchService.searchVolunteers(text);
    }

 }
