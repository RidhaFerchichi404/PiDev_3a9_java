package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.Session;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button btnAddUser;
    @FXML
    private Button btnViewCards;
    @FXML
    private Button btnLogout; // Optional logout button

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Check if there's a logged-in user
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            // If no user is logged in, redirect to the login screen
            redirectToLogin();
        } else {
            System.out.println("Welcome, " + currentUser.getFirstName() + "!");
        }
    }

    @FXML
    private void handleAddUser() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AjouterUser.fxml")));
        Stage stage = (Stage) btnAddUser.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Ajout des Utilisateurs");
    }

    @FXML
    private void handleViewCards() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UserList.fxml")));
            Stage stage = (Stage) btnViewCards.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Utilisateurs");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la vue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLogout() {
        // Clear the session
        Session.logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainMenu.fxml"));
            Stage stage = (Stage) btnAddUser.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
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
