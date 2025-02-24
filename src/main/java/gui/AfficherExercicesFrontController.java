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
        // ─────────────────────────────────────────────────────────────────────────────
        // 1. Création de la carte (VBox) avec un style moderne
        // ─────────────────────────────────────────────────────────────────────────────
        VBox card = new VBox(12);
        card.setStyle(
                "-fx-background-color: #1A1A1A; "
                        + "-fx-background-radius: 20; "
                        + "-fx-padding: 20; "
                        + "-fx-spacing: 12; "
                        + "-fx-alignment: center; "
                        + "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.3), 10, 0, 4, 4); "
                        + "-fx-cursor: hand;"
        );
        // Ajustez la taille de la carte pour laisser de la place au texte
        card.setPrefWidth(300);
        card.setPrefHeight(450);

        // ─────────────────────────────────────────────────────────────────────────────
        // 2. Label du nom de l'exercice (wrapText pour ne pas couper le texte)
        // ─────────────────────────────────────────────────────────────────────────────
        Label nomLabel = new Label("Nom de l'exercice : " + exercice.getNomExercice());
        nomLabel.setStyle("-fx-text-fill: #FF6600; -fx-font-size: 18px; -fx-font-weight: bold;");
        nomLabel.setWrapText(true);
        nomLabel.setMaxWidth(260); // Largeur max pour renvoyer à la ligne si nécessaire

        // ─────────────────────────────────────────────────────────────────────────────
        // 3. Label de la description (wrapText + couleur + taille)
        // ─────────────────────────────────────────────────────────────────────────────
        String description = (exercice.getDescription() != null && !exercice.getDescription().isEmpty())
                ? exercice.getDescription()
                : "Aucune description disponible";
        Label descriptionLabel = new Label("Description : " + description);
        descriptionLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(260);

        // ─────────────────────────────────────────────────────────────────────────────
        // 4. ImageView pour l'exercice
        // ─────────────────────────────────────────────────────────────────────────────
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        // Chargement de l'image depuis le fichier ou ressource
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
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
        }

        // ─────────────────────────────────────────────────────────────────────────────
        // 5. Boutons d'action
        //    - "Voir Image" : ouvrir une fenêtre ou pop-up pour agrandir l'image
        //    - "Détails"    : afficher nom & description complets dans une fenêtre ou alerte
        // ─────────────────────────────────────────────────────────────────────────────
        Button voirImageButton = new Button("Voir Image");
        voirImageButton.setStyle(
                "-fx-background-color: #FF6600; "
                        + "-fx-text-fill: #000000; "
                        + "-fx-font-weight: bold; "
                        + "-fx-font-size: 14px; "
                        + "-fx-background-radius: 10; "
                        + "-fx-cursor: hand; "
                        + "-fx-padding: 8 20;"
        );
        // Exemple : ouvre la même vue, ou un pop-up, selon votre logique
        voirImageButton.setOnAction(event -> voirImagesExercice(exercice));

        Button detailsButton = new Button("Détails");
        detailsButton.setStyle(
                "-fx-background-color: #FF6600; "
                        + "-fx-text-fill: #000000; "
                        + "-fx-font-weight: bold; "
                        + "-fx-font-size: 14px; "
                        + "-fx-background-radius: 10; "
                        + "-fx-cursor: hand; "
                        + "-fx-padding: 8 20;"
        );
        // Affiche nom & description complets dans une simple alerte ou un pop-up
        detailsButton.setOnAction(event -> {
            showAlert(
                    "Détails de l'exercice",
                    "Nom complet : " + exercice.getNomExercice()
                            + "\n\nDescription : " + description,
                    Alert.AlertType.INFORMATION
            );
        });

        // ─────────────────────────────────────────────────────────────────────────────
        // 6. Assemblage des éléments dans la carte
        // ─────────────────────────────────────────────────────────────────────────────
        HBox buttonsBox = new HBox(10, voirImageButton, detailsButton);
        buttonsBox.setStyle("-fx-alignment: center;");
        card.getChildren().addAll(imageView, nomLabel, descriptionLabel, buttonsBox);

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
