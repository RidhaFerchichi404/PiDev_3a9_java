package gui;

import entities.Abonnement;
import entities.Promotion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import services.AbonnementService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class userAffichage {

    @FXML
    private FlowPane abonnementsContainer;

    private AbonnementService abonnementService = new AbonnementService();

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
        carte.setStyle("-fx-background-color: #262626; -fx-padding: 20; -fx-border-color: #ff8c00; -fx-border-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 0);");

        // Titre de l'abonnement
        Label nomLabel = new Label(abonnement.getNom());
        nomLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Informations de l'abonnement
        VBox infosContainer = new VBox(5);
        infosContainer.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-border-radius: 5;");

        Label descriptionLabel = new Label("Description : " + abonnement.getDescriptiona());
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label dureeLabel = new Label("Durée : " + abonnement.getDuree() + " jours");
        dureeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label prixLabel = new Label("Prix : " + abonnement.getPrix() + " €");
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
                Label datesLabel = new Label("Du " + promotion.getDateDebut() + " au " + promotion.getDateFin());
                datesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

                // Prix initial et prix après réduction
                BigDecimal prixInitial = BigDecimal.valueOf(abonnement.getPrix()); // Convertir le prix en BigDecimal
                BigDecimal reduction = promotion.getValeurReduction(); // Récupérer la réduction (BigDecimal)
                BigDecimal cent = new BigDecimal(100);

                // Calculer le prix après réduction
                BigDecimal prixApresReduction = prixInitial.multiply(
                        BigDecimal.ONE.subtract(reduction.divide(cent, 2, RoundingMode.HALF_UP))
                );

                Label prixApresReductionLabel = new Label(String.format("Prix après réduction : %.2f €", prixApresReduction.doubleValue()));
                prixApresReductionLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 14px; -fx-font-weight: bold;");

                promotionBox.getChildren().addAll(codePromoLabel, datesLabel, prixApresReductionLabel);
                promotionsContainer.getChildren().add(promotionBox);
            }
        } else {
            Label noPromotionLabel = new Label("Aucune promotion disponible.");
            noPromotionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
            promotionsContainer.getChildren().add(noPromotionLabel);
        }

        // Ajouter les éléments à la carte
        carte.getChildren().addAll(nomLabel, infosContainer, promotionsContainer);

        return carte;
    }
}