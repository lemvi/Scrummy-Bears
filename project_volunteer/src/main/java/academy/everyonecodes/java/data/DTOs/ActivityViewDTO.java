package academy.everyonecodes.java.data.DTOs;

import academy.everyonecodes.java.data.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class ActivityViewDTO {
    private String title;
    private Status status;
    private LocalDateTime begin;
    private LocalDateTime end;
    private boolean openEnd;
    private OrganizerViewForVolunteerActivityViewDTO organizer;
    private int givenRatingToOrganizer;
    private String givenFeedbackToOrganizer;
    private int ratingReceivedByOrganizer;
    private String feedbackReceivedByOrganizer;

    public ActivityViewDTO() {
    }

    public ActivityViewDTO(String title,
                           Status status,
                           LocalDateTime begin,
                           LocalDateTime end,
                           boolean openEnd, OrganizerViewForVolunteerActivityViewDTO organizer,
                           int givenRatingToOrganizer,
                           String givenFeedbackToOrganizer,
                           int ratingReceivedByOrganizer,
                           String feedbackReceivedByOrganizer) {
        this.title = title;
        this.status = status;
        this.begin = begin;
        this.end = end;
        this.openEnd = openEnd;
        this.organizer = organizer;
        this.givenRatingToOrganizer = givenRatingToOrganizer;
        this.givenFeedbackToOrganizer = givenFeedbackToOrganizer;
        this.ratingReceivedByOrganizer = ratingReceivedByOrganizer;
        this.feedbackReceivedByOrganizer = feedbackReceivedByOrganizer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public boolean isOpenEnd() {
        return openEnd;
    }

    public void setOpenEnd(boolean openEnd) {
        this.openEnd = openEnd;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OrganizerViewForVolunteerActivityViewDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(OrganizerViewForVolunteerActivityViewDTO organizer) {
        this.organizer = organizer;
    }

    public int getGivenRatingToOrganizer() {
        return givenRatingToOrganizer;
    }

    public void setGivenRatingToOrganizer(int givenRatingToOrganizer) {
        this.givenRatingToOrganizer = givenRatingToOrganizer;
    }

    public String getGivenFeedbackToOrganizer() {
        return givenFeedbackToOrganizer;
    }

    public void setGivenFeedbackToOrganizer(String givenFeedbackToOrganizer) {
        this.givenFeedbackToOrganizer = givenFeedbackToOrganizer;
    }

    public int getRatingReceivedByOrganizer() {
        return ratingReceivedByOrganizer;
    }

    public void setRatingReceivedByOrganizer(int ratingReceivedByOrganizer) {
        this.ratingReceivedByOrganizer = ratingReceivedByOrganizer;
    }

    public String getFeedbackReceivedByOrganizer() {
        return feedbackReceivedByOrganizer;
    }

    public void setFeedbackReceivedByOrganizer(String feedbackReceivedByOrganizer) {
        this.feedbackReceivedByOrganizer = feedbackReceivedByOrganizer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityViewDTO that = (ActivityViewDTO) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getBegin(), that.getBegin()) && Objects.equals(getEnd(), that.getEnd()) && Objects.equals(getOrganizer(), that.getOrganizer()) && Objects.equals(getGivenRatingToOrganizer(), that.getGivenRatingToOrganizer()) && Objects.equals(getGivenFeedbackToOrganizer(), that.getGivenFeedbackToOrganizer()) && Objects.equals(getRatingReceivedByOrganizer(), that.getRatingReceivedByOrganizer()) && Objects.equals(getFeedbackReceivedByOrganizer(), that.getFeedbackReceivedByOrganizer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getBegin(), getEnd(), getOrganizer(), getGivenRatingToOrganizer(), getGivenFeedbackToOrganizer(), getRatingReceivedByOrganizer(), getFeedbackReceivedByOrganizer());
    }


    
    
}
