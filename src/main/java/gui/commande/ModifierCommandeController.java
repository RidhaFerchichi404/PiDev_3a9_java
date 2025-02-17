package gui.commande;

import entities.Commande;
import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.services.CommandeService;
import services.services.ProduitService;
import javafx.collections.FXCollections;

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
        populateFields();
    }

    private void populateFields() {
        try {
            Produit produit = produitService.readById(commande.getIdProduit());
            produitCombo.setValue(produit);
            nomClientField.setText(commande.getNomClient());
            telephoneField.setText(commande.getTelephone());
            adresseLivraisonField.setText(commande.getAdresseLivraison());
            quantiteField.setText(String.valueOf(commande.getQuantite()));
            statutCombo.setValue(commande.getStatutCommande());
        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les données de la commande");
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Validation
            if (produitCombo.getValue() == null || nomClientField.getText().isEmpty() || 
                telephoneField.getText().isEmpty() || adresseLivraisonField.getText().isEmpty() || 
                quantiteField.getText().isEmpty()) {
                showError("Erreur de validation", "Tous les champs sont obligatoires");
                return;
            }

            commande.setIdProduit(produitCombo.getValue().getIdProduit());
            commande.setNomClient(nomClientField.getText());
            commande.setTelephone(telephoneField.getText());
            commande.setAdresseLivraison(adresseLivraisonField.getText());
            commande.setQuantite(Integer.parseInt(quantiteField.getText()));
            commande.setStatutCommande(statutCombo.getValue());

            commandeService.update(commande);
            closeWindow();
        } catch (NumberFormatException e) {
            showError("Erreur de format", "La quantité doit être un nombre valide");
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de la modification de la commande: " + e.getMessage());
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
        ((Stage) nomClientField.getScene().getWindow()).close();
    }
} 