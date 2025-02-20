package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;
import utils.Session;

import java.sql.SQLException;

public class ClientModifyController {

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

    private UserService userService = new UserService();  // Initialize the service
    private User currentUser;

    @FXML
    public void initialize() {
        // Fetch the logged-in user from the session
        currentUser = Session.getCurrentUser();

        if (currentUser == null) {
            System.err.println("No current user found in session.");
            return;
        }

        // Populate fields with the current user's data
        txtFirstName.setText(currentUser.getFirstName());
        txtLastName.setText(currentUser.getLastName());
        txtEmail.setText(currentUser.getEmail());
        txtPhoneNumber.setText(currentUser.getPhoneNumber());
        txtCin.setText(currentUser.getCin());
    }

    @FXML
    public void saveChanges() {
        if (currentUser == null) {
            System.err.println("No user is set for modification.");
            return;
        }

        // Update the user object with the new data
        currentUser.setFirstName(txtFirstName.getText());
        currentUser.setLastName(txtLastName.getText());
        currentUser.setEmail(txtEmail.getText());
        currentUser.setPhoneNumber(txtPhoneNumber.getText());
        currentUser.setCin(txtCin.getText());


        // Save the changes using the UserService
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

