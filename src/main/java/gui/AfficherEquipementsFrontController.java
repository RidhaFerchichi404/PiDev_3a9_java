package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.Equipement;
import org.example.service.EquipementService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #262626; -fx-border-color: #ff8c00; "
                + "-fx-border-radius: 15; -fx-background-radius: 15; -fx-padding: 15; "
                + "-fx-spacing: 10; -fx-alignment: center; -fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.5), 10, 0, 0, 5);");
        card.setPrefWidth(250);
        card.setPrefHeight(120);

        Label nomLabel = new Label("Équipement: " + equipement.getNom());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // ✅ Bouton "Voir Exercices" en vert
        Button voirExerciceButton = new Button("Voir Exercices");
        voirExerciceButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        voirExerciceButton.setOnAction(event -> voirExercices(equipement));

        card.getChildren().addAll(nomLabel, voirExerciceButton);
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
