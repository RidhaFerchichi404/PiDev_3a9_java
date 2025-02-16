package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import services.UserService;

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
    private void initialize() {
        roleComboBox.getItems().addAll("Coach", "Client");
        roleComboBox.setValue("Client"); // valeur par défaut, par exemple
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
        // Vérifier la validité des entrées
        if (!validateInputs()) {
            return; // Sortir si la validation échoue
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String passwordHash = passwordField.getText();
        int age = Integer.parseInt(ageField.getText());
        String cin = cinField.getText();
        String phoneNumber = phoneNumberField.getText();
        String location = locationField.getText();
        String role = roleComboBox.getValue(); // Récupération du rôle sélectionné dans le ComboBox

        // Créer un objet utilisateur
        User user = new User(firstName, lastName, email, passwordHash, age);
        user.setCin(cin.isEmpty() ? null : cin);
        user.setPhoneNumber(phoneNumber);
        user.setRole(role); // Utilisation du rôle choisi ("Coach" ou "Client")
        user.setLocation(location);

        // Insertion dans la base de données
        try {
            userService.create(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Utilisateur ajouté avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec de l'ajout de l'utilisateur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


}