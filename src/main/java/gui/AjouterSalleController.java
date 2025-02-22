package gui;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entities.SalleDeSport;
import services.SalleDeSportService;
import utils.Session;

public class AjouterSalleController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField zoneField;

    @FXML
    private TextField imageUrlField;

    @FXML
    private ImageView imagePreview;

    private SalleDeSportService salleService = new SalleDeSportService();

    @FXML
    private void initialize() {
        assert nomField != null : "nomField not injected!";
        assert zoneField != null : "zoneField not injected!";
        assert imageUrlField != null : "imageUrlField not injected!";
        resetFields();
    }

    private void resetFields() {
        nomField.setText("");
        zoneField.setText("");
        imageUrlField.setText("");
        imagePreview.setImage(null);
    }

    @FXML
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String relativePath = "images/" + selectedFile.getName();
            imageUrlField.setText(relativePath);
            imagePreview.setImage(new Image(selectedFile.toURI().toString()));
        }
    }


    @FXML
    private void handleAjouter() {
        if (validateInputs()) {
            try {
                // ✅ Récupérer l'ID de l'utilisateur depuis la session
                long userId = Session.getCurrentUser() != null ? Session.getCurrentUser().getId() : -1;

                if (userId == -1) {
                    showAlert("Erreur de Session", "Impossible de récupérer l'utilisateur actuel.", Alert.AlertType.ERROR);
                    return;
                }

                SalleDeSport nouvelleSalle = new SalleDeSport(
                        nomField.getText().trim(),
                        zoneField.getText().trim(),
                        imageUrlField.getText().trim(),
                        userId  // ✅ L'ID utilisateur dynamique
                );

                salleService.create(nouvelleSalle);
                showAlert("Succès", "La salle a été ajoutée avec succès!", Alert.AlertType.INFORMATION);
                afficherSalles();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de l'ajout de la salle: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }



    private boolean validateInputs() {
        if (nomField.getText().trim().isEmpty() || zoneField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void afficherSalles() {
        try {
            // Charger la nouvelle vue des salles
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherCards.fxml"));
            Parent salleView = loader.load();

            // Récupérer la scène actuelle
            StackPane mainContent = (StackPane) nomField.getScene().lookup("#mainContent");

            // Remplacer le contenu du StackPane
            if (mainContent != null) {
                mainContent.getChildren().setAll(salleView);
            } else {
                showAlert("Erreur", "Impossible de trouver le conteneur principal.", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la liste des salles: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void setModificationMode(SalleDeSport salle) {
    }
}