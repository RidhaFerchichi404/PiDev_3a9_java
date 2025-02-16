package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.SalleDeSport;
import org.example.service.SalleDeSportService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherSallesFrontController {

    @FXML private FlowPane cardsContainer;

    private final SalleDeSportService salleService = new SalleDeSportService();

    @FXML
    public void initialize() {
        afficherSalles();
    }

    private void afficherSalles() {
        try {
            cardsContainer.getChildren().clear();
            List<SalleDeSport> salles = salleService.readAll();

            if (salles.isEmpty()) {
                Label noSalleLabel = new Label("Aucune salle disponible");
                noSalleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff4444; -fx-alignment: center;");
                cardsContainer.getChildren().add(noSalleLabel);
            } else {
                for (SalleDeSport salle : salles) {
                    cardsContainer.getChildren().add(createSalleCard(salle));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createSalleCard(SalleDeSport salle) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #262626; -fx-border-color: #ff8c00; "
                + "-fx-border-radius: 15; -fx-background-radius: 15; -fx-padding: 15; "
                + "-fx-spacing: 10; -fx-alignment: center; -fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.5), 10, 0, 0, 5);");
        card.setPrefWidth(350);
        card.setPrefHeight(380);

        Label nomLabel = new Label("Salle: " + salle.getNom());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label zoneLabel = new Label("Zone: " + salle.getZone());
        zoneLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 16px;");

        // ✅ Gestion de l'image avec une taille adaptée
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);

        try {
            String imagePath = salle.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString()));
                } else {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
                }
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
        }

        // ✅ Bouton "Voir Équipements"
        Button voirEquipementsButton = new Button("Voir Équipements");
        voirEquipementsButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        voirEquipementsButton.setOnAction(event -> voirEquipements(salle));

        card.getChildren().addAll(imageView, nomLabel, zoneLabel, voirEquipementsButton);
        return card;
    }

    private void voirEquipements(SalleDeSport salle) {
        try {
            System.out.println("📌 Chargement des équipements pour la salle: " + salle.getNom());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEquipementsFront.fxml"));
            Parent root = loader.load();

            AfficherEquipementsFrontController controller = loader.getController();
            controller.setIdSalle(salle.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Équipements - " + salle.getNom());
            stage.show();

            System.out.println("✅ Affichage des équipements réussi !");
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'affichage des équipements: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'afficher les équipements", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
