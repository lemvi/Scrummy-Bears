package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Level;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.LevelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LevelService
{
    private final String levelUpMessage;
    private final LevelRepository levelRepository;

    public LevelService(@Value("${level.levelUpMessage}") String levelUpMessage, LevelRepository levelRepository)
    {
        this.levelUpMessage = levelUpMessage;
        this.levelRepository = levelRepository;
    }
    public void gainXp(User user, int xpGained)
    {
        Level level = new Level();
        Optional<Level> oLevel = levelRepository.findByUser(user);

        level = oLevel.isPresent() ? level = oLevel.get() : new Level(user, 1);
        level.setId(user.getId());

        int currentXp = level.getCurrentXp();
        int accumulatedXp = currentXp + xpGained;
        if (accumulatedXp < level.getXpForLevelUpNeeded())
            level.setCurrentXp(accumulatedXp);
        else
        {
            level.setCurrentXp(xpGained - (level.getXpForLevelUpNeeded() - currentXp));
            levelUp(level);
        }

        levelRepository.save(level);
    }

    private void levelUp(Level level)
    {
        level.setLevelValue(level.getLevelValue() + 1);
        level.setXpForLevelUpNeeded((int) ((double) level.getXpForLevelUpNeeded() * 1.5));
        System.out.println(levelUpMessage + level.getLevelValue());
        if (0 < level.getCurrentXp())
            gainXp(level.getUser(), level.getCurrentXp());
    }


}
