package academy.everyonecodes.java.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
@Entity
public class Draft
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String recommendedSkills;

    private String categories;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endDateTime;

    private boolean openEnd;

    private String organizer;

    public Draft()
    {
    }

    public Draft(String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean openEnd, String organizer)
    {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.openEnd = openEnd;
        this.organizer = organizer;
    }

    public Draft(String title, String description, String recommendedSkills, String categories, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean openEnd, String organizer)
    {
        this.title = title;
        this.description = description;
        this.recommendedSkills = recommendedSkills;
        this.categories = categories;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.openEnd = openEnd;
        this.organizer = organizer;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getRecommendedSkills()
    {
        return recommendedSkills;
    }

    public void setRecommendedSkills(String recommendedSkills)
    {
        this.recommendedSkills = recommendedSkills;
    }

    public String getCategories()
    {
        return categories;
    }

    public void setCategories(String categories)
    {
        this.categories = categories;
    }

    public LocalDateTime getStartDateTime()
    {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime)
    {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime()
    {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime)
    {
        this.endDateTime = endDateTime;
    }

    public boolean isOpenEnd()
    {
        return openEnd;
    }

    public void setOpenEnd(boolean openEnd)
    {
        this.openEnd = openEnd;
    }

    public String getOrganizer()
    {
        return organizer;
    }

    public void setOrganizer(String organizer)
    {
        this.organizer = organizer;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Draft draft = (Draft) o;
        return openEnd == draft.openEnd && Objects.equals(id, draft.id) && Objects.equals(title, draft.title) && Objects.equals(description, draft.description) && Objects.equals(recommendedSkills, draft.recommendedSkills) && Objects.equals(categories, draft.categories) && Objects.equals(startDateTime, draft.startDateTime) && Objects.equals(endDateTime, draft.endDateTime) && Objects.equals(organizer, draft.organizer);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, title, description, recommendedSkills, categories, startDateTime, endDateTime, openEnd, organizer);
    }

    @Override
    public String toString()
    {
        return "Draft{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", recommendedSkills='" + recommendedSkills + '\'' +
                ", categories='" + categories + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", openEnd=" + openEnd +
                ", organizer='" + organizer + '\'' +
                '}';
    }
}
