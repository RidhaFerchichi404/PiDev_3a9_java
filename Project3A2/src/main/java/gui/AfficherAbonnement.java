package gui;

import entities.Abonnement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.AbonnementService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherAbonnement {

    @FXML
    private FlowPane abonnementsContainer; // Conteneur principal pour les abonnements

    private AbonnementService abonnementService = new AbonnementService();
    private Parent ajouterRoot;
    private VBox mainContainer; // Add this field
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void ouvrirAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAbonnement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Abonnement");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    // Add this method
    public void setMainContainer(VBox mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void setAjouterRoot(Parent ajouterRoot) {
        this.ajouterRoot = ajouterRoot;
    }

    @FXML
    public void initialize() {
        try {
            // Récupérer tous les abonnements depuis la base de données
            List<Abonnement> abonnements = abonnementService.readAll();
            afficherAbonnements(abonnements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void afficherAbonnements(List<Abonnement> abonnements) {
        abonnementsContainer.getChildren().clear(); // Vider le conteneur avant d'ajouter de nouveaux éléments

        for (Abonnement abonnement : abonnements) {
            // Créer un cadre pour chaque abonnement
            VBox cadreAbonnement = new VBox(10); // Espacement de 10 entre les éléments
            cadreAbonnement.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

            // Ajouter les détails de l'abonnement
            Label nomLabel = new Label("Nom : " + abonnement.getNom());
            Label descriptionLabel = new Label("Description : " + abonnement.getDescriptiona());
            Label prixLabel = new Label("Prix : " + abonnement.getPrix());
            Label dureeLabel = new Label("Durée : " + abonnement.getDuree() + " jours");

            // Ajouter les boutons de suppression et de modification
            Button supprimerButton = new Button("Supprimer");
            Button modifierButton = new Button("Modifier");

            // Associer des actions aux boutons
            supprimerButton.setOnAction(event -> supprimerAbonnement(abonnement));
            modifierButton.setOnAction(event -> modifierAbonnement(abonnement));

            // Ajouter les éléments au cadre
            cadreAbonnement.getChildren().addAll(nomLabel, descriptionLabel, prixLabel, dureeLabel, supprimerButton, modifierButton);

            // Ajouter le cadre au conteneur principal
            abonnementsContainer.getChildren().add(cadreAbonnement);
        }
    }

    private void supprimerAbonnement(Abonnement abonnement) {
        try {
            // Supprimer l'abonnement de la base de données
            //abonnementService.delete(abonnement.getId());

            // Recharger la liste des abonnements
            List<Abonnement> abonnements = abonnementService.readAll();
            afficherAbonnements(abonnements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private VBox creerCarteAbonnement(Abonnement abonnement) {
        VBox carte = new VBox(10);
        carte.setStyle("-fx-background-color: #262626; -fx-padding: 15; -fx-border-color: #ff8c00; -fx-border-radius: 10;");

        Label nomLabel = new Label("Nom : " + abonnement.getNom());
        nomLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 16px;");

        Label descriptionLabel = new Label("Description : " + abonnement.getDescriptiona());
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label dureeLabel = new Label("Durée : " + abonnement.getDuree() + " jours");
        dureeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label prixLabel = new Label("Prix : " + abonnement.getPrix() + " €");
        prixLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label salleLabel = new Label("Salle : " + abonnement.getSalleNom());
        salleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Boutons pour modifier et supprimer
        HBox boutonsContainer = new HBox(10);
        Button modifierButton = new Button("Modifier");
        modifierButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white;");
        modifierButton.setOnAction(event -> modifierAbonnement(abonnement));

        Button supprimerButton = new Button("Supprimer");
        supprimerButton.setStyle("-fx-background-color: #ff3333; -fx-text-fill: white;");
        supprimerButton.setOnAction(event -> supprimerAbonnement(abonnement));

        boutonsContainer.getChildren().addAll(modifierButton, supprimerButton);
        carte.getChildren().addAll(nomLabel, descriptionLabel, dureeLabel, prixLabel, salleLabel, boutonsContainer);

        return carte;
    }
    public void actualiser() {
        abonnementsContainer.getChildren().clear(); // Vider le conteneur actuel

        try {
            List<Abonnement> abonnements = abonnementService.readAll(); // Récupérer tous les abonnements
            for (Abonnement abonnement : abonnements) {
                abonnementsContainer.getChildren().add(creerCarteAbonnement(abonnement)); // Ajouter chaque abonnement
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des abonnements : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void modifierAbonnement(Abonnement abonnement) {
        try {
            // Charger le fichier FXML pour la modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAbonnement.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et passer l'abonnement à modifier
            ModifierAbonnement controller = loader.getController();
            controller.chargerAbonnement(abonnement);

            // Créer une nouvelle scène
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier l'Abonnement");
            stage.show();

            // Rafraîchir l'affichage après la modification
            stage.setOnHidden(event -> actualiser());
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du formulaire de modification : " + e.getMessage());
            e.printStackTrace();
        }
    }
}