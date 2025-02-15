package gui;

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
        // Cette méthode est appelée automatiquement après le chargement du FXML
    }

    public void setSalle(SalleDeSport salle) {
        try {
            // Récupérer la salle depuis la base de données pour avoir les données à jour
            this.salleActuelle = salleService.readById(salle.getId());
            
            // Remplir les champs avec les données récupérées
            nomField.setText(this.salleActuelle.getNom());
            zoneField.setText(this.salleActuelle.getZone());
            imageUrlField.setText(this.salleActuelle.getImage());
            
            // Charger l'image de manière sécurisée
            try {
                String imagePath = this.salleActuelle.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    // Essayer de charger l'image depuis le système de fichiers
                    File file = new File("src/main/resources/" + imagePath);
                    if (file.exists()) {
                        Image image = new Image(file.toURI().toString());
                        imagePreview.setImage(image);
                    } else {
                        // Si le fichier n'existe pas, charger l'image par défaut
                        Image defaultImage = new Image(getClass().getResourceAsStream("/images/default.png"));
                        imagePreview.setImage(defaultImage);
                    }
                } else {
                    // Si pas de chemin d'image, charger l'image par défaut
                    Image defaultImage = new Image(getClass().getResourceAsStream("/images/default.png"));
                    imagePreview.setImage(defaultImage);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                try {
                    // Dernière tentative de charger l'image par défaut
                    Image defaultImage = new Image(getClass().getResourceAsStream("/images/default.png"));
                    imagePreview.setImage(defaultImage);
                } catch (Exception ex) {
                    System.out.println("Impossible de charger l'image par défaut: " + ex.getMessage());
                }
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la récupération des données de la salle: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleModifier() {
        if (validateInputs()) {
            try {
                // Préserver l'ID de l'utilisateur existant
                int currentUserId = salleActuelle.getUser_id(); // Assurez-vous que cette propriété existe
                
                // Mettre à jour les autres champs
                salleActuelle.setNom(nomField.getText().trim());
                salleActuelle.setZone(zoneField.getText().trim());
                salleActuelle.setImage(imageUrlField.getText().trim());
                salleActuelle.setUser_id(currentUserId); // Conserver l'ID de l'utilisateur
                
                salleService.update(salleActuelle);
                showAlert("Succès", "La salle a été modifiée avec succès!", Alert.AlertType.INFORMATION);
                
                // Fermer la fenêtre après la modification
                nomField.getScene().getWindow().hide();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la modification de la salle: " + e.getMessage(), Alert.AlertType.ERROR);
            }
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
            showAlert("Erreur", "Erreur lors de l'ouverture de la liste des salles", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void annuler() {
        // Fermer la fenêtre de modification
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private boolean validateInputs() {
        if (nomField.getText().trim().isEmpty() || zoneField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}