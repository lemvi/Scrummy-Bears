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
    private static String levelUpMessage;
    private static LevelRepository levelRepository;

    public LevelService(@Value("${level.levelUpMessage}") String levelUpMessage, LevelRepository levelRepository)
    {
        LevelService.levelUpMessage = levelUpMessage;
        LevelService.levelRepository = levelRepository;
    }

    public static void gainXp(User user, int xpGained)
    {
        Level level = getCurrentUserLevel(user);

        int accumulatedXp = level.getCurrentXp() + xpGained;
        level.setCurrentXp(0);

        if (accumulatedXp < level.getXpForLevelUpNeeded())
        {
            System.out.println("HALLO! ER HAT ES BIS HIERHIN GESCHAFFT!");
            System.out.println(accumulatedXp);
            level.setCurrentXp(accumulatedXp);
            levelRepository.save(level);
            System.out.println("HALLO! ER HAT ES BIS HIERHIN GESCHAFFT NACH DEN SETTER!");
        } else
        {
            levelUp(level, accumulatedXp);
        }
    }

    private static void levelUp(Level level, int accumulatedXp)
    {
        level.setLevelValue(level.getLevelValue() + 1);
        int leftOverXp = accumulatedXp - level.getXpForLevelUpNeeded();
        level.setXpForLevelUpNeeded((int) ((double) level.getXpForLevelUpNeeded() * 1.5));
        System.out.println(levelUpMessage + level.getLevelValue());
        levelRepository.save(level);
        if (leftOverXp > 0)
        {
            gainXp(level.getUser(), leftOverXp);
        }
    }

    private static Level getCurrentUserLevel(User user)
    {
        Level level = new Level();
        Optional<Level> oLevel = levelRepository.findByUser(user);

        level = oLevel.isPresent() ? level = oLevel.get() : new Level(user);
        level.setId(user.getId());

        return level;
    }


}
