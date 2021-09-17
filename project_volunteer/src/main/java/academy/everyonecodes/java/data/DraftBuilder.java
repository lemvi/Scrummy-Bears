package academy.everyonecodes.java.data;

import java.time.LocalDateTime;

public class DraftBuilder {
    private String title;
    private String description;
    private String recommendedSkills;
    private String categories;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Boolean openEnd;
    private String organizerUsername;

    public DraftBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public DraftBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public DraftBuilder setRecommendedSkills(String recommendedSkills) {
        this.recommendedSkills = recommendedSkills;
        return this;
    }

    public DraftBuilder setCategories(String categories) {
        this.categories = categories;
        return this;
    }

    public DraftBuilder setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public DraftBuilder setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public DraftBuilder setOpenEnd(Boolean openEnd) {
        this.openEnd = openEnd;
        return this;
    }

    public DraftBuilder setOrganizerUsername(String organizerUsername) {
        this.organizerUsername = organizerUsername;
        return this;
    }

    public Draft createDraft() {
        return new Draft(title, description, recommendedSkills, categories, startDateTime, endDateTime, openEnd, organizerUsername);
    }
}