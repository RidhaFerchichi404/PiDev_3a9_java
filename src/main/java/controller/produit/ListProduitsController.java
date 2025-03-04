package controller.produit;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ProduitService;
import java.io.IOException;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.scene.Parent;
import controller.commande.AjouterCommandeController;
import utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListProduitsController {
    @FXML private FlowPane cardsContainer;
    @FXML private Button addProductButton;
    @FXML private Button refreshButton;
    @FXML private TextField searchField;
    private final ProduitService produitService = new ProduitService();
    private ObservableList<Produit> allProducts;

    @FXML
    public void initialize() {
        System.out.println("Initializing ListProduitsController...");
        updateButtonVisibility();
        loadProduits();
        
        // Add search field listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });
    }

    private void filterProducts(String searchText) {
        if (allProducts == null) return;
        
        cardsContainer.getChildren().clear();
        
        if (searchText == null || searchText.isEmpty()) {
            updateCardView(allProducts);
            return;
        }
        
        List<Produit> filteredProducts = allProducts.stream()
            .filter(produit -> 
                produit.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                produit.getCategorie().toLowerCase().contains(searchText.toLowerCase()) ||
                produit.getDescription().toLowerCase().contains(searchText.toLowerCase()))
            .toList();
            
        updateCardView(filteredProducts);
    }

    private void updateButtonVisibility() {
        String userRole = Session.getRole();
        System.out.println("ListProduitsController - Current user role: " + userRole);
        
        if ("ADMIN".equalsIgnoreCase(userRole)) {
            System.out.println("Setting visibility for admin - showing Add Product button");
            addProductButton.setVisible(true);
        } else {
            System.out.println("Setting visibility for client/coach - hiding Add Product button");
            addProductButton.setVisible(false);
        }
        System.out.println("Add Product button visibility: " + addProductButton.isVisible());
    }

    private void loadProduits() {
        try {
            System.out.println("Loading products from database...");
            allProducts = FXCollections.observableArrayList(produitService.readAll());
            System.out.println("Found " + allProducts.size() + " products");
            System.out.println("Current user role: " + Session.getRole());
            updateCardView(allProducts);
        } catch (Exception e) {
            System.err.println("Error loading products: " + e.getMessage());
            e.printStackTrace();
            showError("Erreur de chargement", e.getMessage());
        }
    }

    private void updateCardView(List<Produit> products) {
        cardsContainer.getChildren().clear();
        System.out.println("Updating card view with " + products.size() + " products");
        System.out.println("Current user role while updating cards: " + Session.getRole());
        
        for (Produit produit : products) {
            try {
                System.out.println("Creating card for product: " + produit.getNom());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/produit/ProductCard.fxml"));
                VBox card = loader.load();
                ProductCardController controller = loader.getController();
                controller.setProduit(produit);
                controller.setParentController(this);
                cardsContainer.getChildren().add(card);
                System.out.println("Successfully added card for product: " + produit.getNom());
            } catch (IOException e) {
                System.err.println("Error creating card for product: " + produit.getNom());
                System.err.println("Error details: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void refreshProducts() {
        loadProduits();
    }

    @FXML
    private void openAddProduit() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/produit/AjouterProduit.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Ajouter Produit");
            stage.setScene(scene);
            stage.showAndWait();
            loadProduits();
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre d'ajout");
        }
    }

    public void openEditProduit(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/produit/ModifierProduit.fxml"));
            Scene scene = new Scene(loader.load());
            ModifierProduitController controller = loader.getController();
            controller.setProduit(produit);
            Stage stage = new Stage();
            stage.setTitle("Modifier Produit");
            stage.setScene(scene);
            stage.showAndWait();
            loadProduits();
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de modification");
        }
    }

    public void deleteProduit(Produit produit) {
        try {
            produitService.delete(produit);
            loadProduits();
        } catch (Exception e) {
            showError("Erreur de suppression", e.getMessage());
        }
    }

    public void handleCommander(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/commande/AjouterCommande.fxml"));
            Parent addCommandeView = loader.load();

            // Pass the selected product to the command creation controller
            AjouterCommandeController addCommandeController = loader.getController();
            addCommandeController.setProduit(produit);

            Stage stage = new Stage();
            stage.setTitle("Ajouter Commande");
            stage.setScene(new Scene(addCommandeView));
            stage.showAndWait();

            loadProduits(); // Refresh the product list after adding a command
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load AjouterCommande.fxml");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 