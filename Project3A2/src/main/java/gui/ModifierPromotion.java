package gui;

import entities.Promotion;
import services.PromotionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Date;

public class ModifierPromotion {

    @FXML private TextField codePromoField;
    @FXML private TextField descriptionField;
    @FXML private TextField typeReductionField;
    @FXML private TextField valeurReductionField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;

    private PromotionService promotionService;

    public ModifierPromotion() {
        promotionService = new PromotionService();
    }

    // Méthode pour mettre à jour une promotion
   /* @FXML
    public void updatePromotion() {
        Promotion promotion = new Promotion();
        promotion.setCodePromo(codePromoField.getText());
        promotion.setDescription(descriptionField.getText());
        promotion.setTypeReduction(typeReductionField.getText());
        promotion.setValeurReduction(new BigDecimal(valeurReductionField.getText()));
        promotion.setDateDebut(String.valueOf(Date.valueOf(dateDebutPicker.getValue())));
        promotion.setDateFin(String.valueOf(Date.valueOf(dateFinPicker.getValue())));

        // Assigner l'ID de la promotion que l'on souhaite modifier
        promotion.setPromotionId(1); // Exemple, à adapter selon le cas

        try {
            promotionService.update(promotion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
