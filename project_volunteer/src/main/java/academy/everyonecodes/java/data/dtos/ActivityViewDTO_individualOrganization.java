package academy.everyonecodes.java.data.dtos;

import academy.everyonecodes.java.data.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class ActivityViewDTO_individualOrganization {
    private String title;
    private Status status;
    private LocalDateTime begin;
    private LocalDateTime end;
    private boolean isOpenEnd;
    private VolunteerViewForActivityViewDTO_individualOrganization volunteer;
    private int givenRatingToVolunteer;
    private String givenFeedbackToVolunteer;
    private int ratingReceivedByVolunteer;
    private String feedbackReceivedByVolunteer;

    public ActivityViewDTO_individualOrganization(String title,
                                                  Status status,
                                                  LocalDateTime begin,
                                                  LocalDateTime end,
                                                  boolean isOpenEnd, VolunteerViewForActivityViewDTO_individualOrganization volunteer,
                                                  int givenRatingToVolunteer,
                                                  String givenFeedbackToVolunteer,
                                                  int ratingReceivedByVolunteer,
                                                  String feedbackReceivedByVolunteer) {
        this.title = title;
        this.status = status;
        this.begin = begin;
        this.end = end;
        this.isOpenEnd = isOpenEnd;
        this.volunteer = volunteer;
        this.givenRatingToVolunteer = givenRatingToVolunteer;
        this.givenFeedbackToVolunteer = givenFeedbackToVolunteer;
        this.ratingReceivedByVolunteer = ratingReceivedByVolunteer;
        this.feedbackReceivedByVolunteer = feedbackReceivedByVolunteer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        return isOpenEnd;
    }

    public void setOpenEnd(boolean openEnd) {
        isOpenEnd = openEnd;
    }

    public VolunteerViewForActivityViewDTO_individualOrganization getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(VolunteerViewForActivityViewDTO_individualOrganization volunteer) {
        this.volunteer = volunteer;
    }

    public int getGivenRatingToVolunteer() {
        return givenRatingToVolunteer;
    }

    public void setGivenRatingToVolunteer(int givenRatingToVolunteer) {
        this.givenRatingToVolunteer = givenRatingToVolunteer;
    }

    public String getGivenFeedbackToVolunteer() {
        return givenFeedbackToVolunteer;
    }

    public void setGivenFeedbackToVolunteer(String givenFeedbackToVolunteer) {
        this.givenFeedbackToVolunteer = givenFeedbackToVolunteer;
    }

    public int getRatingReceivedByVolunteer() {
        return ratingReceivedByVolunteer;
    }

    public void setRatingReceivedByVolunteer(int ratingReceivedByVolunteer) {
        this.ratingReceivedByVolunteer = ratingReceivedByVolunteer;
    }

    public String getFeedbackReceivedByVolunteer() {
        return feedbackReceivedByVolunteer;
    }

    public void setFeedbackReceivedByVolunteer(String feedbackReceivedByVolunteer) {
        this.feedbackReceivedByVolunteer = feedbackReceivedByVolunteer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityViewDTO_individualOrganization that = (ActivityViewDTO_individualOrganization) o;
        return getGivenRatingToVolunteer() == that.getGivenRatingToVolunteer() && getRatingReceivedByVolunteer() == that.getRatingReceivedByVolunteer() && Objects.equals(getTitle(), that.getTitle()) && getStatus() == that.getStatus() && Objects.equals(getBegin(), that.getBegin()) && Objects.equals(getEnd(), that.getEnd()) && Objects.equals(getVolunteer(), that.getVolunteer()) && Objects.equals(getGivenFeedbackToVolunteer(), that.getGivenFeedbackToVolunteer()) && Objects.equals(getFeedbackReceivedByVolunteer(), that.getFeedbackReceivedByVolunteer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getStatus(), getBegin(), getEnd(), getVolunteer(), getGivenRatingToVolunteer(), getGivenFeedbackToVolunteer(), getRatingReceivedByVolunteer(), getFeedbackReceivedByVolunteer());
    }
}
