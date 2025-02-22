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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entities.SalleDeSport;
import org.example.service.SalleDeSportService;

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
                SalleDeSport nouvelleSalle = new SalleDeSport(
                    nomField.getText().trim(),
                    zoneField.getText().trim(),
                    imageUrlField.getText().trim(),
                    1 // ID utilisateur par défaut
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherCards.fxml"));
            Parent root = loader.load();
            
            Stage currentStage = (Stage) nomField.getScene().getWindow();
            
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("Liste des Salles de Sport");
            currentStage.show();
            
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