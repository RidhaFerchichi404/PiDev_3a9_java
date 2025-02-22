package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.Exercice;
import org.example.service.ExerciceService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherExercicesFrontController {

    @FXML private FlowPane exercicesContainer;
    @FXML private Button btnRetour;

    private final ExerciceService exerciceService = new ExerciceService();
    private int idEquipement;

    public void setIdEquipement(int idEquipement) {
        this.idEquipement = idEquipement;
        loadExercices();
    }

    private void loadExercices() {
        try {
            exercicesContainer.getChildren().clear();
            List<Exercice> exercices = exerciceService.readByEquipementId(idEquipement);

            if (exercices.isEmpty()) {
                Label noExerciceLabel = new Label("Aucun exercice disponible");
                noExerciceLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ff4444; -fx-alignment: center;");
                exercicesContainer.getChildren().add(noExerciceLabel);
            } else {
                for (Exercice exercice : exercices) {
                    exercicesContainer.getChildren().add(createExerciceCard(exercice));
                }
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des exercices", Alert.AlertType.ERROR);
        }
    }

    private VBox createExerciceCard(Exercice exercice) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #262626; -fx-border-color: #ff8c00; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; "
                + "-fx-spacing: 10; -fx-alignment: center;");
        card.setPrefWidth(250);
        card.setPrefHeight(300);

        Label nomLabel = new Label(exercice.getNomExercice());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // ✅ Gestion de l'image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        try {
            String imagePath = exercice.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString()));
                } else {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
                }
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
            }
        } catch (Exception e) {
            System.out.println("Erreur chargement image: " + e.getMessage());
        }

        // ✅ Boutons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setStyle("-fx-alignment: center;");

        Button voirImagesButton = new Button("Voir Image");
        voirImagesButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-weight: bold;");
        voirImagesButton.setOnAction(event -> voirImagesExercice(exercice));

        buttonsBox.getChildren().add(voirImagesButton);
        card.getChildren().addAll(imageView, nomLabel, buttonsBox);

        return card;
    }

    private void voirImagesExercice(Exercice exercice) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherImagesExercice.fxml"));
            Parent root = loader.load();

            AfficherImagesExerciceController controller = loader.getController();
            controller.setExercice(exercice);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Images - " + exercice.getNomExercice());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'afficher les images", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void retourner() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
