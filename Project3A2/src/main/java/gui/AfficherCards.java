package gui;

import entities.Abonnement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.AbonnementService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherCards implements Initializable {
    @FXML
    private FlowPane cardsContainer;

    private AbonnementService abonnementService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialisation du contrôleur");
        try {
            abonnementService = new AbonnementService();
            cardsContainer.setPadding(new Insets(20));
            cardsContainer.setHgap(20);
            cardsContainer.setVgap(20);
            actualiser();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void actualiser() {
        System.out.println("Actualisation des cartes");
        if (cardsContainer == null) {
            System.err.println("cardsContainer est null!");
            return;
        }

        cardsContainer.getChildren().clear();
        try {
            List<Abonnement> abonnements = abonnementService.readAll();
            System.out.println("Nombre d'abonnements trouvés: " + abonnements.size());
            for (Abonnement abonnement : abonnements) {
                cardsContainer.getChildren().add(createAbonnementCard(abonnement));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
            showAlert("Erreur", "Erreur lors du chargement des abonnements: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createAbonnementCard(Abonnement abonnement) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #262626; " +
                "-fx-padding: 15; " +
                "-fx-border-color: #ff8c00; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-min-width: 250; " +
                "-fx-max-width: 250;");

        // Nom de l'abonnement
        Label nameLabel = new Label(abonnement.getNom());
        nameLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Description
        Label descriptionLabel = new Label(abonnement.getDescriptiona());
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Durée
        Label dureeLabel = new Label("Durée: " + abonnement.getDuree() + " jours");
        dureeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Prix
        Label prixLabel = new Label("Prix: " + abonnement.getPrix() + " €");
        prixLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Salle de sport
        Label salleLabel = new Label("Salle: " + abonnement.getSalleNom());
        salleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Boutons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);

        Button editButton = new Button("Modifier");
        editButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; " +
                "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
        editButton.setOnAction(e -> modifierAbonnement(abonnement));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #ff3333; -fx-text-fill: white; " +
                "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
        deleteButton.setOnAction(e -> supprimerAbonnement(abonnement));

        buttonsBox.getChildren().addAll(editButton, deleteButton);
        card.getChildren().addAll(nameLabel, descriptionLabel, dureeLabel, prixLabel, salleLabel, buttonsBox);

        return card;
    }

    private void supprimerAbonnement(Abonnement abonnement) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer l'abonnement " + abonnement.getNom() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                abonnementService.delete(abonnement);
                actualiser();
                showAlert("Succès", "Abonnement supprimé avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void modifierAbonnement(Abonnement abonnement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAbonnement.fxml"));
            Parent root = loader.load();

            ModifierAbonnement controller = loader.getController();
            controller.chargerAbonnement(abonnement);

            // Créer une nouvelle scène dans la fenêtre actuelle
            Stage currentStage = (Stage) cardsContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("Modifier l'Abonnement");
            currentStage.show();

            // Rafraîchir l'affichage après la modification
            currentStage.setOnHidden(event -> actualiser());
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de modification: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
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
