package utils;

import entities.User;

public class Session {
    private static final ThreadLocal<String> currentToken = new ThreadLocal<>(); // Thread-local storage for token
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>(); // Thread-local storage for user (backward compatibility)

    // Private constructor to prevent instantiation
    private Session() {
        throw new IllegalStateException("Utility class");
    }

    // Set the current token (login)
    public static void setCurrentToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        currentToken.set(token);
    }

    // Set the current user (backward compatibility)
    public static void setCurrentUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        currentUser.set(user);
    }

    // Get the current token
    public static String getCurrentToken() {
        String token = currentToken.get();
        if (token == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        return token;
    }

    // Get the current user (backward compatibility)
    public static User getCurrentUser() {
        User user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("No user is currently logged in");
        }
        return user;
    }

    // Log out the current user
    public static void logout() {
        currentToken.remove(); // Clear the token
        currentUser.remove(); // Clear the cached user
    }

    // Get the role of the current user
    public static String getRole() {
        String token = getCurrentToken();
        return TokenManager.getRoleFromToken(token);
    }

    // Get the email of the current user
    public static String getEmail() {
        String token = getCurrentToken();
        return TokenManager.getEmailFromToken(token);
    }

    // Check if a user is logged in
    public static boolean isLoggedIn() {
        try {
            String token = currentToken.get();
            return token != null && TokenManager.validateToken(token) != null;
        } catch (SecurityException e) {
            return false;
        }
    }
}