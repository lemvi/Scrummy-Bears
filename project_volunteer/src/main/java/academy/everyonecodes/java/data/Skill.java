package academy.everyonecodes.java.data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
public class Skill {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;
    @NotEmpty
    private String skill;

    public Skill() {
    }

    public Skill(User user, String skill) {
        this.user = user;
        this.skill = skill;
    }

    public Skill(Long id, User user, String skill) {
        this.id = id;
        this.user = user;
        this.skill = skill;
    }

    public Skill(String skill) {
        this.skill = skill;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skills) {
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill1 = (Skill) o;
        return Objects.equals(id, skill1.id) && Objects.equals(user, skill1.user) && Objects.equals(skill, skill1.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, skill);
    }
}
