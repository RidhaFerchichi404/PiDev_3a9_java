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
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtCin;

    @FXML
    private Button btnSave;

    private User currentUser;
    private UserService userService = new UserService();  // Initialize the service

    public void setUser(User user) {
        this.currentUser = user;

        // Populate the form fields with the user's data
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtEmail.setText(user.getEmail());
        txtPhoneNumber.setText(user.getPhoneNumber());
        txtCin.setText(user.getCin()); // Set CIN (nullable)
    }


    @FXML
    public void saveChanges() {
        // Update the user object with the new data
        currentUser.setFirstName(txtFirstName.getText());
        currentUser.setLastName(txtLastName.getText());
        currentUser.setEmail(txtEmail.getText());
        currentUser.setPhoneNumber(txtPhoneNumber.getText());

        // CIN validation: Only allow CIN if age is 18 or older
        if (currentUser.getAge() >= 18) {
            currentUser.setCin(txtCin.getText());
        } else {
            currentUser.setCin(null); // Clear CIN if under 18
        }

        // Save the changes using the UserService
        try {
            userService.update(currentUser); // Save changes to the database
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to update user: " + e.getMessage());
        }

        // Close the modification window after saving
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}