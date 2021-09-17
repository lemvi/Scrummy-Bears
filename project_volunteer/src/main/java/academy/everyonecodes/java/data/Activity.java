package academy.everyonecodes.java.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@SQLDelete(sql = "UPDATE activity SET deleted = true WHERE id=?")
@FilterDef(name = "deletedActivityFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedActivityFilter", condition = "deleted = :isDeleted")
public class Activity implements ActivityDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 40,message = "Must have 1-40 characters")
    private String title;

    @NotEmpty
    private String description;

    private String recommendedSkills;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> categories;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endDateTime;

    @NotNull
    private Boolean openEnd;

    @ManyToOne(fetch = FetchType.EAGER)
    private User organizer;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> applicants;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> participants;

    private boolean deleted = Boolean.FALSE;

    public Activity(String title, String description, String recommendedSkills, List<String> categories, LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean openEnd, User organizer, Set<User> applicants, Set<User> participants, boolean deleted) {
        this.title = title;
        this.description = description;
        this.recommendedSkills = recommendedSkills;
        this.categories = categories;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.openEnd = openEnd;
        this.organizer = organizer;
        this.applicants = applicants;
        this.participants = participants;
        this.deleted = deleted;
    }

    public Activity(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecommendedSkills() {
        return recommendedSkills;
    }

    public void setRecommendedSkills(String recommendedSkills) {
        this.recommendedSkills = recommendedSkills;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Boolean isOpenEnd() {
        return openEnd;
    }

    public void setOpenEnd(Boolean openEnd) {
        this.openEnd = openEnd;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public Set<User> getApplicants() {
        return applicants;
    }

    public void setApplicants(Set<User> applicants) {
        this.applicants = applicants;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        Activity activity = (Activity) o;
        return isDeleted() == activity.isDeleted() && Objects.equals(getId(), activity.getId()) && Objects.equals(getTitle(), activity.getTitle()) && Objects.equals(getDescription(), activity.getDescription()) && Objects.equals(getRecommendedSkills(), activity.getRecommendedSkills()) && Objects.equals(getCategories(), activity.getCategories()) && Objects.equals(getStartDateTime(), activity.getStartDateTime()) && Objects.equals(getEndDateTime(), activity.getEndDateTime()) && Objects.equals(openEnd, activity.openEnd) && Objects.equals(getOrganizer(), activity.getOrganizer()) && Objects.equals(getApplicants(), activity.getApplicants()) && Objects.equals(getParticipants(), activity.getParticipants());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getRecommendedSkills(), getCategories(), getStartDateTime(), getEndDateTime(), openEnd, getOrganizer(), getApplicants(), getParticipants(), isDeleted());
    }

    @Override
    public String toString() {
        return "Activity{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", recommendedSkills='" + recommendedSkills + '\'' + ", categories=" + categories + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", openEnd=" + openEnd + ", organizer=" + organizer + ", applicants=" + applicants + ", participants=" + participants + ", deleted=" + deleted + '}';
    }

    @Override
    public boolean isActivity() {
        return true;
    }
}
