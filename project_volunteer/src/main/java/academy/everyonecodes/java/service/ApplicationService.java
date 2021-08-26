package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.ErrorMessage;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;

@Service
@Validated
public class ApplicationService {

    private final ActivityRepository activityRepository;

    private final ActivityService activityService;
    private final UserService userService;
    private final EmailServiceImpl emailServiceImpl;
    private final StatusHandler statusHandler;
    private final String subject;

    @Size(max = 800, message = "Text can be max. 800 characters")
    private String text;

    public ApplicationService(ActivityRepository activityRepository, ActivityService activityService, UserService userService,
                              EmailServiceImpl emailServiceImpl, StatusHandler statusHandler,
                              @Value("${applicationEmail.subject}") String subject, @Value("${applicationEmail.text}") @Valid String text) {
        this.activityRepository = activityRepository;
        this.activityService = activityService;
        this.userService = userService;
        this.emailServiceImpl = emailServiceImpl;
        this.statusHandler = statusHandler;
        this.subject = subject;
        this.text = text;
    }

    public void applyForActivityWithEmailAndText(Long ActivityId, Long userId, String text) {
        Activity activity = activityService.findActivityById(ActivityId);
        Optional<User> oUser = userService.findById(userId);
        User user = oUser.get();
        authenticateLoggedInUserEqualsObjectOwner(user.getUsername());

        activity.getApplicants().add(user);
        Set<User> applicants = activity.getApplicants();
        activity.setApplicants(applicants);

        String individualizedSubject = user.getUsername() + subject + activity.getTitle();
        emailServiceImpl.sendSimpleMessage(activity.getOrganizer().getEmailAddress(), individualizedSubject, text);

        statusHandler.getStatusForSpecificActivityAndVolunteer(activity, user.getId());
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
