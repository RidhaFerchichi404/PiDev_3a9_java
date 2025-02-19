package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {

    @FXML private Button btnAddUser;
    @FXML private Button btnViewCards;

    @FXML
    private void handleAddUser() throws IOException {
        // Load the "Add User" view
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AjouterUser.fxml")));
        Stage stage = (Stage) btnAddUser.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Ajout des Utilisateurs");
    }

    @FXML
    private void handleViewCards() {
        try {
            // Load the "User List" view
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UserList.fxml")));
            Stage stage = (Stage) btnViewCards.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Utilisateurs");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la vue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
