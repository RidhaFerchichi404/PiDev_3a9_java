package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text; // Correct import
import javafx.stage.Stage;
import services.UserService;
import entities.User;
import utils.Session;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorMessage;
    @FXML
    private Button submitButton;

    private UserService userService = new UserService();

    public void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Stage stage = (Stage)  submitButton.getScene().getWindow(); // Get current stage
            stage.setScene(new Scene(loader.load())); // Load the registration form
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        // Authenticate user
        String username = emailField.getText().trim();
        String password = passwordField.getText().trim();

        User user = userService.authenticateUser(username, password);

        if (user != null) {
            // Set the current session
            Session.setCurrentUser(user);

            // Load the Main Menu
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/MainMenu.fxml"));
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Main Menu");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Unable to load Main Menu.");
            }
        } else {
            // Show error message if authentication fails
            errorMessage.setText("Invalid username or password.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
