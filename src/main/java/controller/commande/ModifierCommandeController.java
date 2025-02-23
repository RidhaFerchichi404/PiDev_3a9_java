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
        
        // Only allow editing if user is admin or owner
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

            commande.setIdProduit(produitCombo.getValue().getIdProduit());
            commande.setNomClient(nomClientField.getText().trim());
            commande.setAdresseLivraison(adresseLivraisonField.getText().trim());
            commande.setTelephone(telephoneField.getText().trim());
            commande.setQuantite(Integer.parseInt(quantiteField.getText().trim()));
            
            // Only allow status change if admin
            if (Session.getRole().equalsIgnoreCase("ADMIN")) {
                commande.setStatutCommande(statutCombo.getValue());
            }
            // For non-admin users, status remains unchanged
            
            commandeService.update(commande);
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
        if (produitCombo.getValue() == null || nomClientField.getText().isEmpty() || 
            telephoneField.getText().isEmpty() || adresseLivraisonField.getText().isEmpty() || 
            quantiteField.getText().isEmpty()) {
            errors.append("Tous les champs sont obligatoires\n");
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