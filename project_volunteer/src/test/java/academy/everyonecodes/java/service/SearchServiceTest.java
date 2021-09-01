package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Skill;
import academy.everyonecodes.java.data.dtos.VolunteerProfileDTO;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.Role;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.SkillRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SearchServiceTest {
    @Autowired
    SearchService searchService;

    @MockBean
    ActivityRepository activityRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    SkillRepository skillRepository;
    @MockBean
    UserToProfileDTOTranslator userToProfileDTOTranslator;
    @MockBean
    RatingService ratingService;

    @Test
    void searchActivityTest_CONTAINING_NOTHING() {
        String keyword = "stuff";

        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(activityRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(List.of());
        when(activityRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(List.of());
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(List.of());

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            searchService.searchActivities(keyword);
        });

        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(activityRepository).findByTitleContainingIgnoreCase(keyword);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findByRecommendedSkillsContainingIgnoreCase(keyword);
    }
    @Test
    void searchActivityTest_CONTAINING_SKILLS() {
        String keyword = "skills";
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a, a1, a2);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);
        List<Activity> result = searchService.searchActivities(keyword);
        Assertions.assertEquals(expected, result);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(activityRepository).findByRecommendedSkillsContainingIgnoreCase(keyword);

    }

    @Test
    void searchActivityTest_CONTAINING_TITLE() {
        String keyword = "title";
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a1, a2);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(expected);
        List<Activity> result = searchService.searchActivities(keyword);
        Assertions.assertEquals(expected, result);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(activityRepository).findByTitleContainingIgnoreCase(keyword);
    }

    @Test
    void searchActivityTest_CONTAINING_DESCRIPTION() {
        String keyword = "description";
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a1, a2);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(expected);
        List<Activity> result = searchService.searchActivities(keyword);
        Assertions.assertEquals(expected, result);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
    }

    @Test
    void searchActivityTest_CONTAINING_ALL() {
        String keyword = "cook";
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a1 = new Activity("cook", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "cook", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a3 = new Activity("title2", "desc3", "cook", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());

        List<Activity> titleList = List.of(a1);
        List<Activity> descList = List.of(a2);
        List<Activity> skillsList = List.of(a3);
        List<Activity> expected = List.of(a1, a2, a3);

        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(titleList);
        when(activityRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(descList);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(skillsList);

        List<Activity> result = searchService.searchActivities(keyword);
        Assertions.assertEquals(expected, result);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(activityRepository).findByTitleContainingIgnoreCase(keyword);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findByRecommendedSkillsContainingIgnoreCase(keyword);
    }

    @ParameterizedTest
    @MethodSource("inputData")
    void test_mergeList(List<Activity> input1, List<Activity> input2, List<Activity> input3, List<Activity> input4, List<Activity> expected) {
        List<Activity> result = searchService.mergeActivityLists(input1, input2, input3, input4);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> inputData() {
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a3 = new Activity("title3", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a4 = new Activity("title4", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a5 = new Activity("title5", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a6 = new Activity("title6", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());


        return Stream.of(
                Arguments.of(
                        new LinkedList<>(List.of(a1, a2)), List.of(a1, a2), List.of(a3, a4), List.of(a5, a6), List.of(a1, a2, a3, a4, a5, a6)
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
                        new LinkedList<>(List.of(a1, a2)), List.of(), List.of(a1, a6), List.of(), List.of(a1, a2, a6)
                ),
                Arguments.of(
                        new LinkedList<>(), List.of(), List.of(), List.of(a2, a5), List.of(a2, a5)
                ),
                Arguments.of(
                        new LinkedList<>(), List.of(), List.of(), List.of(), List.of()
                )
        );
    }
    //---------------SEARCH VOLUNTEER TESTS-----------------------------------------------------

    @Test
    void searchVolunteerTest_CONTAINING_NOTHING() {
        String keyword = "stuff";

        when(userRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(List.of());
        when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of());

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            searchService.searchVolunteers(keyword);
        });

        verify(userRepository).findFullTextSearchByText(keyword);
        verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
        verify(skillRepository).findBySkillContainingIgnoreCase(keyword);
    }

    @Test
    void searchVolunteerTest_CONTAINING_SKILLS() {
        String keyword = "skill";
        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;
        Role role = new Role("ROLE_VOLUNTEER");
        role.setId(id1);
        User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        u1.setId(id1);
        u2.setId(id2);
        u3.setId(id3);
        VolunteerProfileDTO v1 = new VolunteerProfileDTO("volunteer1", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
        VolunteerProfileDTO v2 = new VolunteerProfileDTO("volunteer2", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
        VolunteerProfileDTO v3 = new VolunteerProfileDTO("volunteer3", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);

        Skill s1 = new Skill(u1, "skill");
        s1.setId(u1.getId());
        Skill s2 = new Skill(u2, "skill");
        s2.setId(u2.getId());
        Skill s3 = new Skill(u3, "skill");
        s3.setId(u3.getId());
        List<VolunteerProfileDTO> expected2 = List.of(v1, v2, v3);

        when(userRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(List.of());
        when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of(s1, s2, s3));
        when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of(s1, s2, s3));
        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u1)).thenReturn(v1);
        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u2)).thenReturn(v2);
        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u3)).thenReturn(v3);


        List<VolunteerProfileDTO> result = searchService.searchVolunteers(keyword, Optional.empty(), Optional.empty(), Optional.empty());
        Assertions.assertEquals(expected2, result);


        verify(userRepository).findFullTextSearchByText(keyword);
        verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
        verify(skillRepository).findBySkillContainingIgnoreCase(keyword);
        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u1);
        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u2);
        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u3);

    }


    @Test
    void searchVolunteerTest_CONTAINING_DESCRIPTION() {
        String keyword = "desc";
        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;
        Role role = new Role("ROLE_VOLUNTEER");
        role.setId(id1);
        User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        u1.setId(id1);
        u2.setId(id2);
        u3.setId(id3);
        VolunteerProfileDTO v1 = new VolunteerProfileDTO("volunteer1", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
        VolunteerProfileDTO v2 = new VolunteerProfileDTO("volunteer2", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
        VolunteerProfileDTO v3 = new VolunteerProfileDTO("volunteer3", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
        List<VolunteerProfileDTO> expected = List.of(v1, v2, v3);
        when(userRepository.findFullTextSearchByText(keyword)).thenReturn(List.of(u1, u2, u3));
        when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(List.of(u1, u2, u3));
        when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of());
        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u1)).thenReturn(v1);
        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u2)).thenReturn(v2);
        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u3)).thenReturn(v3);


        List<VolunteerProfileDTO> result = searchService.searchVolunteers(keyword, Optional.empty(), Optional.empty(), Optional.empty());
        Assertions.assertEquals(expected, result);


        verify(userRepository).findFullTextSearchByText(keyword);
        verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
        verify(skillRepository).findBySkillContainingIgnoreCase(keyword);
        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u1);
        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u2);
        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u3);
    }


        @Test
        void searchVolunteer_CONTAINING_ALL () {
            String keyword = "skill";
            Long id1 = 1L;
            Long id2 = 2L;
            Long id3 = 3L;
            Long id4 = 4L;
            Long id5 = 5L;
            Role role = new Role("ROLE_VOLUNTEER");
            role.setId(id1);
            User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
            User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
            User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
            User u4 = new User("volunteer4", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
            User u5 = new User("volunteer5", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));

            u1.setId(id1);
            u2.setId(id2);
            u3.setId(id3);
            u4.setId(id4);
            u5.setId(id5);

            VolunteerProfileDTO v1 = new VolunteerProfileDTO("volunteer1", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
            VolunteerProfileDTO v2 = new VolunteerProfileDTO("volunteer2", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
            VolunteerProfileDTO v3 = new VolunteerProfileDTO("volunteer3", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
            VolunteerProfileDTO v4 = new VolunteerProfileDTO("volunteer4", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);
            VolunteerProfileDTO v5 = new VolunteerProfileDTO("volunteer5", "email@email.com", Set.of(new Role("ROLE_VOLUNTEER")), "first last", 0);

            Skill s1 = new Skill(u1, "test");
            s1.setId(u1.getId());
            Skill s2 = new Skill(u2, "test");
            s2.setId(u2.getId());
            Skill s3 = new Skill(u3, "test");
            s3.setId(u3.getId());
            Skill s4 = new Skill(u4, "skill");
            s4.setId(u4.getId());
            Skill s5 = new Skill(u5, "skill");
            s5.setId(u5.getId());
            List<VolunteerProfileDTO> expected2 = List.of(v1, v2, v3, v4, v5);

            when(userRepository.findFullTextSearchByText(keyword)).thenReturn(List.of(u1));
            when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(List.of(u2, u3));
            when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of(s1));
            when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of(s3, s4, s5));
            when(userToProfileDTOTranslator.toVolunteerProfileDTO(u1)).thenReturn(v1);
            when(userToProfileDTOTranslator.toVolunteerProfileDTO(u2)).thenReturn(v2);
            when(userToProfileDTOTranslator.toVolunteerProfileDTO(u3)).thenReturn(v3);
            when(userToProfileDTOTranslator.toVolunteerProfileDTO(u4)).thenReturn(v4);
            when(userToProfileDTOTranslator.toVolunteerProfileDTO(u5)).thenReturn(v5);



            List<VolunteerProfileDTO> result = searchService.searchVolunteers(keyword, Optional.empty(), Optional.empty(), Optional.empty());
            Assertions.assertEquals(expected2, result);


            verify(userRepository).findFullTextSearchByText(keyword);
            verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
            verify(skillRepository).findFullTextSearchByText(keyword);
            verify(skillRepository).findBySkillContainingIgnoreCase(keyword);
            verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u1);
            verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u2);
            verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u3);
            verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u4);
            verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u5);

    }



    @ParameterizedTest
    @MethodSource("inputData2")
    void test_mergeUserLists(List<User> input1, List<Skill> input2, List<User> input3, List<Skill> input4, List<User> expected) {
        List<User> result = searchService.mergeUserLists(input1, input2, input3, input4);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> inputData2() {
        User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(new Role("ROLE_VOLUNTEER")));
        User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(new Role("ROLE_VOLUNTEER")));
        User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(new Role("ROLE_VOLUNTEER")));
        User u4 = new User("volunteer4", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(new Role("ROLE_VOLUNTEER")));
        User u5 = new User("volunteer5", "pw", "first", "last", LocalDate.now(), "123", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(new Role("ROLE_VOLUNTEER")));
        Skill s1 = new Skill(u1, "test");
        s1.setId(u1.getId());
        Skill s2 = new Skill(u2, "test");
        s2.setId(u2.getId());
        Skill s3 = new Skill(u3, "test");
        s3.setId(u3.getId());
        Skill s4 = new Skill(u4, "skill");
        s4.setId(u4.getId());
        Skill s5 = new Skill(u5, "skill");
        s5.setId(u5.getId());


        return Stream.of(
                Arguments.of(
                        new LinkedList<>(List.of(u1, u2)), List.of(s1, s2), List.of(u3, u4), List.of(s5), List.of(u1, u2, u3, u4, u5)
                )
                ,
                Arguments.of(
                        new LinkedList<>(List.of(u1, u2)), List.of(s3, s2), List.of(), List.of(), List.of(u1, u2, u3)
                ),
                Arguments.of(
                        new LinkedList<>(List.of(u1, u2)), List.of(s3, s4), List.of(u3, u4), List.of(), List.of(u1, u2, u3, u4)
                ),
                Arguments.of(
                        new LinkedList<>(List.of(u5, u4)), List.of(s5, s4), List.of(u5, u4), List.of(s5, s4), List.of(u5, u4)
                ),
                Arguments.of(
                        new LinkedList<>(List.of(u1, u2)), List.of(), List.of(u1, u4), List.of(), List.of(u1, u2,u4)
                ),
                Arguments.of(
                        new LinkedList<>(), List.of(), List.of(), List.of(s2, s5), List.of(u2, u5)
                ),
                Arguments.of(
                        new LinkedList<>(), List.of(), List.of(), List.of(), List.of()
                )
        );
    }
    //---------------------FILTER ACTIVITY TEST----------------------------------
    @Test
    void filterActivityTest_CONTAINING_All() {
        String keyword = "skills";
        String creatorFilter = "username";
        String startDate = "2020-08-08";
        String endDate = "2022-08-08";
        int ratingMin = 0;
        int ratingMax = 5;
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        organizer.setId(1L);
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);


        List<Activity> result = searchService.searchActivities(keyword, Optional.of(creatorFilter), Optional.of(startDate), Optional.of(endDate), Optional.of(ratingMin), Optional.of(ratingMax));

        Assertions.assertEquals(expected, result);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(ratingService, times(2)).calculateAverageUserRating(organizer.getId());

    }
    @Test
    void filterActivityTest_CONTAINING_organizer_Date() {
        String keyword = "skills";
        String creatorFilter = "username";
        String startDate = "2020-08-08";
        String endDate = "2022-08-08";

        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        organizer.setId(1L);
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);

        List<Activity> result = searchService.searchActivities(keyword, Optional.of(creatorFilter), Optional.of(startDate), Optional.of(endDate), Optional.empty(), Optional.empty());

        Assertions.assertEquals(expected, result);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findFullTextSearchByText(keyword);
    }
    @Test
    void filterActivityTest_CONTAINING_organizer_rating() {
        String keyword = "skills";
        String creatorFilter = "username";

        int ratingMin = 0;
        int ratingMax = 5;
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        organizer.setId(1L);
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);


        List<Activity> result = searchService.searchActivities(keyword, Optional.of(creatorFilter), Optional.empty(), Optional.empty(), Optional.of(ratingMin), Optional.of(ratingMax));

        Assertions.assertEquals(expected, result);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(ratingService, times(2)).calculateAverageUserRating(organizer.getId());

    }
    @Test
    void filterActivityTest_CONTAINING_Date_rating() {
        String keyword = "skills";
        String startDate = "2020-08-08";
        String endDate = "2022-08-08";
        int ratingMin = 0;
        int ratingMax = 5;
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        organizer.setId(1L);
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);


        List<Activity> result = searchService.searchActivities(keyword, Optional.empty(), Optional.of(startDate), Optional.of(endDate), Optional.of(ratingMin), Optional.of(ratingMax));

        Assertions.assertEquals(expected, result);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(ratingService, times(2)).calculateAverageUserRating(organizer.getId());

    }
    @Test
    void filterActivityTest_CONTAINING_organizer() {
        String keyword = "skills";
        String creatorFilter = "username";


        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        organizer.setId(1L);
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);

        List<Activity> result = searchService.searchActivities(keyword, Optional.of(creatorFilter), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        Assertions.assertEquals(expected, result);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findFullTextSearchByText(keyword);
    }
    @Test
    void filterActivityTest_CONTAINING_Date() {
        String keyword = "skills";
        String startDate = "2020-08-08";
        String endDate = "2022-08-08";

        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        organizer.setId(1L);
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);

        List<Activity> result = searchService.searchActivities(keyword,  Optional.empty(), Optional.of(startDate), Optional.of(endDate), Optional.empty(), Optional.empty());

        Assertions.assertEquals(expected, result);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findFullTextSearchByText(keyword);
    }
    @Test
    void filterActivityTest_CONTAINING_rating() {
        String keyword = "skills";

        int ratingMin = 0;
        int ratingMax = 5;
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        organizer.setId(1L);
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);


        List<Activity> result = searchService.searchActivities(keyword, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(ratingMin), Optional.of(ratingMax));

        Assertions.assertEquals(expected, result);
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(ratingService, times(2)).calculateAverageUserRating(organizer.getId());
    }
    @Test
    void filterActivityTest_CONTAINING_some_Filter_but_noMatches_exception() {
        String keyword = "stuff";
        String creatorFilter = "username";
        String startDate = "2020-08-08";
        String endDate = "2022-08-08";
        int ratingMin = 0;
        int ratingMax = 1;
        User organizer = new User("username", "first", "last", Set.of(new Role("ROLE_ORGANIZATION")));
        organizer.setId(1L);
        Activity a = new Activity("title1", "desc1", "skills", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a1 = new Activity("title1", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        Activity a2 = new Activity("title2", "desc1", "skills1", List.of("categorie1"), LocalDateTime.now(), LocalDateTime.now(), false, organizer, Set.of(), Set.of());
        List<Activity> expected = List.of(a);
        when(activityRepository.findFullTextSearchByText(keyword)).thenReturn(expected);
        when(activityRepository.findByRecommendedSkillsContainingIgnoreCase(keyword)).thenReturn(expected);
        when(ratingService.calculateAverageUserRating(organizer.getId())).thenReturn(3.0);


        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            searchService.searchActivities(keyword, Optional.of(creatorFilter), Optional.of(startDate), Optional.of(endDate), Optional.of(ratingMin), Optional.of(ratingMax));
        });
        verify(activityRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(activityRepository).findFullTextSearchByText(keyword);
        verify(ratingService).calculateAverageUserRating(organizer.getId());
    }
    //---------------------FILTER VOLUNTEER TEST----------------------------------
    @Test
    void filterVOLUNTEERTest_CONTAINING_All() {
        String keyword = "desc";
        String postalCode = "1210";
        int ratingMin = 0;
        int ratingMax = 5;
        Role role = new Role("ROLE_VOLUNTEER");
        role.setId(1L);
        User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);

        Skill s1 = new Skill(u1, "test");
        s1.setId(u1.getId());
        Skill s2 = new Skill(u2, "test");
        s2.setId(u2.getId());
        Skill s3 = new Skill(u3, "test");
        s3.setId(u3.getId());
        List<Skill> skills = List.of(s1, s2, s3);
        VolunteerProfileDTO v1 = new VolunteerProfileDTO("volunteer1", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v2 = new VolunteerProfileDTO("volunteer2", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v3 = new VolunteerProfileDTO("volunteer3", "email@email.com", Set.of(role), "first last", 0);
        List<VolunteerProfileDTO> expected = List.of(v1);
        List<User> userExpected = List.of(u1);
        when(userRepository.findFullTextSearchByText(keyword)).thenReturn(userExpected);
        when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(userExpected);
        when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of());

        when(ratingService.calculateAverageUserRating(u1.getId())).thenReturn(3.0);
        when(ratingService.calculateAverageUserRating(u1.getId())).thenReturn(3.0);
        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u1)).thenReturn(v1);



        List<VolunteerProfileDTO> result = searchService.searchVolunteers(keyword, Optional.of(postalCode), Optional.of(ratingMin), Optional.of(ratingMax));

        Assertions.assertEquals(expected, result);
        verify(userRepository).findFullTextSearchByText(keyword);
        verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);

        verify(ratingService, times(2)).calculateAverageUserRating(u1.getId());
        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u1);
    }
    @Test
    void filterVOLUNTEERTest_CONTAINING_PostalCode() {
        String keyword = "desc";
        String postalCode = "1210";

        Role role = new Role("ROLE_VOLUNTEER");
        role.setId(1L);
        User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);

        Skill s1 = new Skill(u1, "test");
        s1.setId(u1.getId());
        Skill s2 = new Skill(u2, "test");
        s2.setId(u2.getId());
        Skill s3 = new Skill(u3, "test");
        s3.setId(u3.getId());
        List<Skill> skills = List.of(s1, s2, s3);
        VolunteerProfileDTO v1 = new VolunteerProfileDTO("volunteer1", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v2 = new VolunteerProfileDTO("volunteer2", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v3 = new VolunteerProfileDTO("volunteer3", "email@email.com", Set.of(role), "first last", 0);
        List<VolunteerProfileDTO> expected = List.of(v1);
        List<User> userExpected = List.of(u1);
        when(userRepository.findFullTextSearchByText(keyword)).thenReturn(userExpected);
        when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(userExpected);
        when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of());

        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u1)).thenReturn(v1);



        List<VolunteerProfileDTO> result = searchService.searchVolunteers(keyword, Optional.of(postalCode), Optional.empty(), Optional.empty());

        Assertions.assertEquals(expected, result);
        verify(userRepository).findFullTextSearchByText(keyword);
        verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);

        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u1);
    }
    @Test
    void filterVOLUNTEERTest_CONTAINING_Rating() {
        String keyword = "desc";
        int ratingMin = 0;
        int ratingMax = 5;
        Role role = new Role("ROLE_VOLUNTEER");
        role.setId(1L);
        User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);

        Skill s1 = new Skill(u1, "test");
        s1.setId(u1.getId());
        Skill s2 = new Skill(u2, "test");
        s2.setId(u2.getId());
        Skill s3 = new Skill(u3, "test");
        s3.setId(u3.getId());
        List<Skill> skills = List.of(s1, s2, s3);
        VolunteerProfileDTO v1 = new VolunteerProfileDTO("volunteer1", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v2 = new VolunteerProfileDTO("volunteer2", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v3 = new VolunteerProfileDTO("volunteer3", "email@email.com", Set.of(role), "first last", 0);
        List<VolunteerProfileDTO> expected = List.of(v1);
        List<User> userExpected = List.of(u1);
        when(userRepository.findFullTextSearchByText(keyword)).thenReturn(userExpected);
        when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(userExpected);
        when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of());

        when(ratingService.calculateAverageUserRating(u1.getId())).thenReturn(3.0);
        when(ratingService.calculateAverageUserRating(u1.getId())).thenReturn(3.0);
        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u1)).thenReturn(v1);



        List<VolunteerProfileDTO> result = searchService.searchVolunteers(keyword, Optional.empty(), Optional.of(ratingMin), Optional.of(ratingMax));

        Assertions.assertEquals(expected, result);
        verify(userRepository).findFullTextSearchByText(keyword);
        verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);

        verify(ratingService, times(2)).calculateAverageUserRating(u1.getId());
        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u1);
    }
    @Test
    void filterVOLUNTEERTest_No_Match() {
        String keyword = "desc";
        String postalCode = "200000";
        int ratingMin = 0;
        int ratingMax = 1;
        Role role = new Role("ROLE_VOLUNTEER");
        role.setId(1L);
        User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);

        Skill s1 = new Skill(u1, "test");
        s1.setId(u1.getId());
        Skill s2 = new Skill(u2, "test");
        s2.setId(u2.getId());
        Skill s3 = new Skill(u3, "test");
        s3.setId(u3.getId());
        List<Skill> skills = List.of(s1, s2, s3);
        VolunteerProfileDTO v1 = new VolunteerProfileDTO("volunteer1", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v2 = new VolunteerProfileDTO("volunteer2", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v3 = new VolunteerProfileDTO("volunteer3", "email@email.com", Set.of(role), "first last", 0);

        when(userRepository.findFullTextSearchByText(keyword)).thenReturn(List.of(u1));
        when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(List.of(u1));
        when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of());

        Exception exception = assertThrows(HttpStatusCodeException.class, () ->
        {
            searchService.searchVolunteers(keyword, Optional.of(postalCode), Optional.of(ratingMin), Optional.of(ratingMax));
        });
        verify(userRepository).findFullTextSearchByText(keyword);
        verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
    }
    @Test
    void filterVOLUNTEERTest_No_Filter() {
        String keyword = "desc";

        Role role = new Role("ROLE_VOLUNTEER");
        role.setId(1L);
        User u1 = new User("volunteer1", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "desc", Set.of(role));
        User u2 = new User("volunteer2", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        User u3 = new User("volunteer3", "pw", "first", "last", LocalDate.now(), "1210", "city", "street", "1a", "email@email.com", "0660", "sd", Set.of(role));
        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);

        Skill s1 = new Skill(u1, "test");
        s1.setId(u1.getId());
        Skill s2 = new Skill(u2, "test");
        s2.setId(u2.getId());
        Skill s3 = new Skill(u3, "test");
        s3.setId(u3.getId());
        VolunteerProfileDTO v1 = new VolunteerProfileDTO("volunteer1", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v2 = new VolunteerProfileDTO("volunteer2", "email@email.com", Set.of(role), "first last", 0);
        VolunteerProfileDTO v3 = new VolunteerProfileDTO("volunteer3", "email@email.com", Set.of(role), "first last", 0);

        List<Skill> skills = List.of(s1, s2, s3);
        List<VolunteerProfileDTO> expected = List.of(v1);
        List<User> userExpected = List.of(u1);

        when(userRepository.findFullTextSearchByText(keyword)).thenReturn(userExpected);
        when(userRepository.findByDescriptionContainingIgnoreCase(keyword)).thenReturn(List.of());
        when(skillRepository.findFullTextSearchByText(keyword)).thenReturn(List.of());
        when(skillRepository.findBySkillContainingIgnoreCase(keyword)).thenReturn(List.of());

        when(userToProfileDTOTranslator.toVolunteerProfileDTO(u1)).thenReturn(v1);


        List<VolunteerProfileDTO> result = searchService.searchVolunteers(keyword, Optional.empty(), Optional.empty(), Optional.empty());

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected, result);

        verify(userRepository).findFullTextSearchByText(keyword);
        verify(userRepository).findByDescriptionContainingIgnoreCase(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);
        verify(skillRepository).findFullTextSearchByText(keyword);

        verify(userToProfileDTOTranslator).toVolunteerProfileDTO(u1);
    }
}
