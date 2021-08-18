//package academy.everyonecodes.java.service;
//
//import academy.everyonecodes.java.data.Activity;
//import academy.everyonecodes.java.data.Role;
//import academy.everyonecodes.java.data.User;
//
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class JsonParser
//{
//    private String createJson(Activity activity)
//    {
//        return "{" +
//                "\"title\": \"" + activity.getTitle() + "\"," +
//                "\"description\": \"" + activity.getDescription() + "\"," +
//                "\"recommendedSkills\": \"" + activity.getRecommendedSkills() + "\"," +
//                "\"categories\": " + createJsonPartForCategories(activity.getCategories()) + "," +
//                "\"startDateTime\": \"" + activity.getStartDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\"," +
//                "\"endDateTime\": \"" + activity.getEndDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\"," +
//                "\"openEnd\": \"" + activity.isOpenEnd() + "\"," +
//                "\"organizer\": " + createJsonPartForOneUser(activity.getOrganizer()) + "," +
//                "\"applicants\": " + createJsonPartForUsers(activity.getApplicants()) + "," +
//                "\"participants\": " + createJsonPartForUsers(activity.getParticipants()) +
//                "}";
//    }
//
//    private String createJsonPartForUsers(Set<User> users)
//    {
//        return "[" +
//                users.stream()
//                        .map(this::createJsonPartForOneUser)
//                        .collect(Collectors.joining(","))
//                + "]";
//    }
//
//    private String createJsonPartForOneUser(User user)
//    {
//        return "{" +
//                "\"id\": " + user.getId() + "," +
//                "\"username\": \"" + user.getUsername() + "\"," +
//                "\"password\": \"" + user.getPassword() + "\"," +
//                "\"email\": \"" + user.getEmailAddress() + "\"," +
//                "\"roles\": " + createJsonPartForRoles(user.getRoles()) +
//                "}";
//    }
//
//    private String createJsonPartForRoles(Set<Role> roles)
//    {
//        return "[" +
//                roles.stream()
//                        .map(this::createJsonPartForOneRole)
//                        .collect(Collectors.joining(","))
//                + "]";
//    }
//
//    private String createJsonPartForOneRole(Role role)
//    {
//        return "{" +
//                "\"id\": " + role.getId() + "," +
//                "\"role\": \"" + role.getRole() +
//                "\"}";
//    }
//
//    private String createJsonPartForCategories(List<String> categories)
//    {
//        return "[" +
//                categories.stream()
//                        .map(category -> category = "\"" + category + "\"")
//                        .collect(Collectors.joining(","))
//                + "]";
//    }
//}
