package academy.everyonecodes.java.data;

import javax.persistence.*;
import java.util.Objects;
@Entity
public class Level
{
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    private int levelValue;
    private int currentXp;
    private int xpForLevelUpNeeded;

    public Level()
    {
    }

    public Level(User user, int levelValue)
    {
        this.user = user;
        this.levelValue = levelValue;
        this.currentXp = 0;
        this.xpForLevelUpNeeded = (int)(500 * 1.5 * levelValue);
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

    public int getLevelValue()
    {
        return levelValue;
    }

    public void setLevelValue(int levelValue)
    {
        this.levelValue = levelValue;
    }

    public int getCurrentXp()
    {
        return currentXp;
    }

    public void setCurrentXp(int currentXp)
    {
        this.currentXp = currentXp;
    }

    public int getXpForLevelUpNeeded()
    {
        return xpForLevelUpNeeded;
    }

    public void setXpForLevelUpNeeded(int xpForLevelUpNeeded)
    {
        this.xpForLevelUpNeeded = xpForLevelUpNeeded;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return levelValue == level.levelValue && currentXp == level.currentXp && xpForLevelUpNeeded == level.xpForLevelUpNeeded && Objects.equals(id, level.id) && Objects.equals(user, level.user);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, user, levelValue, currentXp, xpForLevelUpNeeded);
    }

    @Override
    public String toString()
    {
        return "Level{" +
                "id=" + id +
                ", user=" + user +
                ", levelValue=" + levelValue +
                ", currentXp=" + currentXp +
                ", xpForLevelUpNeeded=" + xpForLevelUpNeeded +
                '}';
    }
}
