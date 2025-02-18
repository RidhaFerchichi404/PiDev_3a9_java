package services;

import entities.User;
import utils.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        String query = "UPDATE user SET first_name = ?, last_name = ?, email = ?, password_hash = ?, phone_number = ?, role = ?, subscription_end_date = ?, is_active = ?, updated_at = ?, violation_count = ?, location = ?, cin = ?, age = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPasswordHash());
        ps.setString(5, user.getPhoneNumber());
        ps.setString(6, user.getRole());
        ps.setDate(7, user.getSubscriptionEndDate() != null ? Date.valueOf(user.getSubscriptionEndDate()) : null);
        ps.setBoolean(8, user.isActive());
        ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now())); // updated_at
        ps.setInt(10, user.getViolationCount());
        ps.setString(11, user.getLocation());
        ps.setString(12, user.getCin());
        ps.setInt(13, user.getAge());
        ps.setLong(14, user.getId());
        ps.executeUpdate();
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
    }



}