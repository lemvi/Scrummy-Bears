package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Badges;
import academy.everyonecodes.java.data.BadgesEnum;
import academy.everyonecodes.java.data.Level;
import academy.everyonecodes.java.data.User;
import academy.everyonecodes.java.data.repositories.BadgesRepository;
import academy.everyonecodes.java.data.repositories.LevelRepository;
import academy.everyonecodes.java.data.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BadgesService
{
    private static BadgesRepository badgeRepository;
    private static RatingRepository ratingRepository;
    private static LevelRepository levelRepository;
    private static String baseMessage;
    private static String firstActivity;
    private static String fiveActivities;
    private static String tenActivities;
    private static String fiftyActivities;
    private static String hundredActivities;
    private static String thousandActivities;
    private static String level5;
    private static String level10;
    private static String level50;
    private static String level100;

    public BadgesService(
            BadgesRepository badgeRepository,
            RatingRepository ratingRepository,
            LevelRepository levelRepository,
            @Value("${badges.baseMessage}") String baseMessage,
            @Value("${badges.firstActivity}") String firstActivity,
            @Value("${badges.fiveActivities}") String fiveActivities,
            @Value("${badges.tenActivities}") String tenActivities,
            @Value("${badges.fiftyActivities}") String fiftyActivities,
            @Value("${badges.hundredActivities}") String hundredActivities,
            @Value("${badges.thousandActivities}") String thousandActivities,
            @Value("${badges.level5}") String level5,
            @Value("${badges.level10}") String level10,
            @Value("${badges.level50}") String level50,
            @Value("${badges.level100}") String level100)
    {
        BadgesService.badgeRepository = badgeRepository;
        BadgesService.ratingRepository = ratingRepository;
        BadgesService.levelRepository = levelRepository;
        BadgesService.baseMessage = baseMessage;
        BadgesService.firstActivity = firstActivity;
        BadgesService.fiveActivities = fiveActivities;
        BadgesService.tenActivities = tenActivities;
        BadgesService.fiftyActivities = fiftyActivities;
        BadgesService.hundredActivities = hundredActivities;
        BadgesService.thousandActivities = thousandActivities;
        BadgesService.level5 = level5;
        BadgesService.level10 = level10;
        BadgesService.level50 = level50;
        BadgesService.level100 = level100;
    }

    public static void updateBadges(User user)
    {
        updateActivitiesCompletedBadges(user);
        updateLevelBadges(user);
    }

    private static void updateActivitiesCompletedBadges(User user)
    {
        int completedActivities = ratingRepository.findByUser_Id(user.getId()).size();
        switch (completedActivities)
        {
            case 1:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.FIRST_ACTIVITY_COMPLETED, firstActivity);
                break;
            case 5:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.FIVE_ACTIVITIES_COMPLETED, fiveActivities);
                break;
            case 10:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.TEN_ACTIVITIES_COMPLETED, tenActivities);
                break;
            case 50:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.FIFTY_ACTIVITIES_COMPLETED, fiftyActivities);
                break;
            case 100:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.HUNDRED_ACTIVITIES_COMPLETED, hundredActivities);
                break;
            case 1000:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.THOUSAND_ACTIVITIES_COMPLETED, thousandActivities);
                break;
        }
    }

    private static void updateLevelBadges(User user)
    {
        Optional<Level> oCurrentLevel = levelRepository.findByUser(user);
        int levelValue = oCurrentLevel.map(Level::getLevelValue).orElse(0);

        switch (levelValue)
        {
            case 5:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.LEVEL_5_REACHED, level5);
                break;
            case 10:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.LEVEL_10_REACHED, level10);
                break;
            case 50:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.LEVEL_50_REACHED, level50);
                break;
            case 100:
                saveNewBadgesAndPrintMessage(user, BadgesEnum.LEVEL_100_REACHED, level100);
                break;
        }
    }

    private static void saveNewBadgesAndPrintMessage(User user, BadgesEnum badgeEnum, String badgesMessage)
    {
        Badges badges = new Badges(user, badgeEnum);
        badgeRepository.save(badges);

        System.out.println(baseMessage + badgesMessage);
    }
}
