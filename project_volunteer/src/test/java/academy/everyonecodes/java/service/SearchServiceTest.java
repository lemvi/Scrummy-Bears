package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ActivityRepository;
import academy.everyonecodes.java.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest
public class SearchServiceTest {
    @Autowired
    SearchService searchService;

    @MockBean
    ActivityRepository activityRepository;

    @Test
    void searchActivityTest_CONTAINING_SKILLS() {
        String keyword = "skills";
        User organizer = new User("username", "first", "last", "company");
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a, a1, a2);
        Mockito.when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        Mockito.when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);
        List<Activity> result = searchService.searchActivities(keyword);
        Assertions.assertEquals(expected, result);
        Mockito.verify(activityRepository).findFullTextSearchByText(keyword);
        Mockito.verify(activityRepository).findByRecommendedSkillsContainingIgnoreCase(keyword);

    }
    @Test
    void searchActivityTest_CONTAINING_TITLE() {
        String keyword = "title";
        User organizer = new User("username", "first", "last", "company");
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a1, a2);
        Mockito.when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        Mockito.when(activityRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(expected);
        List<Activity> result = searchService.searchActivities(keyword);
        Assertions.assertEquals(expected, result);
        Mockito.verify(activityRepository).findFullTextSearchByText(keyword);
        Mockito.verify(activityRepository).findByTitleContainingIgnoreCase(keyword);
    }
    @Test
    void searchActivityTest_CONTAINING_DESCRIPTION() {
        String keyword = "description";
        User organizer = new User("username", "first", "last", "company");
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a1, a2);
        Mockito.when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        Mockito.when(activityRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(expected);
        List<Activity> result = searchService.searchActivities(keyword);
        Assertions.assertEquals(expected, result);
        Mockito.verify(activityRepository).findFullTextSearchByText(keyword);
        Mockito.verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
    }
    @Test
    void searchActivityTest_CONTAINING_ALL() {
        String keyword = "cook";
        User organizer = new User("username", "first", "last", "company");
        Activity a1 = new Activity("cook", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "cook", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a3 = new Activity("title2", "desc3", "cook", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());

        List<Activity> titleList = List.of(a1);
        List<Activity> descList = List.of(a2);
        List<Activity> skillsList = List.of(a3);
        List<Activity> expected = List.of(a1, a2, a3);

        Mockito.when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        Mockito.when(activityRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(titleList);
        Mockito.when(activityRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(descList);
        Mockito.when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(skillsList);

        List<Activity> result = searchService.searchActivities(keyword);
        Assertions.assertEquals(expected, result);
        Mockito.verify(activityRepository).findFullTextSearchByText(keyword);
        Mockito.verify(activityRepository).findByTitleContainingIgnoreCase(keyword);
        Mockito.verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        Mockito.verify(activityRepository).findByRecommendedSkillsContainingIgnoreCase(keyword);
    }
    @ParameterizedTest
    @MethodSource("inputData")
    void test_mergeList(List<Activity> input1, List<Activity> input2,List<Activity> input3 ,List<Activity> input4, List<Activity> expected)  {
        List<Activity> result = searchService.mergeLists(input1, input2, input3, input4);
        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> inputData() {
        User organizer = new User("username", "first", "last", "company");
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a3 = new Activity("title3", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a4 = new Activity("title4", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a5 = new Activity("title5", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a6 = new Activity("title6", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());


        return Stream.of(
                Arguments.of(
                        new LinkedList<>(List.of(a1, a2)), List.of(a1, a2), List.of(a3, a4), List.of(a5, a6), List.of(a1,a2, a3, a4, a5, a6)
                )
                ,
                Arguments.of(
                        new LinkedList<>(List.of(a1, a2)), List.of(a3, a2), List.of(), List.of(), List.of(a1, a2, a3)
                ),
                Arguments.of(
                        new LinkedList<>(List.of(a1, a2)), List.of(a3, a4), List.of(a3, a4), List.of(), List.of(a1, a2, a3, a4)
                ),
                Arguments.of(
                        new LinkedList<>(List.of(a5, a6)), List.of(a5, a6), List.of(a5, a6), List.of(a5, a6), List.of(a5, a6)
                ),
                Arguments.of(
                        new LinkedList<>(List.of(a1, a2)), List.of(), List.of(a1, a6), List.of(), List.of(a1,a2,a6)
                ),
                Arguments.of(
                        new LinkedList<>(), List.of(), List.of(), List.of(a2, a5), List.of(a2, a5)
                ),
                Arguments.of(
                        new LinkedList<>(),List.of(), List.of(), List.of(), List.of()
                )
        );
    }
}
