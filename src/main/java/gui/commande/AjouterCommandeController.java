package gui.commande;

import entities.Commande;
import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.services.CommandeService;
import services.services.ProduitService;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;

public class AjouterCommandeController {
    @FXML private ComboBox<Produit> produitCombo;
    @FXML private TextField nomClientField;
    @FXML private TextField telephoneField;
    @FXML private TextArea adresseLivraisonField;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> statutCombo;

    private final CommandeService commandeService = new CommandeService();
    private final ProduitService produitService = new ProduitService();

    @FXML
    public void initialize() {
        // Initialize status choices
        statutCombo.setItems(FXCollections.observableArrayList(
            "En attente", "En cours", "Livrée", "Annulée"
        ));
        statutCombo.setValue("En attente");

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

            Commande commande = new Commande(
                produitCombo.getValue().getIdProduit(),
                nomClientField.getText(),
                adresseLivraisonField.getText(),
                telephoneField.getText(),
                Integer.parseInt(quantiteField.getText()),
                LocalDateTime.now(),
                statutCombo.getValue()
            );

            commandeService.create(commande);
            closeWindow();
        } catch (NumberFormatException e) {
            showError("Erreur de format", "La quantité doit être un nombre valide");
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de la création de la commande: " + e.getMessage());
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