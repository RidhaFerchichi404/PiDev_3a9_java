package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.UserService;
import utils.LocalCaptcha;

import java.io.IOException;
import java.security.cert.PolicyNode;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterUserController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField cinField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button AddButton;
    @FXML
    private HBox captchaBox; // CAPTCHA container

    @FXML
    private Label captchaLabel; // Label to display the CAPTCHA text

    @FXML
    private TextField captchaInput; // Input field for the user to type the CAPTCHA

    @FXML
    private Button refreshCaptchaButton; // Button to refresh the CAPTCHA

    @FXML
    private Runnable afterSaveAction;
    private LocalCaptcha localCaptcha;


    public void setAfterSaveAction(Runnable action) {
        this.afterSaveAction = action;
    }

    @FXML
    public void initialize() {
        // Initialize the roleComboBox with only Client and Coach options
        roleComboBox.getItems().clear();
        roleComboBox.getItems().addAll("Client", "Coach");
        roleComboBox.setValue("Client"); // Set default value
        // Initialize the CAPTCHA
        localCaptcha = new LocalCaptcha();
        refreshCaptcha(); // Generate and display the initial CAPTCHA
    }

    private UserService userService = new UserService();
    @FXML
    private void refreshCaptcha() {
        String newCaptchaText = localCaptcha.getNewCaptchaText();
        captchaLabel.setText(newCaptchaText);
        captchaInput.clear(); // Clear the input field
    }

    private boolean validateInputs() {
        // Validate that all required fields are filled in
        if (isEmpty(firstNameField) || isEmpty(lastNameField) || isEmpty(emailField) || isEmpty(passwordField) ||
                isEmpty(ageField) || isEmpty(cinField) || isEmpty(phoneNumberField) || isEmpty(locationField)) {
            showError("Tous les champs doivent être remplis !");
            return false;
        }

        String phoneNumber = phoneNumberField.getText();
        String cin = cinField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validate phone number (Tunisia format)
        if (!isValidPhoneNumber(phoneNumber)) {
            showError("Le numéro de téléphone n'est pas valide. Il doit commencer par +216 ou 216 et comporter 8 chiffres.");
            return false;
        }

        // Validate CIN (must be 8 digits)
        if (!cin.matches("\\d{8}")) {
            showError("Le CIN doit comporter exactement 8 chiffres.");
            return false;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showError("L'adresse email n'est pas valide.");
            return false;
        }

        // Validate password strength
        if (!isValidPassword(password)) {
            showError("Le mot de passe doit avoir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
            return false;
        }

        // Validate age (positive integer)
        try {
            int age = Integer.parseInt(ageField.getText());
            if (age <= 0 || age > 120) {
                showError("L'âge doit être un nombre valide entre 1 et 120.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("L'âge doit être un nombre entier.");
            return false;
        }

        return true;
    }

    private boolean isEmpty(TextField field) {
        return field.getText().trim().isEmpty();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(\\+216|216)?\\d{8}$"; // Validates Tunisian phone numbers starting with +216 or 216 and 8 digits
        return phoneNumber.matches(regex);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        // Password must be at least 6 characters long, contain at least one letter (upper or lower case) and one number
        return true;
    }

    @FXML
    private void handleAddUser() {
        // Validate CAPTCHA before proceeding
        String userInput = captchaInput.getText();
        if (!localCaptcha.validateCaptcha(userInput)) {
            showError("Invalid CAPTCHA. Please try again.");
            refreshCaptcha(); // Regenerate CAPTCHA on failure
            return;
        }
        // Check input validity
        if (!validateInputs()) {
            return; // Exit if validation fails
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String passwordHash = passwordField.getText();
        int age = Integer.parseInt(ageField.getText());
        String cin = cinField.getText();
        String phoneNumber = phoneNumberField.getText();
        String location = locationField.getText();
        String role = roleComboBox.getValue(); // Get selected role

        // Create a user object
        User user = new User(firstName, lastName, email, passwordHash, age);
        user.setCin(cin.isEmpty() ? null : cin);
        user.setPhoneNumber(phoneNumber);
        user.setRole(role); // Use the selected role ("Coach" or "Client")
        user.setLocation(location);

        // Insert into the database
        try {
            userService.create(user);

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Utilisateur ajouté avec succès !");
            alert.showAndWait();

            // Navigate to login screen after user is added
            goToLoginScreen();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec de l'ajout de l'utilisateur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Method to navigate to the login screen
    private void goToLoginScreen() {
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent loginRoot = loader.load();

            // Get the current scene and switch to the login screen
            Stage currentStage = (Stage) AddButton.getScene().getWindow();
            currentStage.setScene(new Scene(loginRoot));
        } catch (IOException e) {
            e.printStackTrace(); // Handle any errors in loading the FXML
        }
    }

}
