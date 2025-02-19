package services;

import entities.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import utils.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements IService<User> {

    private Connection cnx;

    public UserService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(User user) throws SQLException {
        String query = "INSERT INTO user (first_name, last_name, email, password_hash, phone_number, role, subscription_end_date, is_active, created_at, updated_at, violation_count, location, cin, age) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPasswordHash());
        ps.setString(5, user.getPhoneNumber());
        ps.setString(6, user.getRole());
        ps.setDate(7, user.getSubscriptionEndDate() != null ? Date.valueOf(user.getSubscriptionEndDate()) : null);
        ps.setBoolean(8, user.isActive());
        ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now())); // created_at
        ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now())); // updated_at
        ps.setInt(11, user.getViolationCount());
        ps.setString(12, user.getLocation());
        ps.setString(13, user.getCin());
        ps.setInt(14, user.getAge());
        ps.executeUpdate();
    }

    @Override
    public void update(User user) throws SQLException {
        // Start building the query dynamically
        StringBuilder query = new StringBuilder("UPDATE user SET ");
        List<Object> params = new ArrayList<>();

        // Check and add fields to update
        if (user.getFirstName() != null) {
            query.append("first_name = ?, ");
            params.add(user.getFirstName());
        }
        if (user.getLastName() != null) {
            query.append("last_name = ?, ");
            params.add(user.getLastName());
        }
        if (user.getEmail() != null) {
            query.append("email = ?, ");
            params.add(user.getEmail());
        }
        if (user.getPhoneNumber() != null) {
            query.append("phone_number = ?, ");
            params.add(user.getPhoneNumber());
        }
        if (user.getCin() != null) {
            query.append("cin = ?, ");
            params.add(user.getCin());
        }

        // Add the updated_at field to keep track of the modification date
        query.append("updated_at = ? ");
        params.add(Timestamp.valueOf(LocalDateTime.now()));

        // Add the WHERE clause
        query.append("WHERE id = ?");
        params.add(user.getId());

        // Prepare and execute the statement
        try (PreparedStatement ps = cnx.prepareStatement(query.toString())) {
            // Set the parameters dynamically
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            // Execute the update
            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        }
    }


    @Override
    public void delete(User user) throws SQLException {
        String query = "DELETE FROM user WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setLong(1, user.getId());
        ps.executeUpdate();
    }

    /*@Override
    public List<User> readAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setRole(rs.getString("role"));
            user.setSubscriptionEndDate(rs.getDate("subscription_end_date") != null ? rs.getDate("subscription_end_date").toLocalDate() : null);
            user.setActive(rs.getBoolean("is_active"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            user.setViolationCount(rs.getInt("violation_count"));
            user.setLocation(rs.getString("location"));
            user.setCin(rs.getString("cin"));
            user.setAge(rs.getInt("age"));
            users.add(user);
        }

        return users;
    }*/

    // Optional: Find a user by ID
    public User findById(long id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setRole(rs.getString("role"));
            user.setSubscriptionEndDate(rs.getDate("subscription_end_date") != null ? rs.getDate("subscription_end_date").toLocalDate() : null);
            user.setActive(rs.getBoolean("is_active"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            user.setViolationCount(rs.getInt("violation_count"));
            user.setLocation(rs.getString("location"));
            user.setCin(rs.getString("cin"));
            user.setAge(rs.getInt("age"));
            return user;
        }

        return null;
    }
    public List<User> readAll() {
        List<User> users = new ArrayList<>();

        // Adjust the query to match your database table (users is likely the correct table name)
        String query = "SELECT * FROM user"; // If the table name is "user", use it

        try (PreparedStatement ps = cnx.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id")); // Correct method to get the "id"
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setRole(rs.getString("role"));
                user.setAge(rs.getInt("age"));
                user.setCin(rs.getString("cin"));

                // Add the user to the list
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
            throw new RuntimeException("Unable to load users from the database.");
        }

        return users;
    }



    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setRole(rs.getString("role"));
                user.setSubscriptionEndDate(rs.getDate("subscription_end_date") != null ? rs.getDate("subscription_end_date").toLocalDate() : null);
                user.setActive(rs.getBoolean("is_active"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                user.setViolationCount(rs.getInt("violation_count"));
                user.setLocation(rs.getString("location"));
                user.setCin(rs.getString("cin"));
                user.setAge(rs.getInt("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public void deleteUser(User user) {
        // Show confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete the user?");
        confirmationAlert.setContentText("This action cannot be undone.");

        // Wait for the user's response
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String query = "DELETE FROM user WHERE id = ?";
            try (PreparedStatement ps = cnx.prepareStatement(query)) {
                ps.setLong(1, user.getId()); // Use user.getId() to set the parameter
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User with ID " + user.getId() + " deleted successfully.");
                } else {
                    System.out.println("No user found with ID " + user.getId());
                }
            } catch (SQLException e) {
                System.err.println("Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("User deletion canceled.");
        }
    }
    public User authenticateUser(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password_hash = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                // ...set other fields if needed
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }





}