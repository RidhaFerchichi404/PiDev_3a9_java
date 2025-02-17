package gui.produit;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.services.ProduitService;
import java.math.BigDecimal;

public class ModifierProduitController {
    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private TextField categorieField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private CheckBox disponibleCheck;

    private final ProduitService produitService = new ProduitService();
    private Produit produit;

    public void setProduit(Produit produit) {
        this.produit = produit;
        populateFields();
    }

    private void populateFields() {
        nomField.setText(produit.getNom());
        descriptionField.setText(produit.getDescription());
        categorieField.setText(produit.getCategorie());
        prixField.setText(produit.getPrix().toString());
        stockField.setText(String.valueOf(produit.getQuantiteStock()));
        disponibleCheck.setSelected(produit.isDisponible());
    }

    @FXML
    private void handleSave() {
        try {
            // Validation
            if (nomField.getText().isEmpty() || categorieField.getText().isEmpty() || 
                prixField.getText().isEmpty() || stockField.getText().isEmpty()) {
                showError("Erreur de validation", "Tous les champs sont obligatoires");
                return;
            }

            produit.setNom(nomField.getText());
            produit.setDescription(descriptionField.getText());
            produit.setCategorie(categorieField.getText());
            produit.setPrix(new BigDecimal(prixField.getText()));
            produit.setQuantiteStock(Integer.parseInt(stockField.getText()));
            produit.setDisponible(disponibleCheck.isSelected());

            produitService.update(produit);
            closeWindow();
        } catch (NumberFormatException e) {
            showError("Erreur de format", "Prix et stock doivent être des nombres valides");
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de la modification du produit: " + e.getMessage());
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