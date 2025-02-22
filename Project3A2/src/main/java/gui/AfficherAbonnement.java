package gui;

import entities.Abonnement;
import entities.Promotion;
import javafx.geometry.Pos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos; // Import manquant
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
    private FlowPane abonnementsContainer;
    @FXML
    private FlowPane cardsContainer;
    private Parent ajouterRoot; // Ajoutez ce champ

    private AbonnementService abonnementService = new AbonnementService();

    private VBox mainContainer; // Ajoutez ce champ

    // Ajoutez cette méthode
    public void setMainContainer(VBox mainContainer) {
        this.mainContainer = mainContainer;
    }
    @FXML
    public void initialize() {
        try {
            List<Abonnement> abonnements = abonnementService.readAll();
            afficherAbonnements(abonnements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void afficherAbonnements(List<Abonnement> abonnements) {
        abonnementsContainer.getChildren().clear();

        for (Abonnement abonnement : abonnements) {
            VBox carteAbonnement = creerCarteAbonnement(abonnement);
            abonnementsContainer.getChildren().add(carteAbonnement);
        }
    }

    private VBox creerCarteAbonnement(Abonnement abonnement) {
        VBox carte = new VBox(10);
        carte.setStyle("-fx-background-color: #262626; -fx-padding: 15; -fx-border-color: #ff8c00; -fx-border-radius: 10;");

        // Informations de l'abonnement
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

        // Affichage des promotions
        VBox promotionsContainer = new VBox(5);
        promotionsContainer.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-border-color: #4CAF50; -fx-border-radius: 5;");

        Label promotionsTitle = new Label("Promotions :");
        promotionsTitle.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 14px; -fx-font-weight: bold;");
        promotionsContainer.getChildren().add(promotionsTitle);

        List<Promotion> promotions = abonnement.getPromotions();
        if (promotions != null && !promotions.isEmpty()) {
            for (Promotion promotion : promotions) {
                Label promotionLabel = new Label(
                        "➤ " + promotion.getCodePromo() + " : " + promotion.getValeurReduction() + "% de réduction"
                );
                promotionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
                promotionsContainer.getChildren().add(promotionLabel);
            }
        } else {
            Label noPromotionLabel = new Label("Aucune promotion disponible.");
            noPromotionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
            promotionsContainer.getChildren().add(noPromotionLabel);
        }

        // Boutons pour modifier, supprimer et ajouter une promotion
        HBox boutonsContainer = new HBox(10);
        boutonsContainer.setAlignment(javafx.geometry.Pos.CENTER);

        Button modifierButton = new Button("Modifier");
        modifierButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white;");
        modifierButton.setOnAction(event -> modifierAbonnement(abonnement));

        Button supprimerButton = new Button("Supprimer");
        supprimerButton.setStyle("-fx-background-color: #ff3333; -fx-text-fill: white;");
        supprimerButton.setOnAction(event -> supprimerAbonnement(abonnement));

        Button ajouterPromotionButton = new Button("Ajouter une promotion");
        ajouterPromotionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        ajouterPromotionButton.setOnAction(event -> ouvrirAjoutPromotion(abonnement));

        boutonsContainer.getChildren().addAll(modifierButton, supprimerButton, ajouterPromotionButton);

        // Ajouter les éléments à la carte
        carte.getChildren().addAll(nomLabel, descriptionLabel, dureeLabel, prixLabel, salleLabel, promotionsContainer, boutonsContainer);

        return carte;
    }

   /* private void afficherAbonnements(List<Abonnement> abonnements) {
        abonnementsContainer.getChildren().clear(); // Vider le conteneur avant d'ajouter de nouveaux éléments

        for (Abonnement abonnement : abonnements) {
            VBox carteAbonnement = creerCarteAbonnement(abonnement);
            abonnementsContainer.getChildren().add(carteAbonnement);
        }
    }
    public void setAjouterRoot(Parent ajouterRoot) {
        this.ajouterRoot = ajouterRoot;
    }
    private VBox creerCarteAbonnement(Abonnement abonnement) {
        VBox carte = new VBox(10);
        carte.setStyle("-fx-background-color: #262626; -fx-padding: 15; -fx-border-color: #ff8c00; -fx-border-radius: 10;");

        // Nom de l'abonnement
        Label nomLabel = new Label("Nom : " + abonnement.getNom());
        nomLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 16px;");

        // Description
        Label descriptionLabel = new Label("Description : " + abonnement.getDescriptiona());
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Durée
        Label dureeLabel = new Label("Durée : " + abonnement.getDuree() + " jours");
        dureeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Prix
        Label prixLabel = new Label("Prix : " + abonnement.getPrix() + " €");
        prixLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Salle de sport
        Label salleLabel = new Label("Salle : " + abonnement.getSalleNom());
        salleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Boutons pour modifier, supprimer et ajouter une promotion
        HBox boutonsContainer = new HBox(10);
        boutonsContainer.setAlignment(Pos.CENTER); // Utilisation de Pos.CENTER

        // Bouton Modifier
        Button modifierButton = new Button("Modifier");
        modifierButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white;");
        modifierButton.setOnAction(event -> modifierAbonnement(abonnement));

        // Bouton Supprimer
        Button supprimerButton = new Button("Supprimer");
        supprimerButton.setStyle("-fx-background-color: #ff3333; -fx-text-fill: white;");
        supprimerButton.setOnAction(event -> supprimerAbonnement(abonnement));

        // Bouton Ajouter une promotion
        Button ajouterPromotionButton = new Button("Ajouter une promotion");
        ajouterPromotionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        ajouterPromotionButton.setOnAction(event -> ouvrirAjoutPromotion(abonnement));

        // Ajouter les boutons au conteneur
        boutonsContainer.getChildren().addAll(modifierButton, supprimerButton, ajouterPromotionButton);

        // Ajouter les éléments à la carte
        carte.getChildren().addAll(nomLabel, descriptionLabel, dureeLabel, prixLabel, salleLabel, boutonsContainer);

        return carte;
    }*/
    private void supprimerAbonnement(Abonnement abonnement) {
        System.out.println("SupprimerAbonnement appelé pour : " + abonnement.getNom()); // Debug
        try {
            // Supprimer de la base de données
            abonnementService.delete(abonnement.getId());

            // Rafraîchir l'affichage
            actualiser();

            System.out.println("Abonnement supprimé avec succès !"); // Debug
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage()); // Debug
            e.printStackTrace();

            // Afficher une alerte en cas d'erreur
            showAlert("Erreur", "Une erreur est survenue lors de la suppression de l'abonnement.", Alert.AlertType.ERROR);
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


    private void ouvrirAjoutPromotion(Abonnement abonnement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPromotion.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du formulaire d'ajout de promotion
            AjouterPromotion controller = loader.getController();
            controller.setAbonnementId(abonnement.getId()); // Passer l'ID de l'abonnement

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une promotion");
            stage.show();

            // Rafraîchir l'affichage après l'ajout
            stage.setOnHidden(event -> actualiser());
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout de promotion.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private void actualiser() {
        try {
            List<Abonnement> abonnements = abonnementService.readAll();
            afficherAbonnements(abonnements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setAjouterRoot(Parent ajouterRoot) {
        this.ajouterRoot = ajouterRoot;
    }
    @FXML
    private void ouvrirAjout() {
        try {
            // Charger le fichier FXML pour ajouter un abonnement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAbonnement.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un abonnement");
            stage.show();

            // Rafraîchir l'affichage après la fermeture de la fenêtre
            stage.setOnHidden(event -> actualiser());
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout d'abonnement.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    @FXML
    private void ouvrirAjoutPromotion() {
        try {
            // Charger le fichier FXML pour ajouter une promotion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPromotion.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une promotion");
            stage.show();

            // Rafraîchir l'affichage après la fermeture de la fenêtre
            stage.setOnHidden(event -> actualiser());
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout de promotion.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}