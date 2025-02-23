package controller.commande;

import entities.Commande;
import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.CommandeService;
import services.ProduitService;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import utils.Session;

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
        
        // If not admin, disable status combo box and force "En attente"
        String userRole = Session.getRole();
        if (!userRole.equalsIgnoreCase("ADMIN")) {
            statutCombo.setDisable(true);
            statutCombo.setValue("En attente");
        }
        
        // Style the status combo box
        statutCombo.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        statutCombo.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black;");
                }
            }
        });

        // Load products into combo box
        try {
            produitCombo.setItems(FXCollections.observableArrayList(
                produitService.readAll()
            ));
            
            // Style the product combo box
            produitCombo.setButtonCell(new ListCell<Produit>() {
                @Override
                protected void updateItem(Produit produit, boolean empty) {
                    super.updateItem(produit, empty);
                    if (empty || produit == null) {
                        setText(null);
                    } else {
                        setText(produit.getNom());
                        setStyle("-fx-text-fill: black;");
                    }
                }
            });
            
            produitCombo.setCellFactory(lv -> new ListCell<Produit>() {
                @Override
                protected void updateItem(Produit produit, boolean empty) {
                    super.updateItem(produit, empty);
                    if (empty || produit == null) {
                        setText(null);
                    } else {
                        setText(produit.getNom());
                        setStyle("-fx-text-fill: black; -fx-background-color: white;");
                    }
                }
            });
        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les produits");
        }
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
            int quantite = Integer.parseInt(quantiteField.getText().trim());
            if (quantite <= 0) {
                errors.append("- La quantité doit être supérieure à 0\n");
            }
            
            // Vérifier si la quantité est disponible en stock
            Produit produit = produitCombo.getValue();
            if (produit != null && quantite > produit.getQuantiteStock()) {
                errors.append("- La quantité demandée dépasse le stock disponible\n");
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

    @FXML
    private void handleSave() {
        try {
            String validationErrors = validateInputs();
            if (!validationErrors.isEmpty()) {
                showError("Erreur de validation", validationErrors);
                return;
            }

            Commande commande = new Commande(
                produitCombo.getValue().getIdProduit(),
                Session.getCurrentUser().getId(),
                nomClientField.getText().trim(),
                adresseLivraisonField.getText().trim(),
                telephoneField.getText().trim(),
                Integer.parseInt(quantiteField.getText().trim()),
                LocalDateTime.now(),
                statutCombo.getValue()
            );

            commandeService.create(commande);
            closeWindow();
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

    public void setProduit(Produit produit) {
        this.produitCombo.setValue(produit); // Set the selected product in the ComboBox
    }
} 