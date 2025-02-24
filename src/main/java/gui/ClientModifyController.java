package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;
import utils.Session;

import java.sql.SQLException;
import java.util.regex.Pattern;

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
            showAlert(Alert.AlertType.ERROR, "Error", "No user found in session.");
            return;
        }

        // Debug: Print current user data
        System.out.println("Current User: " + currentUser);

        // Populate fields with the current user's data
        txtFirstName.setText(currentUser.getFirstName());
        txtLastName.setText(currentUser.getLastName());
        txtEmail.setText(currentUser.getEmail());
        txtPhoneNumber.setText(currentUser.getPhoneNumber());
        txtCin.setText(currentUser.getCin());

        // Debug: Print field values
        System.out.println("First Name: " + txtFirstName.getText());
        System.out.println("Last Name: " + txtLastName.getText());
        System.out.println("Email: " + txtEmail.getText());
        System.out.println("Phone Number: " + txtPhoneNumber.getText());
        System.out.println("CIN: " + txtCin.getText());

        // Disable CIN field if the user is under 18
        if (currentUser.getAge() < 18) {
            txtCin.setDisable(true);
        }
    }

    @FXML
    public void saveChanges() {
        if (currentUser == null) {
            System.err.println("No user is set for modification.");
            showAlert(Alert.AlertType.ERROR, "Error", "No user is set for modification.");
            return;
        }

        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        // Update the user object with the new data
        currentUser.setFirstName(txtFirstName.getText());
        currentUser.setLastName(txtLastName.getText());
        currentUser.setEmail(txtEmail.getText());
        currentUser.setPhoneNumber(txtPhoneNumber.getText());

        // Only update CIN if the user is 18 or older
        if (currentUser.getAge() >= 18) {
            currentUser.setCin(txtCin.getText());
        }

        // Save the changes using the UserService
        try {
            userService.update(currentUser);  // Save changes to the database
            showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to update user: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user: " + e.getMessage());
        }

        // Close the modification window after saving
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    private boolean validateInputs() {
        // Validate email format
        if (!isValidEmail(txtEmail.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        // Validate phone number format (optional)
        if (!isValidPhoneNumber(txtPhoneNumber.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Please enter a valid phone number.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Simple validation: Check if the phone number contains only digits and is 8-15 characters long
        String phoneRegex = "^\\d{8,15}$";
        return Pattern.matches(phoneRegex, phoneNumber);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}