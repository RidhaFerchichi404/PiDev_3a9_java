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
    private Runnable afterSaveAction;  // Déclare une action à exécuter après enregistrement

    public void setAfterSaveAction(Runnable action) {
        this.afterSaveAction = action;
    }


        private void initialize() {
            roleComboBox.getItems().addAll("Admin", "Coach", "Client"); // Ajout de l'option Admin
            roleComboBox.setValue("Client"); // Par défaut, sélectionne "Client"

    }


    private UserService userService = new UserService();

    private boolean validateInputs() {
        String phoneNumber = phoneNumberField.getText();
        String cin = cinField.getText();

        // Vérifier que phoneNumber est composé de 8 chiffres
        if (!phoneNumber.matches("\\d{8}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le numéro de téléphone doit comporter exactement 8 chiffres.");
            alert.showAndWait();
            return false;
        }

        // Vérifier que cin est composé de 8 chiffres
        if (!cin.matches("\\d{8}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le CIN doit comporter exactement 8 chiffres.");
            alert.showAndWait();
            return false;
        }

        return true;
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