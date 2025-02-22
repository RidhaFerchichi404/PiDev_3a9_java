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
import entities.Exercice;
import services.ExerciceService;

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
        // ✅ Création de la carte principale
        VBox card = new VBox(15);
        card.setStyle(
                "-fx-background-color: #1A1A1A; " +  // Fond noir profond
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 20; " +
                        "-fx-spacing: 15; " +
                        "-fx-alignment: center; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.3), 10, 0, 4, 4); " +
                        "-fx-cursor: hand;");               // Curseur interactif

        card.setPrefWidth(280);
        card.setPrefHeight(350);

        // ✅ Nom de l'exercice en blanc
        Label nomLabel = new Label("nom de l'exercice :"+exercice.getNomExercice());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // ✅ Image de l'exercice
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

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
            System.out.println("Erreur de chargement d'image : " + e.getMessage());
        }

        // ✅ Conteneur pour le bouton
        HBox buttonsBox = new HBox(10);
        buttonsBox.setStyle("-fx-alignment: center;");

        // ✅ Bouton "Voir Image"
        Button voirImagesButton = new Button("Voir Image");
        voirImagesButton.setStyle(
                "-fx-background-color: #FF6600; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.4), 8, 0, 2, 2);");

        // ✅ Effet hover du bouton
        voirImagesButton.setOnMouseEntered(e -> voirImagesButton.setStyle(
                "-fx-background-color: #FF3300; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.6), 10, 0, 2, 2);"));

        voirImagesButton.setOnMouseExited(e -> voirImagesButton.setStyle(
                "-fx-background-color: #FF6600; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.4), 8, 0, 2, 2);"));

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
