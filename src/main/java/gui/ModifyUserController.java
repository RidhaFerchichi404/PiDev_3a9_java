package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;

import java.sql.SQLException;

public class ModifyUserController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSave;

    private User currentUser;
    private UserService userService = new UserService();  // Initialize the service

    public void setUser(User user) {
        this.currentUser = user;
        txtName.setText(user.getFirstName());  // Populate fields with user data
        txtEmail.setText(user.getEmail());
    }

    @FXML
    public void saveChanges() {
        if (currentUser == null) {
            System.err.println("No user is set for modification.");
            return;
        }

        // Update the user object with the new data
        currentUser.setFirstName(txtName.getText());
        currentUser.setEmail(txtEmail.getText());

        try {
            userService.update(currentUser);  // Save changes to the database
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to update user: " + e.getMessage());
        }

        // Close the modification window after saving
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

}
