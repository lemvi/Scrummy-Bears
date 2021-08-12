package academy.everyonecodes.java.data.ProfileDTOs;

import java.util.Objects;

public class ProfileSkillRatingDTO {
    private ProfileDTO profileDTO;
    private Skill skill;
    private Rating rating;

    public ProfileSkillRatingDTO(ProfileDTO profileDTO, Skill skill, Rating rating) {
        this.profileDTO = profileDTO;
        this.skill = skill;
        this.rating = rating;
    }

    public ProfileDTO getProfileDTO() {
        return profileDTO;
    }

    public void setProfileDTO(ProfileDTO profileDTO) {
        this.profileDTO = profileDTO;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileSkillRatingDTO that = (ProfileSkillRatingDTO) o;
        return Objects.equals(profileDTO, that.profileDTO) && Objects.equals(skill, that.skill) && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileDTO, skill, rating);
    }
}
