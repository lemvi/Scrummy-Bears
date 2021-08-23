package academy.everyonecodes.java.controller;

import academy.everyonecodes.java.data.Activity;
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

    @GetMapping("/{text}")
    @Secured("ROLE_VOLUNTEER")
    List<Activity> searchActivities(@PathVariable String text) {
        return  searchService.searchActivities(text);
    }
 }
