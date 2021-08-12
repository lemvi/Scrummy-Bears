package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProfileViewerTest {
//TODO CHECK IF WORKING WHEN MERGED
    @Autowired
    ProfileViewer profileViewer;
    @MockBean
    UserRepository userRepository;
    @MockBean
    SkillRepository skillRepository;
    @MockBean
    RatingRepository ratingRepository;

    @Test
    void getSkill_test() {
        String username = "username";
        User user = new User( "username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Skill skill = new Skill("test");
        Long id = 1L;
        user.setId(id);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(skillRepository.findById(user.getId())).thenReturn(Optional.of(skill));

        profileViewer.getSkill(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(skillRepository).findById(id);
    }
    @Test
    void getRating_test() {
        String username = "username";
        User user = new User( "username", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        Rating rating = new Rating(1);
        Long id = 1L;
        user.setId(id);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(ratingRepository.findById(user.getId())).thenReturn(Optional.of(rating));

        profileViewer.getRating(username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(ratingRepository).findById(id);
    }
}
