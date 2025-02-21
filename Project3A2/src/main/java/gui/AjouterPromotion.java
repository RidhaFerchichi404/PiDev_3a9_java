package gui;

import entities.Promotion;
import services.PromotionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AjouterPromotion {

    @FXML private TextField codePromoField;
    @FXML private TextField descriptionField;
    @FXML private TextField typeReductionField;
    @FXML private TextField valeurReductionField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private TextField abonnementIdField;

    private PromotionService promotionService;

    public AjouterPromotion() {
        promotionService = new PromotionService();
    }

    @FXML
    public void initialize() {
        if (abonnementIdField == null) {
            System.out.println("Erreur : abonnementIdField n'a pas été injecté.");
        } else {
            System.out.println("abonnementIdField a été injecté avec succès.");
        }
    }

    @FXML
    public void addPromotion() {
        // Vérifier que tous les champs sont injectés
        if (abonnementIdField == null) {
            showAlert("Erreur", "Le champ abonnementIdField n'a pas été initialisé.");
            return;
        }

        // Récupération des valeurs des champs
        String codePromo = codePromoField.getText();
        String description = descriptionField.getText();
        String typeReduction = typeReductionField.getText();
        String valeurReductionStr = valeurReductionField.getText();
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        String abonnementIdStr = abonnementIdField.getText();

        // Validation des champs
        if (codePromo.isEmpty() || description.isEmpty() || typeReduction.isEmpty() || valeurReductionStr.isEmpty() || abonnementIdStr.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Conversion et validation des valeurs
        BigDecimal valeurReduction;
        try {
            valeurReduction = new BigDecimal(valeurReductionStr);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La valeur de réduction doit être un nombre valide.");
            return;
        }

        int abonnementId;
        try {
            abonnementId = Integer.parseInt(abonnementIdStr);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID d'abonnement doit être un nombre entier.");
            return;
        }

        // Création de la promotion
        Promotion promotion = new Promotion(codePromo, description, typeReduction, valeurReduction, Date.valueOf(dateDebut), Date.valueOf(dateFin), abonnementId);

        try {
            promotionService.create(promotion);
            showAlert("Succès", "La promotion a été ajoutée avec succès.");
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de la promotion.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}