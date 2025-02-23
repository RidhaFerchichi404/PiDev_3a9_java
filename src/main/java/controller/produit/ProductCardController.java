package controller.produit;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.text.NumberFormat;
import java.util.Locale;
import java.io.File;
import javafx.scene.control.Button;
import controller.commande.AjouterCommandeController;
import utils.Session;

public class ProductCardController {
    @FXML private ImageView productImage;
    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label priceLabel;
    @FXML private Label stockLabel;
    @FXML private Label statusLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button commanderButton;

    private Produit produit;
    private ListProduitsController parentController;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("fr", "TN"));

    public void setProduit(Produit produit) {
        this.produit = produit;
        updateCardContent();
        updateButtonVisibility();
    }

    public void setParentController(ListProduitsController controller) {
        this.parentController = controller;
    }

    private void updateCardContent() {
        // Load and set product image
        if (produit.getImagePath() != null && !produit.getImagePath().isEmpty()) {
            try {
                File imageFile = new File(produit.getImagePath());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    productImage.setImage(image);
                } else {
                    setDefaultImage();
                }
            } catch (Exception e) {
                setDefaultImage();
            }
        } else {
            setDefaultImage();
        }
        
        nameLabel.setText(produit.getNom());
        categoryLabel.setText(produit.getCategorie());
        priceLabel.setText(currencyFormat.format(produit.getPrix()));
        stockLabel.setText("Stock: " + produit.getQuantiteStock());
        
        statusLabel.setText(produit.isDisponible() ? "Disponible" : "Non disponible");
        statusLabel.getStyleClass().removeAll("status-available", "status-unavailable");
        statusLabel.getStyleClass().add(produit.isDisponible() ? "status-available" : "status-unavailable");
    }

    private void setDefaultImage() {
        try {
            // Try to load default image
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-product.png"));
            productImage.setImage(defaultImage);
        } catch (Exception e) {
            // If default image fails to load, set a placeholder background color
            productImage.setFitWidth(200);
            productImage.setFitHeight(150);
            productImage.setStyle("-fx-background-color: #333333;");
        }
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

    @FXML
    private void handleCommander() {
        parentController.handleCommander(produit);
    }

    private void updateButtonVisibility() {
        String userRole = Session.getRole();
        System.out.println("Updating button visibility for role: " + userRole);
        
        if ("ADMIN".equalsIgnoreCase(userRole)) {
            System.out.println("Setting admin button visibility:");
            editButton.setVisible(true);
            deleteButton.setVisible(true);
            commanderButton.setVisible(false);
            System.out.println("Edit button: visible, Delete button: visible, Commander button: hidden");
        } else {
            System.out.println("Setting client/coach button visibility:");
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            commanderButton.setVisible(true);
            System.out.println("Edit button: hidden, Delete button: hidden, Commander button: visible");
        }
        
        // Verify button states after setting
        System.out.println("Final button states - Edit: " + editButton.isVisible() + 
                         ", Delete: " + deleteButton.isVisible() + 
                         ", Commander: " + commanderButton.isVisible());
    }
} 