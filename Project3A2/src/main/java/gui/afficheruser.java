package gui;

import entities.Abonnement;
import entities.Promotion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.AbonnementService;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.exception.StripeException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class afficheruser {

    @FXML
    private FlowPane abonnementsContainer;

    private AbonnementService abonnementService = new AbonnementService();
    private Abonnement selectedAbonnement; // Variable pour stocker l'abonnement sélectionné

    @FXML
    public void initialize() {
        try {
            // Charger tous les abonnements (sans filtre)
            List<Abonnement> abonnements = abonnementService.readAll();

            // Afficher tous les abonnements
            afficherAbonnements(abonnements);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedAbonnement(Abonnement abonnement) {
        this.selectedAbonnement = abonnement;
    }

    private void afficherAbonnements(List<Abonnement> abonnements) {
        abonnementsContainer.getChildren().clear();

        for (Abonnement abonnement : abonnements) {
            VBox carteAbonnement = creerCarteAbonnementUser(abonnement); // Utiliser la nouvelle méthode
            abonnementsContainer.getChildren().add(carteAbonnement);
        }
    }

    private VBox creerCarteAbonnementUser(Abonnement abonnement) {
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

        // Affichage des promotions (si elles existent)
        VBox promotionsContainer = new VBox(5);
        promotionsContainer.setStyle("-fx-padding: 10; -fx-background-color: #444444; -fx-border-color: #ff8c00; -fx-border-radius: 5;");

        List<Promotion> promotions = abonnement.getPromotions();
        if (promotions != null && !promotions.isEmpty()) {
            Label promotionsTitle = new Label("Promotions :");
            promotionsTitle.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 14px; -fx-font-weight: bold;");
            promotionsContainer.getChildren().add(promotionsTitle);

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

                // Ajouter les éléments à la promotionBox
                promotionBox.getChildren().addAll(codePromoLabel, datesLabel, dureePromotionLabel, prixContainer);
                promotionsContainer.getChildren().add(promotionBox);
            }
        } else {
            Label noPromotionLabel = new Label("Aucune promotion disponible.");
            noPromotionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
            promotionsContainer.getChildren().add(noPromotionLabel);
        }

        // Bouton de paiement
        Button payerButton = new Button("Payer");
        payerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        payerButton.setOnAction(event -> {
            setSelectedAbonnement(abonnement); // Définir l'abonnement sélectionné
            handlePayerButtonClick(event); // Appeler la méthode handlePayerButtonClick
        });

        // Ajouter les éléments à la carte
        carte.getChildren().addAll(nomLabel, infosContainer, promotionsContainer, payerButton);
        return carte;
    }

    @FXML
    private void handlePayerButtonClick(ActionEvent event) {
        if (selectedAbonnement != null) {
            System.out.println("Paiement en cours pour l'abonnement : " + selectedAbonnement.getNom());

            // Configurer la clé secrète Stripe
            Stripe.apiKey = "sk_test_51QxPCdPteRZuLQ9NZxF9VY6qk1pqyH8vZRy4SJdpf0jUkXg3BI7dufkWEOPq4OdHgBifaE9GwTWKoVuR11Xyc8Kf00QmWXFn42"; // Remplacez par votre clé secrète Stripe

            // Convertir le prix en cents (Stripe utilise des montants en cents)
            long amount = (long) (selectedAbonnement.getPrix() * 100);

            try {
                // Créer un PaymentIntent
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(amount) // Montant en cents
                        .setCurrency("eur") // Devise (euro)
                        .build();

                PaymentIntent paymentIntent = PaymentIntent.create(params);

                System.out.println("PaymentIntent créé : " + paymentIntent.getId());

                // Ouvrir la vue de paiement
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Paiement.fxml"));
                Parent root = loader.load();

                // Passer l'abonnement au contrôleur de la vue de paiement
                PaiementController paiementController = loader.getController();
                paiementController.setAbonnement(selectedAbonnement);

                // Afficher la vue de paiement
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Paiement");
                stage.show();
            } catch (StripeException | IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la création du PaymentIntent : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun abonnement sélectionné.");
        }
    }

    @FXML
    private void handleRetourButtonClick() {
        try {
            // Charger la vue AfficherAbonnement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAbonnement.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) abonnementsContainer.getScene().getWindow();

            // Changer la scène pour afficher AfficherAbonnement
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Abonnements");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}