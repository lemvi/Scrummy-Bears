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
    private final ActivityDraftTranslator activityDraftTranslator;
    private final DraftRepository draftRepository;
    private final UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, ActivityDraftTranslator activityDraftTranslator, DraftRepository draftRepository, UserRepository userRepository)
    {
        this.activityRepository = activityRepository;
        this.activityDraftTranslator = activityDraftTranslator;
        this.draftRepository = draftRepository;
        this.userRepository = userRepository;
    }

    public Activity postActivity(Activity activity)
    {
        Optional<User> oUser = userRepository.findByUsername(getAuthenticatedName());
        oUser.ifPresent(activity::setOrganizer);
        return activityRepository.save(activity);
    }

    /*
    public Draft saveAsDraft(Activity activity)
    {
        return draftRepository.save(activityDraftTranslator.toDraft(activity));
    }

     */

    public Draft postDraft(Draft draft)
    {
        draft.setOrganizer(getAuthenticatedName());
        return draftRepository.save(draft);
    }

    public List<Draft> getAllDraftsOfOrganizer()
    {
        return draftRepository.findByOrganizer(getAuthenticatedName());
    }

    public Optional<Draft> editDraft(Draft draft)
    {
        Optional<Draft> oDraft = draftRepository.findById(draft.getId());
        if (oDraft.isPresent())
        {
            return Optional.of(postDraft(draft));
        }
        return Optional.empty();
    }

    public Optional<Activity> saveDraftAsActivity(Long draftId)
    {
        Optional<Draft> oDraft = draftRepository.findById(draftId);
        if (oDraft.isPresent())
        {
            draftRepository.deleteById(draftId);
            return Optional.of(postActivity(activityDraftTranslator.toActivity(oDraft.get())));
        }
        return Optional.empty();
    }

    private String getAuthenticatedName()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
