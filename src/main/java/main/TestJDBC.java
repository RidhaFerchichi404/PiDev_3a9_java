package main;

import entities.User;
import services.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class TestJDBC {

    public static void main(String[] args) {
        UserService us = new UserService();
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for input
        System.out.println("Enter user details:");

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
        user.setCin(cin.isEmpty() ? null : cin); // Set CIN only if provided
        user.setPhoneNumber(phoneNumber);
        user.setRole(role);
        user.setLocation(location);

        // Insert the user into the database
        try {
            us.create(user);
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to add user: " + e.getMessage());
        }

        // Read all users from the database
        try {
            System.out.println("List of users:");
            us.readAll().forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Failed to fetch users: " + e.getMessage());
        }

        scanner.close();
    }
}