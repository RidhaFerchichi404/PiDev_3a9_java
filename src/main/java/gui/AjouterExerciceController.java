package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entities.Exercice;
import org.example.service.ExerciceService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AjouterExerciceController {

    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private ImageView imagePreview;
    @FXML private Button choisirImageButton;
    @FXML private Button enregistrerButton;
    @FXML private Button annulerButton;

    private String imagePath;
    private int idEquipement;
    private Runnable afterSaveAction;
    private final ExerciceService exerciceService = new ExerciceService();

    public void setIdEquipement(int idEquipement) {
        this.idEquipement = idEquipement;
    }

    public void setAfterSaveAction(Runnable afterSaveAction) {
        this.afterSaveAction = afterSaveAction;
    }

    @FXML
    private void initialize() {
        resetFields();
    }

    private void resetFields() {
        nomField.setText("");
        descriptionField.setText("");
        imagePath = null;

        try {
            // Vérifier si l'image par défaut existe avant de l'afficher
            String defaultImagePath = "/images/default.png";
            if (getClass().getResourceAsStream(defaultImagePath) != null) {
                imagePreview.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultImagePath))));
            } else {
                System.err.println("⚠ L'image par défaut n'existe pas : " + defaultImagePath);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement de l'image par défaut : " + e.getMessage());
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
            imagePath = selectedFile.getAbsolutePath();
            imagePreview.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    private void enregistrerExercice() {
        if (validateInputs()) {
            try {
                Exercice nouvelExercice = new Exercice(
                        descriptionField.getText().trim(),
                        (imagePath != null && !imagePath.isEmpty()) ? imagePath : "images/default.png",
                        1, // ID utilisateur par défaut
                        idEquipement,
                        nomField.getText().trim()
                );

                exerciceService.create(nouvelExercice);
                showAlert("Succès", "L'exercice a été ajouté avec succès!", Alert.AlertType.INFORMATION);

                if (afterSaveAction != null) afterSaveAction.run();
                fermerFenetre();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de l'ajout de l'exercice: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validateInputs() {
        if (nomField.getText().trim().isEmpty() || descriptionField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void fermerFenetre() {
        ((Stage) enregistrerButton.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
