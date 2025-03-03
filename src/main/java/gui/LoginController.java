package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UserService;
import entities.User;
import utils.Session;
import utils.TokenManager;

import java.io.IOException;
import java.util.Random;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorMessage;

    @FXML
    private Button submitButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    private final UserService userService = new UserService();
    private String otpCode;
    private String phoneNumber;
    public void initialize() {
        forgotPasswordLink.setOnAction(event -> handleForgotPassword());
    }


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

    // Mot de passe oublié - Envoi d'un OTP par SMS
    private void handleForgotPassword() {
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Mot de passe oublié");
        emailDialog.setHeaderText("Entrez votre adresse e-mail");
        emailDialog.setContentText("E-mail :");

        emailDialog.showAndWait().ifPresent(email -> {
            phoneNumber = userService.getUserPhoneNumberByEmail(email);

            if (phoneNumber == null || phoneNumber.isEmpty()) {
                showAlert("Erreur", "Aucun numéro de téléphone trouvé pour cet e-mail.");
                return;
            }

            otpCode = generateOtp();
            System.out.println("Envoi de l'OTP au numéro : " + phoneNumber);

            SmsService.sendSms(phoneNumber, "Votre code de réinitialisation est : " + otpCode);
            System.out.println("✅ SMS envoyé avec succès.");

            verifyOtp();
        });
    }

    // 🔹 Vérification de l'OTP et réinitialisation du mot de passe
    private void verifyOtp() {
        TextInputDialog otpDialog = new TextInputDialog();
        otpDialog.setTitle("Vérification OTP");
        otpDialog.setHeaderText("Entrez le code OTP reçu par SMS");
        otpDialog.setContentText("Code OTP :");

        otpDialog.showAndWait().ifPresent(enteredOtp -> {
            if (enteredOtp.equals(otpCode)) {
                resetPassword();
            } else {
                showAlert("Erreur", "Code OTP incorrect.");
            }
        });
    }

    // 🔹 Réinitialisation du mot de passe
    private void resetPassword() {
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Réinitialisation du mot de passe");
        passwordDialog.setHeaderText("Entrez votre nouveau mot de passe");
        passwordDialog.setContentText("Nouveau mot de passe :");

        passwordDialog.showAndWait().ifPresent(newPassword -> {
            if (newPassword.length() < 6) {
                showAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères.");
                return;
            }

            userService.updatePasswordByPhone(phoneNumber, newPassword);
            showAlert("Succès", "Votre mot de passe a été réinitialisé !");
        });
    }

    // 🔹 Génération d'un code OTP à 6 chiffres
    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
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