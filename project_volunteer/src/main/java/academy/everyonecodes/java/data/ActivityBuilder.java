package academy.everyonecodes.java.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ActivityBuilder {
    private String title;
    private String description;
    private String recommendedSkills;
    private List<String> categories;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Boolean openEnd;
    private User organizer;
    private Set<User> applicants;
    private Set<User> participants;
    private boolean deleted;

    public ActivityBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ActivityBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ActivityBuilder setRecommendedSkills(String recommendedSkills) {
        this.recommendedSkills = recommendedSkills;
        return this;
    }

    public ActivityBuilder setCategories(List<String> categories) {
        this.categories = categories;
        return this;
    }

    public ActivityBuilder setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public ActivityBuilder setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public ActivityBuilder setOpenEnd(Boolean openEnd) {
        this.openEnd = openEnd;
        return this;
    }

    public ActivityBuilder setOrganizer(User organizer) {
        this.organizer = organizer;
        return this;
    }

    public ActivityBuilder setApplicants(Set<User> applicants) {
        this.applicants = applicants;
        return this;
    }

    public ActivityBuilder setParticipants(Set<User> participants) {
        this.participants = participants;
        return this;
    }

    public ActivityBuilder setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Activity createActivity() {
        return new Activity(title, description, recommendedSkills, categories, startDateTime, endDateTime, openEnd, organizer, applicants, participants, deleted);
    }
}