package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entities.Exercice;
import org.example.service.ExerciceService;

import java.io.File;
import java.sql.SQLException;

public class ModifierExerciceController {

    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private ImageView imagePreview;
    @FXML private Button choisirImageButton;
    @FXML private Button enregistrerButton;

    private String imagePath;
    private Exercice exercice;
    private Runnable afterSaveAction;
    private ExerciceService exerciceService = new ExerciceService();

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
        remplirChamps();
    }

    public void setAfterSaveAction(Runnable afterSaveAction) {
        this.afterSaveAction = afterSaveAction;
    }

    private void remplirChamps() {
        if (exercice != null) {
            System.out.println("📌 Chargement des données de l'exercice : " + exercice);
            nomField.setText(exercice.getNomExercice());
            descriptionField.setText(exercice.getDescription());
            imagePath = exercice.getImage();

            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    imagePreview.setImage(new Image(file.toURI().toString()));
                } else {
                    imagePreview.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
                }
            } else {
                imagePreview.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
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
            imagePath = selectedFile.getAbsolutePath();
            imagePreview.setImage(new Image(selectedFile.toURI().toString()));
        }
    }



    @FXML
    private void enregistrerExercice() {
        if (validateInputs()) {
            try {
                if (exercice.getId() <= 0) {
                    showAlert("Erreur", "L'exercice ne possède pas un ID valide !", Alert.AlertType.ERROR);
                    return;
                }

                // ✅ Mise à jour des données de l'exercice
                exercice.setNomExercice(nomField.getText().trim());
                exercice.setDescription(descriptionField.getText().trim());
                exercice.setImage((imagePath != null && !imagePath.isEmpty()) ? imagePath : "images/default.png");

                // ✅ Forcer idUser = 1
                exercice.setIdUser(1);

                // 🔍 Debugging
                System.out.println("🔄 Mise à jour de l'exercice avec ID Utilisateur : " + exercice.getIdUser());

                // ✅ Exécution de la mise à jour
                exerciceService.update(exercice);

                showAlert("Succès", "L'exercice a été modifié avec succès!", Alert.AlertType.INFORMATION);

                if (afterSaveAction != null) afterSaveAction.run();
                fermerFenetre();
            } catch (SQLException e) {
                System.err.println("❌ Erreur SQL lors de la modification : " + e.getMessage());
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la modification de l'exercice: " + e.getMessage(), Alert.AlertType.ERROR);
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
