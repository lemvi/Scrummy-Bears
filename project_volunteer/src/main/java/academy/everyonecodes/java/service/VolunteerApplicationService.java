package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ErrorMessage;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class VolunteerApplicationService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final EmailServiceImpl emailServiceImpl;
    private final String subject;

    public VolunteerApplicationService(ActivityRepository activityRepository, UserRepository userRepository, ActivityService activityService, EmailServiceImpl emailServiceImpl, @Value("${applicationEmail.subject}") String subject) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.activityService = activityService;
        this.emailServiceImpl = emailServiceImpl;
        this.subject = subject;
    }

    public void applyForActivityWithEmailAndText(Long ActivityId, Long userId, String text) {
        Activity activity = activityService.findActivityById(ActivityId);
        User user = getUser(userId);
        authenticateLoggedInUserEqualsObjectOwner(user.getUsername());

        activity.getApplicants().add(user);
        activityRepository.save(activity);

        String individualizedSubject = user.getUsername() + subject + activity.getTitle();
        if (isValidTextMax800Chars(text))
            emailServiceImpl.sendSimpleMessage(activity.getOrganizer().getEmailAddress(), individualizedSubject, text);
        }

    private boolean isValidTextMax800Chars(String text) {
       if (text.length() > 800) {
           ExceptionThrower.badRequest(ErrorMessage.TOO_MANY_CHARACTERS_MAX_800);
       }
        return true;
    }

    private User getUser(Long id) {
        Optional<User> oUser = userRepository.findById(id);
        if (oUser.isEmpty()) ExceptionThrower.badRequest(ErrorMessage.USERNAME_NOT_FOUND);
        return oUser.get();
    }

    private String getAuthenticatedName()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void authenticateLoggedInUserEqualsObjectOwner(String volunteerUsername)
    {
        if (!getAuthenticatedName().equals(volunteerUsername))
            ExceptionThrower.badRequest(ErrorMessage.LOGGED_IN_USER_NOT_MATCHING_REQUEST);
    }
}
