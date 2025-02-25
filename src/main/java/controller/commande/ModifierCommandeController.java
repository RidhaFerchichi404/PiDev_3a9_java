package controller.commande;

import entities.Commande;
import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.CommandeService;
import services.ProduitService;
import javafx.collections.FXCollections;
import utils.Session;
import java.sql.SQLException;

public class ModifierCommandeController {
    @FXML private ComboBox<Produit> produitCombo;
    @FXML private TextField nomClientField;
    @FXML private TextField telephoneField;
    @FXML private TextArea adresseLivraisonField;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> statutCombo;

    private final CommandeService commandeService = new CommandeService();
    private final ProduitService produitService = new ProduitService();
    private Commande commande;

    @FXML
    public void initialize() {
        // Initialize status choices
        statutCombo.setItems(FXCollections.observableArrayList(
            "En attente", "En cours", "Livrée", "Annulée"
        ));

        // If not admin, disable status combo box
        String userRole = Session.getRole();
        if (!userRole.equalsIgnoreCase("ADMIN")) {
            statutCombo.setDisable(true);
        }

        // Load products into combo box
        try {
            produitCombo.setItems(FXCollections.observableArrayList(
                produitService.readAll()
            ));
            produitCombo.setCellFactory(lv -> new ListCell<Produit>() {
                @Override
                protected void updateItem(Produit produit, boolean empty) {
                    super.updateItem(produit, empty);
                    setText(empty ? null : produit.getNom());
                }
            });
            produitCombo.setButtonCell(new ListCell<Produit>() {
                @Override
                protected void updateItem(Produit produit, boolean empty) {
                    super.updateItem(produit, empty);
                    setText(empty ? null : produit.getNom());
                }
            });
        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les produits");
        }
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        
        // Only allow editing if user is admin or owner****************************************************
        if (!Session.getRole().equalsIgnoreCase("ADMIN") && 
            !Session.getCurrentUser().getId().equals(commande.getIdUser())) {
            showError("Accès refusé", "Vous n'avez pas la permission de modifier cette commande");
            closeWindow();
            return;
        }

        try {
            // Populate fields
            produitCombo.setValue(produitService.readById(commande.getIdProduit()));
            nomClientField.setText(commande.getNomClient());
            adresseLivraisonField.setText(commande.getAdresseLivraison());
            telephoneField.setText(commande.getTelephone());
            quantiteField.setText(String.valueOf(commande.getQuantite()));
            statutCombo.setValue(commande.getStatutCommande());
        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les données du produit: " + e.getMessage());
            closeWindow();
        }
    }

    @FXML
    private void handleSave() {
        try {
            String validationErrors = validateInputs();
            if (!validationErrors.isEmpty()) {
                showError("Erreur de validation", validationErrors);
                return;
            }

            // Get the old and new quantities
            int oldQuantity = commande.getQuantite();
            int newQuantity = Integer.parseInt(quantiteField.getText().trim());
            
            // Get the product (either the existing one or newly selected one)
            Produit produit = produitService.readById(produitCombo.getValue().getIdProduit());
            
            // Calculate stock adjustment
            int stockAdjustment = oldQuantity - newQuantity; // Positive if reducing order, negative if increasing
            int newStock = produit.getQuantiteStock() + stockAdjustment;
            
            // Update the product's stock
            produit.setQuantiteStock(newStock);
            produit.setDisponible(newStock > 0);
            
            // Update the order
            commande.setIdProduit(produitCombo.getValue().getIdProduit());
            commande.setNomClient(nomClientField.getText().trim());
            commande.setAdresseLivraison(adresseLivraisonField.getText().trim());
            commande.setTelephone(telephoneField.getText().trim());
            commande.setQuantite(newQuantity);
            
            // Only allow status change if admin
            if (Session.getRole().equalsIgnoreCase("ADMIN")) {
                commande.setStatutCommande(statutCombo.getValue());
            }
            
            // Save both changes to maintain data consistency
            commandeService.update(commande);
            produitService.update(produit);
            
            closeWindow();
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de la modification de la commande: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private String validateInputs() {
        StringBuilder errors = new StringBuilder();

        // Validation du produit sélectionné
        if (produitCombo.getValue() == null) {
            errors.append("- Veuillez sélectionner un produit\n");
        }

        // Validation du nom client
        if (nomClientField.getText().trim().isEmpty()) {
            errors.append("- Le nom du client est obligatoire\n");
        } else if (nomClientField.getText().length() < 3) {
            errors.append("- Le nom du client doit contenir au moins 3 caractères\n");
        }

        // Validation du téléphone
        String phone = telephoneField.getText().trim();
        if (phone.isEmpty()) {
            errors.append("- Le numéro de téléphone est obligatoire\n");
        } else if (!phone.matches("^216[0-9]{8}$")) {
            errors.append("- Le numéro de téléphone doit être au format: 216XXXXXXXX\n");
        }

        // Validation de l'adresse
        if (adresseLivraisonField.getText().trim().isEmpty()) {
            errors.append("- L'adresse de livraison est obligatoire\n");
        } else if (adresseLivraisonField.getText().length() < 10) {
            errors.append("- L'adresse doit être plus détaillée (min 10 caractères)\n");
        }

        // Validation de la quantité
        try {
            int newQuantity = Integer.parseInt(quantiteField.getText().trim());
            if (newQuantity <= 0) {
                errors.append("- La quantité doit être supérieure à 0\n");
            }
            
            // Vérifier si la quantité est disponible en stock
            Produit produit = produitCombo.getValue();
            if (produit != null) {
                try {
                    // Get fresh product data
                    produit = produitService.readById(produit.getIdProduit());
                    
                    // Calculate available stock (current stock + current order quantity)
                    int availableStock = produit.getQuantiteStock() + commande.getQuantite();
                    
                    // Check if new quantity exceeds available stock
                    if (newQuantity > availableStock) {
                        errors.append("- La quantité demandée dépasse le stock disponible\n");
                    }
                } catch (SQLException e) {
                    errors.append("- Erreur lors de la vérification du stock\n");
                }
            }
        } catch (NumberFormatException e) {
            errors.append("- La quantité doit être un nombre entier valide\n");
        }

        // Validation du statut
        if (statutCombo.getValue() == null) {
            errors.append("- Veuillez sélectionner un statut\n");
        }

        return errors.toString();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        ((Stage) nomClientField.getScene().getWindow()).close();
    }
} 