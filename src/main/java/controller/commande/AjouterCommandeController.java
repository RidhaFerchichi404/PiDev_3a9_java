package controller.commande;

import entities.Commande;
import entities.Produit;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.CommandeService;
import services.ProduitService;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import utils.Session;
import java.io.IOException;

public class AjouterCommandeController {
    @FXML private ComboBox<Produit> produitCombo;
    @FXML private TextField nomClientField;
    @FXML private TextArea adresseLivraisonField;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> statutCombo;
    
    // New components
    @FXML private PhoneInputController phoneInputController;
    private AddressInputController addressInputController;

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

        // Auto-fill user information
        User currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            nomClientField.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            
            // Set phone number if exists
            String phone = currentUser.getPhoneNumber();
            if (phone != null && !phone.isEmpty()) {
                phoneInputController.setPhoneNumber(phone);
            }
            
            // Set address if exists
            if (currentUser.getLocation() != null && !currentUser.getLocation().isEmpty()) {
                adresseLivraisonField.setText(currentUser.getLocation());
            }
        }

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

    @FXML
    private void showAddressDialog() {
        try {
            System.out.println("Opening address dialog...");
            // Get the correct resource URL
            String fxmlPath = "/gui/commande/AddressInput.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            
            if (loader.getLocation() == null) {
                System.err.println("Could not find FXML file at: " + fxmlPath);
                showError("Erreur", "Le fichier FXML n'a pas été trouvé: " + fxmlPath);
                return;
            }
            
            System.out.println("Loading FXML from: " + loader.getLocation());
            Parent root = loader.load();
            
            addressInputController = loader.getController();
            if (addressInputController == null) {
                System.err.println("Controller was not initialized");
                showError("Erreur", "Le contrôleur n'a pas été initialisé");
                return;
            }
            
            addressInputController.setOnAddressValidated(() -> {
                adresseLivraisonField.setText(addressInputController.getFormattedAddress());
            });
            
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Saisie d'adresse");
            Scene scene = new Scene(root);
            dialog.setScene(scene);
            
            System.out.println("Showing address dialog");
            dialog.showAndWait();
        } catch (IOException e) {
            System.err.println("Error loading address dialog: " + e.getMessage());
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir le formulaire d'adresse: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showError("Erreur", "Une erreur inattendue s'est produite: " + e.getMessage());
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
        if (!phoneInputController.isValid()) {
            errors.append("- Le numéro de téléphone est invalide\n");
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

            // Get the product and order quantity
            Produit produit = produitCombo.getValue();
            int orderQuantity = Integer.parseInt(quantiteField.getText().trim());

            // Create the order
            Commande commande = new Commande(
                produit.getIdProduit(),
                Session.getCurrentUser().getId(),
                nomClientField.getText().trim(),
                adresseLivraisonField.getText().trim(),
                phoneInputController.getFullPhoneNumber(),
                orderQuantity,
                LocalDateTime.now(),
                statutCombo.getValue()
            );

            // Update product stock
            int newStock = produit.getQuantiteStock() - orderQuantity;
            produit.setQuantiteStock(newStock);
            produit.setDisponible(newStock > 0);

            // Save both changes in a specific order to maintain data consistency
            commandeService.create(commande);
            produitService.update(produit);
            
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
        this.produitCombo.setValue(produit);
    }
} 