package academy.everyonecodes.java.data;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Badges
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @org.springframework.data.annotation.Transient
    private User user;

    @Enumerated(EnumType.STRING)
    private BadgesEnum badgesEnum;

    public Badges()
    {
    }

    public Badges(User user)
    {
        this.user = user;
    }

    public Badges(User user, BadgesEnum badgesEnum)
    {
        this.user = user;
        this.badgesEnum = badgesEnum;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public BadgesEnum getCurrentBadges()
    {
        return badgesEnum;
    }

    public void setCurrentBadges(BadgesEnum badgesEnum)
    {
        this.badgesEnum = badgesEnum;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Badges badges = (Badges) o;
        return Objects.equals(id, badges.id) && Objects.equals(user, badges.user) && Objects.equals(this.badgesEnum, badges.badgesEnum);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, user, badgesEnum);
    }

    @Override
    public String toString()
    {
        return "Badges{" +
                "id=" + id +
                ", user=" + user +
                ", badgesEnum=" + badgesEnum +
                '}';
    }
}
