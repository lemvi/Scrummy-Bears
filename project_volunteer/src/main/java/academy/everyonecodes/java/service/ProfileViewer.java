package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service  //TODO CHECK IF WORKING WHEN MERGED
public class ProfileViewer
{
    private final UserRepository userRepository;
    private final SkillReposiotry skillReposiotry;
    private final RatingRepostioy ratingRepostioy;

    public ProfileViewer(UserRepository userRepository, SkillReposiotry skillReposiotry, RatingRepostioy ratingRepostioy) {
        this.userRepository = userRepository;
        this.skillReposiotry = skillReposiotry;
        this.ratingRepostioy = ratingRepostioy;
    }

    public Optional<Skill> getSkill(String username) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (oUser.isPresent()) {
            User user = oUser.get();
            Optional<Skill> oSkill = skillReposiotry.findById(user.getId());
            if (oSkill.isPresent()) {
                return oSkill;
            }
        }
    return Optional.empty();
    }
    public Optional<Rating> getRating(String username) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (oUser.isPresent()) {
            User user = oUser.get();
            Optional<Rating> oRating = ratingRepostioy.findById(user.getId());
            if (oRating.isPresent()) {
                return oRating;
            }
        }
        return Optional.empty();
    }

}
