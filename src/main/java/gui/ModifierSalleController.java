package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import entities.SalleDeSport;
import services.SalleDeSportService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ModifierSalleController {

    @FXML private TextField nomField;
    @FXML private TextField zoneField;
    @FXML private TextField imageUrlField;
    @FXML private ImageView imagePreview;

    private SalleDeSportService salleService = new SalleDeSportService();
    private SalleDeSport salleActuelle;

    @FXML
    public void initialize() {
        // Initialisation automatique après chargement du FXML
    }

    public void setSalle(SalleDeSport salle) {
        try {
            // Charger les données de la salle depuis la base de données
            this.salleActuelle = salleService.readById(salle.getId());

            // Pré-remplir les champs avec les données de la salle
            nomField.setText(this.salleActuelle.getNom());
            zoneField.setText(this.salleActuelle.getZone());
            imageUrlField.setText(this.salleActuelle.getImage());

            // Charger l'image associée à la salle
            chargerImage(this.salleActuelle.getImage());

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la récupération des données : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void chargerImage(String cheminImage) {
        try {
            if (cheminImage != null && !cheminImage.isEmpty()) {
                File file = new File("src/main/resources/" + cheminImage);
                if (file.exists()) {
                    imagePreview.setImage(new Image(file.toURI().toString()));
                } else {
                    imagePreview.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
                }
            } else {
                imagePreview.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger l'image : " + e.getMessage(), Alert.AlertType.ERROR);
        }
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
    private void handleModifier() {
        if (validateInputs()) {
            try {
                salleActuelle.setNom(nomField.getText().trim());
                salleActuelle.setZone(zoneField.getText().trim());
                salleActuelle.setImage(imageUrlField.getText().trim());

                salleService.update(salleActuelle);
                showAlert("Succès", "Salle modifiée avec succès !", Alert.AlertType.INFORMATION);

                redirigerVersListeSalles();

            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la modification : " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void annuler() {
        redirigerVersListeSalles();
    }

    private void redirigerVersListeSalles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherCards.fxml"));
            Parent root = loader.load();

            // 🔄 Remplacer le contenu directement dans le dashboard
            StackPane mainContent = (StackPane) nomField.getScene().lookup("#mainContent");

            if (mainContent != null) {
                mainContent.getChildren().setAll(root);
            } else {
                showAlert("Erreur", "Impossible de trouver le conteneur principal du dashboard.", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'affichage de la liste des salles.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateInputs() {
        if (nomField.getText().trim().isEmpty() || zoneField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
