package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.entities.Equipement;
import org.example.service.EquipementService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherEquipementsController {

    @FXML private FlowPane cardsContainer;
    @FXML private Label noEquipementsLabel;

    private EquipementService equipementService = new EquipementService();
    private int idSalle;

    @FXML
    public void initialize() {
        loadEquipements();
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
        loadEquipements();
    }

    private void loadEquipements() {
        try {
            cardsContainer.getChildren().clear();
            List<Equipement> equipements = equipementService.readBySalleId(idSalle);

            if (equipements.isEmpty()) {
                noEquipementsLabel.setVisible(true);
                cardsContainer.setVisible(false);
            } else {
                noEquipementsLabel.setVisible(false);
                cardsContainer.setVisible(true);
                for (Equipement equipement : equipements) {
                    cardsContainer.getChildren().add(createEquipementCard(equipement));
                }
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des équipements", Alert.AlertType.ERROR);
        }
    }

    // ✅ Création d'une carte d'équipement bien alignée
    private VBox createEquipementCard(Equipement equipement) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #ff8c00; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; "
                + "-fx-spacing: 10; -fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.5), 10, 0, 0, 3);");
        card.setPrefWidth(280);

        Label nomLabel = new Label("Nom: " + equipement.getNom());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label etatLabel = new Label("État: " + (equipement.isFonctionnement() ? "✔ Fonctionnel" : "❌ En panne"));
        etatLabel.setStyle(equipement.isFonctionnement() ? "-fx-text-fill: #4CAF50;" : "-fx-text-fill: #ff4444;");

        Label derniereVerifLabel = new Label("Dernière vérification: " + equipement.getDerniereVerification());
        derniereVerifLabel.setStyle("-fx-text-fill: white;");

        Label prochaineVerifLabel = new Label("Prochaine vérification: " + equipement.getProchaineVerification());
        prochaineVerifLabel.setStyle("-fx-text-fill: white;");

        HBox buttonsBox = new HBox(10);
        buttonsBox.setStyle("-fx-padding: 10 0 0 0;");

        Button voirExercicesButton = new Button("Voir Exercices");
        voirExercicesButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-cursor: hand;");
        voirExercicesButton.setOnAction(event -> voirExercices(equipement));

        Button editButton = new Button("Modifier");
        editButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand;");
        editButton.setOnAction(event -> modifierEquipement(equipement));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-cursor: hand;");
        deleteButton.setOnAction(event -> supprimerEquipement(equipement));

        // ✅ Ajouter le bouton "Voir Exercices"
        buttonsBox.getChildren().addAll(voirExercicesButton, editButton, deleteButton);

        card.getChildren().addAll(nomLabel, etatLabel, derniereVerifLabel, prochaineVerifLabel, buttonsBox);
        return card;
    }


    @FXML
    private void ajouterEquipement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterEquipement.fxml"));
            Parent root = loader.load();

            AjouterEquipementController controller = loader.getController();
            controller.setIdSalle(idSalle);
            controller.setAfterSaveAction(this::loadEquipements);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un équipement");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire", Alert.AlertType.ERROR);
        }
    }

    private void modifierEquipement(Equipement equipement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierEquipement.fxml"));
            Parent root = loader.load();

            ModifierEquipementController controller = loader.getController();
            controller.setEquipement(equipement);
            controller.setAfterSaveAction(this::loadEquipements);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'équipement");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la modification", Alert.AlertType.ERROR);
        }
    }

    private void supprimerEquipement(Equipement equipement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cet équipement ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                equipementService.delete(equipement);
                loadEquipements();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression", Alert.AlertType.ERROR);
            }
        }
    }
    private void voirExercices(Equipement equipement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherExercices.fxml"));
            Parent root = loader.load();

            AfficherExercicesController controller = loader.getController();
            controller.setIdEquipement(equipement.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Exercices - " + equipement.getNom());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la liste des exercices", Alert.AlertType.ERROR);
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
