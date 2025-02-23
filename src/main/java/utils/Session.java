package utils;

import entities.User;

public class Session {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static String getRole() {
        return currentUser != null ? currentUser.getRole() : null; // Return the role of the current user
    }
}
