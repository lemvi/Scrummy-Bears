package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Activity;
import academy.everyonecodes.java.data.Draft;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.ActivityRepository;
import academy.everyonecodes.java.data.repositories.DraftRepository;
import academy.everyonecodes.java.data.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService
{
    private final ActivityRepository activityRepository;
    private final DraftActivityTranslator draftActivityTranslator;
    private final DraftRepository draftRepository;
    private final UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, DraftActivityTranslator draftActivityTranslator, DraftRepository draftRepository, UserRepository userRepository)
    {
        this.activityRepository = activityRepository;
        this.draftActivityTranslator = draftActivityTranslator;
        this.draftRepository = draftRepository;
        this.userRepository = userRepository;
    }

    public Activity postActivity(Activity activity)
    {
        String currentPrincipalName = getAuthenticatedName();
        Optional<User> oUser = userRepository.findByUsername(currentPrincipalName);
        oUser.ifPresent(activity::setOrganizer);
        return activityRepository.save(activity);
    }

    public Draft saveAsDraft(Draft draft) {
        return draftRepository.save(draft);
    }

    public Optional<Draft> editDraft(Draft draft) {
        Optional<Draft> oDraft = draftRepository.findById(draft.getId());
        if (oDraft.isPresent()) {
            return Optional.of(saveAsDraft(draft));
        }
        return Optional.empty();
    }

    public List<Draft> getAllDrafts()
    {
        return draftRepository.findAll();
    }

    public Draft saveAsDraft(Activity activity)
    {
        return draftRepository.save(draftActivityTranslator.toDraft(activity));
    }
    public Activity saveDraftAsActivity(Draft draft)
    {
        return postActivity(draftActivityTranslator.toActivity(draft));
    }

    private String getAuthenticatedName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
