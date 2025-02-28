package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UserService;
import entities.User;
import utils.Session;
import utils.TokenManager;

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

    private final UserService userService = new UserService();

    // Navigate to the registration form
    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
            stage.setScene(new Scene(loader.load())); // Load the registration form
        } catch (IOException e) {
            showAlert("Error", "Unable to load the registration form.");
            e.printStackTrace();
        }
    }

    // Handle login button click
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input fields
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Please enter both email and password.");
            return;
        }

        // Authenticate user
        User user = userService.authenticateUser(email, password);

        if (user != null) {
            // Generate a token for the authenticated user
            String token = TokenManager.generateToken(user);
            Session.setCurrentToken(token); // Set the token in the session
            Session.setCurrentUser(user);

            // Navigate to the appropriate dashboard based on the user's role
            navigateToDashboard(user.getRole());
        } else {
            errorMessage.setText("Invalid email or password.");
        }
    }

    // Navigate to the appropriate dashboard based on the user's role
    private void navigateToDashboard(String role) {
        try {
            String fxmlFile;
            String title;

            switch (role.toUpperCase()) {
                case "ADMIN":
                    fxmlFile = "/AdminDashboard.fxml";
                    title = "Admin Dashboard";
                    break;
                case "CLIENT":
                case "COACH":
                    fxmlFile = "/MainMenu.fxml";
                    title = "Main Menu";
                    break;
                default:
                    showAlert("Error", "Unrecognized user role.");
                    return;
            }

            // Load the FXML file and set the scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            showAlert("Error", "Unable to load the requested view.");
            e.printStackTrace();
        }
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}