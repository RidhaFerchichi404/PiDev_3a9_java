package main;

import entities.User;
import services.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TestJDBC {

    private static UserService userService = new UserService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- User Management System ---");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. View All Users");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    viewAllUsers();
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void addUser() {
        System.out.println("\n--- Add User ---");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password Hash: ");
        String passwordHash = scanner.nextLine();

        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("CIN (leave blank if under 18): ");
        String cin = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Role (default is USER): ");
        String role = scanner.nextLine();
        if (role.isEmpty()) {
            role = "USER"; // Default role
        }

        System.out.print("Location: ");
        String location = scanner.nextLine();

        // Create a new user object
        User user = new User(firstName, lastName, email, passwordHash, age);
        user.setCin(cin.isEmpty() ? null : cin);
        user.setPhoneNumber(phoneNumber);
        user.setRole(role);
        user.setLocation(location);

        // Insert the user into the database
        try {
            userService.create(user);
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to add user: " + e.getMessage());
        }
    }

    private static void updateUser() {
        System.out.println("\n--- Update User ---");

        System.out.print("Enter the ID of the user to update: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Consume the newline character

        try {
            // Fetch the user by ID
            User user = userService.findById(id);
            if (user == null) {
                System.out.println("User not found with ID: " + id);
                return;
            }

            System.out.println("Current User Details:");
            System.out.println(user);

            // Prompt for updated details
            System.out.print("New First Name (leave blank to keep current): ");
            String firstName = scanner.nextLine();
            if (!firstName.isEmpty()) {
                user.setFirstName(firstName);
            }

            System.out.print("New Last Name (leave blank to keep current): ");
            String lastName = scanner.nextLine();
            if (!lastName.isEmpty()) {
                user.setLastName(lastName);
            }

            System.out.print("New Email (leave blank to keep current): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                user.setEmail(email);
            }

            System.out.print("New Password Hash (leave blank to keep current): ");
            String passwordHash = scanner.nextLine();
            if (!passwordHash.isEmpty()) {
                user.setPasswordHash(passwordHash);
            }

            System.out.print("New Age (leave blank to keep current): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.isEmpty()) {
                user.setAge(Integer.parseInt(ageInput));
            }

            System.out.print("New CIN (leave blank to keep current): ");
            String cin = scanner.nextLine();
            if (!cin.isEmpty()) {
                user.setCin(cin);
            }

            System.out.print("New Phone Number (leave blank to keep current): ");
            String phoneNumber = scanner.nextLine();
            if (!phoneNumber.isEmpty()) {
                user.setPhoneNumber(phoneNumber);
            }

            System.out.print("New Role (leave blank to keep current): ");
            String role = scanner.nextLine();
            if (!role.isEmpty()) {
                user.setRole(role);
            }

            System.out.print("New Location (leave blank to keep current): ");
            String location = scanner.nextLine();
            if (!location.isEmpty()) {
                user.setLocation(location);
            }

            // Update the user in the database
            userService.update(user);
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to update user: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.println("\n--- Delete User ---");

        System.out.print("Enter the ID of the user to delete: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Consume the newline character

        try {
            // Fetch the user by ID
            User user = userService.findById(id);
            if (user == null) {
                System.out.println("User not found with ID: " + id);
                return;
            }

            System.out.println("User to Delete:");
            System.out.println(user);

            System.out.print("Are you sure you want to delete this user? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                userService.delete(user);
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Deletion canceled.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to delete user: " + e.getMessage());
        }
    }

    private static void viewAllUsers() {
        System.out.println("\n--- All Users ---");

        try {
            List<User> users = userService.readAll();
            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                for (User user : users) {
                    System.out.println(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch users: " + e.getMessage());
        }
    }
}