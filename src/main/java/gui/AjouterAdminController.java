package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterAdminController {

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
    private Runnable afterSaveAction;  // Declare an action to be executed after saving

    public void setAfterSaveAction(Runnable action) {
        this.afterSaveAction = action;
    }

    @FXML
    public void initialize() {
        // Initialize the roleComboBox with options for Client, Coach, Admin
        roleComboBox.getItems().clear(); // Clear any existing items
        roleComboBox.getItems().addAll("Client", "Coach", "Admin");
        roleComboBox.setValue("Client"); // Set default value
    }

    private UserService userService = new UserService();

    private boolean validateInputs() {
        String phoneNumber = phoneNumberField.getText();
        String cin = cinField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String age = ageField.getText();
        String location = locationField.getText();

        // Check if any required fields are empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                age.isEmpty() || cin.isEmpty() || phoneNumber.isEmpty() || location.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis.");
            return false;
        }

        // Validate phone number (8 digits, Tunisian format)
        if (!phoneNumber.matches("^\\d{8}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le numéro de téléphone doit comporter exactement 8 chiffres.");
            return false;
        }

        // Validate CIN (8 digits, Tunisian format)
        if (!cin.matches("^\\d{8}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le CIN doit comporter exactement 8 chiffres.");
            return false;
        }

        // Validate email format
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'email n'est pas valide.");
            return false;
        }

        // Validate password strength
        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le mot de passe doit contenir au moins 6 caractères, avec des lettres et des chiffres.");
            return false;
        }

        // Validate age (should be a number)
        try {
            int ageInt = Integer.parseInt(age);
            if (ageInt < 18) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'âge doit être supérieur ou égal à 18.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'âge doit être un nombre valide.");
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password) {
       return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAddUser() {
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
        String role = roleComboBox.getValue();

        // Create user object
        User user = new User(firstName, lastName, email, passwordHash, age);
        user.setCin(cin.isEmpty() ? null : cin);
        user.setPhoneNumber(phoneNumber);
        user.setRole(role);
        user.setLocation(location);

        // Insert into database
        try {
            userService.create(user);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès !");

            // Close the current window
            Stage stage = (Stage) AddButton.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }
}
