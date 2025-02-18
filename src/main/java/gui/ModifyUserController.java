package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;

import java.sql.SQLException;

public class ModifyUserController {

    @FXML
    private TextField txtName; // Example field for user name
    @FXML
    private TextField txtEmail; // Example field for user email

    private User currentUser; // The user being modified
    private UserService userService;
    // Method to set the selected user's data
    public void setUser(User user) {

        this.currentUser = user;
        txtName.setText(user.getFirstName()); // Populate name
        txtEmail.setText(user.getEmail()); // Populate email
        // Populate other fields as needed
    }

    // Method to save changes
    public void saveChanges() {
        // Update the user object with modified values
        currentUser.setFirstName(txtName.getText());
        currentUser.setEmail(txtEmail.getText());

        // Try saving the updated user
        try {
            userService.update(currentUser); // Ensure this method is implemented in your service
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to update user: " + e.getMessage());
            e.printStackTrace(); // Optional: for debugging
        }

        // Close the window after saving
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

}
