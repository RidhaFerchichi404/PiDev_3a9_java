package gui;

import entities.Promotion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.PromotionService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class ModifierPromotion {

    @FXML
    private TextField codePromoField;

    @FXML
    private TextField valeurReductionField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private Button enregistrerButton; // Assurez-vous que cette variable est annotée avec @FXML

    private Promotion promotion; // Promotion à modifier
    private PromotionService promotionService = new PromotionService(); // Service pour gérer les promotions

    /**
     * Charge les données de la promotion dans le formulaire.
     *
     * @param promotion La promotion à modifier.
     */
    public void chargerPromotion(Promotion promotion) {
        this.promotion = promotion;

        // Remplir les champs avec les données de la promotion
        codePromoField.setText(promotion.getCodePromo());
        valeurReductionField.setText(promotion.getValeurReduction().toString());
        dateDebutPicker.setValue(promotion.getDateDebut().toLocalDate());
        dateFinPicker.setValue(promotion.getDateFin().toLocalDate());
    }

    /**
     * Enregistre les modifications de la promotion dans la base de données.
     */
    @FXML
    private void enregistrerModifications() {
        try {
            // Récupérer les nouvelles valeurs des champs
            String codePromo = codePromoField.getText();
            BigDecimal valeurReduction = new BigDecimal(valeurReductionField.getText());
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            // Mettre à jour l'objet promotion
            promotion.setCodePromo(codePromo);
            promotion.setValeurReduction(valeurReduction);
            promotion.setDateDebut(java.sql.Date.valueOf(dateDebut));
            promotion.setDateFin(java.sql.Date.valueOf(dateFin));

            // Mettre à jour la promotion dans la base de données
            promotionService.update(promotion);

            // Afficher un message de succès
            showAlert("Succès", "La promotion a été modifiée avec succès.", Alert.AlertType.INFORMATION);

            // Fermer la fenêtre de modification
            Stage stage = (Stage) enregistrerButton.getScene().getWindow(); // Utiliser enregistrerButton pour fermer la fenêtre
            stage.close();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La valeur de réduction doit être un nombre valide.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification de la promotion.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     *
     * @param title   Le titre de l'alerte.
     * @param message Le message de l'alerte.
     * @param type    Le type d'alerte.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}