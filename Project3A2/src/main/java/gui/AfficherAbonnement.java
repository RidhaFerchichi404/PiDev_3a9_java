package gui;

import entities.Abonnement;
import entities.Promotion;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.math.RoundingMode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos; // Import manquant
import javafx.scene.Node;
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
import services.PromotionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class AfficherAbonnement {

    @FXML
    private FlowPane abonnementsContainer;
    @FXML
    private FlowPane cardsContainer;
    @FXML
    private TextField searchField;

    private Parent ajouterRoot; // Ajoutez ce champ

    private AbonnementService abonnementService = new AbonnementService();
    private Guide guide;
    // Déclaration de la liste observable
    private ObservableList<Abonnement> abonnementList;
    private VBox mainContainer;
    // Ajoutez ce champ
    // Méthode pour initialiser le guide après que la scène est prête
    public void initialiserGuide() {
        Stage primaryStage = (Stage) abonnementsContainer.getScene().getWindow();
        guide = new Guide(primaryStage);
    }

    // Méthode pour ouvrir le guide d'ajout d'abonnement
    @FXML
    private void ouvrirGuideAjoutAbonnement() {
        if (guide != null) {
            guide.afficherGuideAjoutAbonnement();
            // Surligner le bouton "Ajouter un abonnement" si nécessaire
            //guide.surlignerElement(boutonAjouterAbonnement);
        }
    }
    public static long calculerDureePromotion(LocalDate dateDebut, LocalDate dateFin) {
        return ChronoUnit.DAYS.between(dateDebut, dateFin);
    }
    // Méthode pour ouvrir le guide d'ajout de promotion
    @FXML
    private void ouvrirGuideAjoutPromotion() {
        if (guide != null) {
            guide.afficherGuideAjoutPromotion();
            // Surligner le bouton "Ajouter une promotion" si nécessaire
            // guide.surlignerElement(boutonAjouterPromotion);
        }
    }

    // Méthode pour effacer le surlignage
    @FXML
    private void effacerSurlignage() {
        if (guide != null) {
            guide.effacerSurlignage();
        }
    }

    // Ajoutez cette méthode
    public void setMainContainer(VBox mainContainer) {
        this.mainContainer = mainContainer;
    }
    @FXML
    public void initialize() {
        // Initialiser le guide avec la fenêtre principale

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
       carte.setStyle("-fx-background-color: #262626; -fx-padding: 20; -fx-border-color: #ff8c00; -fx-border-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 0);");

       // Titre de l'abonnement
       Label nomLabel = new Label(abonnement.getNom());
       nomLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 18px; -fx-font-weight: bold;");

       // Informations de l'abonnement
       VBox infosContainer = new VBox(5);
       infosContainer.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-border-radius: 5;");

       Label descriptionLabel = new Label("Description : " + abonnement.getDescriptiona());
       descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

       Label dureeLabel = new Label("Durée : " + abonnement.getDuree() + " mois");
       dureeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

       Label prixLabel = new Label("Prix : " + abonnement.getPrix() + " DT");
       prixLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

       Label salleLabel = new Label("Salle : " + abonnement.getSalleNom());
       salleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

       infosContainer.getChildren().addAll(descriptionLabel, dureeLabel, prixLabel, salleLabel);

       // Affichage des promotions
       VBox promotionsContainer = new VBox(5);
       promotionsContainer.setStyle("-fx-padding: 10; -fx-background-color: #444444; -fx-border-color: #ff8c00; -fx-border-radius: 5;");

       Label promotionsTitle = new Label("Promotions :");
       promotionsTitle.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 14px; -fx-font-weight: bold;");
       promotionsContainer.getChildren().add(promotionsTitle);

       List<Promotion> promotions = abonnement.getPromotions();
       if (promotions != null && !promotions.isEmpty()) {
           for (Promotion promotion : promotions) {
               VBox promotionBox = new VBox(5);
               promotionBox.setStyle("-fx-padding: 10; -fx-background-color: #555555; -fx-border-radius: 5;");

               // Code promo et réduction
               Label codePromoLabel = new Label("Code promo : " + promotion.getCodePromo());
               codePromoLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 12px;");

               // Dates de début et de fin
               LocalDate dateDebut = promotion.getDateDebut().toLocalDate();
               LocalDate dateFin = promotion.getDateFin().toLocalDate();
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

               Label datesLabel = new Label("Du " + dateDebut.format(formatter) + " au " + dateFin.format(formatter));
               datesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

               // Calculer la durée de la promotion
               long dureePromotion = ChronoUnit.DAYS.between(dateDebut, dateFin);
               Label dureePromotionLabel = new Label("Durée de la promotion : " + dureePromotion + " jours");
               dureePromotionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

               // Prix initial et prix après réduction
               BigDecimal prixInitial = BigDecimal.valueOf(abonnement.getPrix()); // Convertir en BigDecimal
               BigDecimal reduction = promotion.getValeurReduction(); // Récupérer la réduction (BigDecimal)
               BigDecimal cent = new BigDecimal(100);

               // Calculer le prix après réduction
               BigDecimal prixApresReduction = prixInitial.multiply(BigDecimal.ONE.subtract(reduction.divide(cent, 2, RoundingMode.HALF_UP)));

               HBox prixContainer = new HBox(5);
               Label prixInitialLabel = new Label(String.format("%.2f DT", prixInitial));
               prixInitialLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-strikethrough: true;");

               Label prixApresReductionLabel = new Label(String.format("→ %.2f DT", prixApresReduction));
               prixApresReductionLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 14px; -fx-font-weight: bold;");

               prixContainer.getChildren().addAll(prixInitialLabel, prixApresReductionLabel);

               // Boutons pour modifier et supprimer la promotion
               HBox boutonsPromotionContainer = new HBox(10);
               boutonsPromotionContainer.setAlignment(Pos.CENTER);

               Button modifierPromotionButton = new Button("Modifier");
               modifierPromotionButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 10px;");
               modifierPromotionButton.setOnMouseEntered(e -> modifierPromotionButton.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 10px;"));
               modifierPromotionButton.setOnMouseExited(e -> modifierPromotionButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 10px;"));
               modifierPromotionButton.setOnAction(event -> modifierPromotion(promotion));

               Button supprimerPromotionButton = new Button("Supprimer");
               supprimerPromotionButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 10px;");
               supprimerPromotionButton.setOnMouseEntered(e -> supprimerPromotionButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 10px;"));
               supprimerPromotionButton.setOnMouseExited(e -> supprimerPromotionButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 10px;"));
               supprimerPromotionButton.setOnAction(event -> supprimerPromotion(promotion));

               boutonsPromotionContainer.getChildren().addAll(modifierPromotionButton, supprimerPromotionButton);

               // Ajouter les éléments à la promotionBox
               promotionBox.getChildren().addAll(codePromoLabel, datesLabel, dureePromotionLabel, prixContainer, boutonsPromotionContainer);
               promotionsContainer.getChildren().add(promotionBox);
           }
       } else {
           Label noPromotionLabel = new Label("Aucune promotion disponible.");
           noPromotionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
           promotionsContainer.getChildren().add(noPromotionLabel);
       }

       // Boutons pour modifier, supprimer et ajouter une promotion
       HBox boutonsContainer = new HBox(10);
       boutonsContainer.setAlignment(Pos.CENTER);

       Button modifierButton = new Button("Modifier");
       modifierButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;");
       modifierButton.setOnMouseEntered(e -> modifierButton.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;"));
       modifierButton.setOnMouseExited(e -> modifierButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;"));
       modifierButton.setOnAction(event -> modifierAbonnement(abonnement));

       Button supprimerButton = new Button("Supprimer");
       supprimerButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;");
       supprimerButton.setOnMouseEntered(e -> supprimerButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;"));
       supprimerButton.setOnMouseExited(e -> supprimerButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;"));
       supprimerButton.setOnAction(event -> supprimerAbonnement(abonnement));

       Button ajouterPromotionButton = new Button("Ajouter une promotion");
       ajouterPromotionButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;");
       ajouterPromotionButton.setOnMouseEntered(e -> ajouterPromotionButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;"));
       ajouterPromotionButton.setOnMouseExited(e -> ajouterPromotionButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5 10;"));
       ajouterPromotionButton.setOnAction(event -> ouvrirAjoutPromotion(abonnement));

       boutonsContainer.getChildren().addAll(modifierButton, supprimerButton, ajouterPromotionButton);

       // Ajouter les éléments à la carte
       carte.getChildren().addAll(nomLabel, infosContainer, promotionsContainer, boutonsContainer);

       return carte;
   }
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
            Stage currentStage = (Stage) abonnementsContainer.getScene().getWindow(); // Utilisez abonnementsContainer au lieu de cardsContainer
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

    @FXML
    private void handleVoirPromotionsButtonClick() {
        try {
            // Charger la vue AbonnementsAvecPromotions.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichageuser.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) abonnementsContainer.getScene().getWindow();

            // Changer la scène pour afficher AbonnementsAvecPromotions
            stage.setScene(new Scene(root));
            stage.setTitle("Abonnements avec Promotions");
            stage.show();
        } catch (IOException e) {
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
    private void modifierPromotion(Promotion promotion) {
        try {
            // Charger le formulaire de modification de promotion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPromotion.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du formulaire de modification
            ModifierPromotion controller = loader.getController();
            controller.chargerPromotion(promotion); // Charger les données de la promotion

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la promotion");
            stage.show();

            // Rafraîchir l'affichage après la modification
            stage.setOnHidden(event -> actualiser());
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de modification de promotion.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private void supprimerPromotion(Promotion promotion) {
        try {
            // Supprimer la promotion de la base de données
            PromotionService promotionService = new PromotionService();
            promotionService.delete(promotion.getPromotionId());

            // Rafraîchir l'affichage
            actualiser();

            showAlert("Succès", "La promotion a été supprimée avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression de la promotion.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

}