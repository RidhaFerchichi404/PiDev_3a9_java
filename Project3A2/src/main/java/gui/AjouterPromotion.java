package gui;

import entities.Promotion;
import services.PromotionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Date;

public class AjouterPromotion {

    @FXML private TextField codePromoField;
    @FXML private TextField descriptionField;
    @FXML private TextField typeReductionField;
    @FXML private TextField valeurReductionField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private TextField abonnementIdField; // Ajout de l'ID d'abonnement comme champ

    private PromotionService promotionService;

    public AjouterPromotion() {
        promotionService = new PromotionService();
    }

    @FXML
    public void addPromotion() {
        // Récupération des valeurs des champs
        String codePromo = codePromoField.getText();
        String description = descriptionField.getText();
        String typeReduction = typeReductionField.getText();
        String valeurReductionStr = valeurReductionField.getText();
        Date dateDebut = Date.valueOf(dateDebutPicker.getValue());
        Date dateFin = Date.valueOf(dateFinPicker.getValue());
        String abonnementIdStr = abonnementIdField.getText();

        // Validation des champs
        if (codePromo.isEmpty() || description.isEmpty() || typeReduction.isEmpty() || valeurReductionStr.isEmpty() || abonnementIdStr.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Conversion de la valeur de réduction en BigDecimal
        BigDecimal valeurReduction;
        try {
            valeurReduction = new BigDecimal(valeurReductionStr);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La valeur de réduction doit être un nombre valide.");
            return;
        }

        // Validation de l'ID d'abonnement
        int abonnementId;
        try {
            abonnementId = Integer.parseInt(abonnementIdStr);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID d'abonnement doit être un nombre entier.");
            return;
        }


        // Vérifie si l'ID d'abonnement existe dans la base de données
        if (!isAbonnementExist(abonnementId)) {
            showAlert("Erreur", "L'ID d'abonnement n'existe pas.");
            return;
        }

        // Création de la promotion

        Promotion promotion = new Promotion(codePromo, description, typeReduction, valeurReduction, dateDebut, dateFin, abonnementId );

        try {
            // Appel au service pour ajouter la promotion
            promotionService.create(promotion);
            showAlert("Succès", "La promotion a été ajoutée avec succès.");
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de la promotion.");
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier si l'ID d'abonnement existe dans la base de données
    private boolean isAbonnementExist(int abonnementId) {
        // Ici tu peux interroger ta base de données pour vérifier si l'abonnement existe
        // Exemple de code à implémenter dans PromotionService :
        // SELECT COUNT(*) FROM abonnement WHERE AbonnementID = ?
        // Retourne true si l'abonnement existe.
        return true; // Exemple, à remplacer par une réelle logique de vérification
    }

    // Méthode pour afficher les alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
