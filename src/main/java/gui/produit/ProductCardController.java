package gui.produit;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import java.text.NumberFormat;
import java.util.Locale;

public class ProductCardController {
    @FXML private ImageView productImage;
    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label priceLabel;
    @FXML private Label stockLabel;
    @FXML private Label statusLabel;

    private Produit produit;
    private ListProduitsController parentController;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("fr", "TN"));

    public void setProduit(Produit produit) {
        this.produit = produit;
        updateCardContent();
    }

    public void setParentController(ListProduitsController controller) {
        this.parentController = controller;
    }

    private void updateCardContent() {
        // Set default product image
        productImage.setImage(null); // You can set a default product image here
        
        nameLabel.setText(produit.getNom());
        categoryLabel.setText(produit.getCategorie());
        priceLabel.setText(currencyFormat.format(produit.getPrix()));
        stockLabel.setText("Stock: " + produit.getQuantiteStock());
        
        // Update status label
        statusLabel.setText(produit.isDisponible() ? "Disponible" : "Non disponible");
        statusLabel.getStyleClass().removeAll("status-available", "status-unavailable");
        statusLabel.getStyleClass().add(produit.isDisponible() ? "status-available" : "status-unavailable");
    }

    @FXML
    private void handleEdit() {
        parentController.openEditProduit(produit);
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le produit");
        alert.setContentText("Voulez-vous vraiment supprimer " + produit.getNom() + " ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            parentController.deleteProduit(produit);
        }
    }
} 