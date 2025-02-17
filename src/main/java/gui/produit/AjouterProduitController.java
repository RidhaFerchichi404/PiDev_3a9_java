package gui.produit;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.services.ProduitService;
import java.math.BigDecimal;

public class AjouterProduitController {
    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private TextField categorieField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private CheckBox disponibleCheck;

    private final ProduitService produitService = new ProduitService();

    @FXML
    private void handleSave() {
        try {
            // Validation
            if (nomField.getText().isEmpty() || categorieField.getText().isEmpty() || 
                prixField.getText().isEmpty() || stockField.getText().isEmpty()) {
                showError("Erreur de validation", "Tous les champs sont obligatoires");
                return;
            }

            Produit produit = new Produit(
                nomField.getText(),
                descriptionField.getText(),
                categorieField.getText(),
                new BigDecimal(prixField.getText()),
                Integer.parseInt(stockField.getText()),
                disponibleCheck.isSelected()
            );

            produitService.create(produit);
            closeWindow();
        } catch (NumberFormatException e) {
            showError("Erreur de format", "Prix et stock doivent être des nombres valides");
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de la création du produit: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        ((Stage) nomField.getScene().getWindow()).close();
    }
} 