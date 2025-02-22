package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import entities.Equipement;
import services.EquipementService;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class AfficherEquipementsFrontController {

    @FXML private FlowPane cardsContainer;
    @FXML private Button btnRetour;

    private final EquipementService equipementService = new EquipementService();
    private int idSalle;

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
        System.out.println("📌 ID Salle reçu : " + idSalle);
        loadEquipements();
    }

    private void loadEquipements() {
        try {
            System.out.println("📌 Chargement des équipements pour la salle ID : " + idSalle);
            List<Equipement> equipements = equipementService.readBySalleId(idSalle);
            cardsContainer.getChildren().clear();

            if (equipements.isEmpty()) {
                System.out.println("⚠ Aucun équipement trouvé !");
                Label noEquipLabel = new Label("Aucun équipement disponible.");
                noEquipLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff4444;");
                cardsContainer.getChildren().add(noEquipLabel);
            } else {
                for (Equipement equipement : equipements) {
                    System.out.println("✅ Équipement trouvé : " + equipement.getNom());
                    cardsContainer.getChildren().add(createEquipementCard(equipement));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors du chargement des équipements : " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des équipements", Alert.AlertType.ERROR);
        }
    }

    private VBox createEquipementCard(Equipement equipement) {
        // ✅ Création de la carte principale
        VBox card = new VBox(15);
        card.setStyle(
                "-fx-background-color: #1A1A1A; " +
                        "-fx-border-color: #FF6600; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 20; " +
                        "-fx-spacing: 15; " +
                        "-fx-alignment: center; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.5), 12, 0, 4, 4); " +
                        "-fx-cursor: hand;");

        card.setPrefWidth(300);
        card.setPrefHeight(300);

        // ✅ Effet de survol (hover)
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #262626; " +
                        "-fx-border-color: #FF6600; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 20; " +
                        "-fx-spacing: 15; " +
                        "-fx-alignment: center; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.8), 16, 0, 4, 4); " +
                        "-fx-cursor: hand;"));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #1A1A1A; " +
                        "-fx-border-color: #FF6600; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 20; " +
                        "-fx-spacing: 15; " +
                        "-fx-alignment: center; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.5), 12, 0, 4, 4); " +
                        "-fx-cursor: hand;"));

        // ✅ Image par défaut de l'équipement
        ImageView equipImage = new ImageView();
        try {
            // Charge une image par défaut (equipement.jpg) depuis les ressources
            Image image = new Image(getClass().getResourceAsStream("/images/equipement.jpg"));
            equipImage.setImage(image);
            equipImage.setFitWidth(200);
            equipImage.setFitHeight(150);
            equipImage.setPreserveRatio(true);
            equipImage.setStyle("-fx-border-radius: 10; -fx-background-radius: 10;");
        } catch (Exception e) {
            System.out.println("❌ Erreur de chargement de l'image de l'équipement : " + e.getMessage());
        }

        // ✅ Nom de l'équipement en blanc
        Label nomLabel = new Label(" Équipement: " + equipement.getNom());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // ✅ Bouton "Voir Exercices" avec effet hover
        Button voirExerciceButton = new Button(" Voir Exercices");
        voirExerciceButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #FF6600, #CC5200); " +
                        "-fx-text-fill: #000000; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.4), 8, 0, 2, 2);");

        voirExerciceButton.setOnMouseEntered(e -> voirExerciceButton.setStyle(
                "-fx-background-color: #FF3300; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.6), 10, 0, 2, 2);"));

        voirExerciceButton.setOnMouseExited(e -> voirExerciceButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #FF6600, #CC5200); " +
                        "-fx-text-fill: #000000; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.4), 8, 0, 2, 2);"));

        voirExerciceButton.setOnAction(event -> voirExercices(equipement));

        // ✅ Ajout des éléments dans la carte
        card.getChildren().addAll(equipImage, nomLabel, voirExerciceButton);
        return card;
    }



    private void voirExercices(Equipement equipement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherExercicesFront.fxml"));
            Parent root = loader.load();

            AfficherExercicesFrontController controller = loader.getController();
            controller.setIdEquipement(equipement.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 500, 400)); // ✅ Fenêtre plus petite
            stage.setTitle("Exercices - " + equipement.getNom());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la liste des exercices", Alert.AlertType.ERROR);
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
