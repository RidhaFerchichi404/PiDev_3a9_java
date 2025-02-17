package gui.produit;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.services.ProduitService;
import java.io.IOException;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;

public class ListProduitsController {
    @FXML private FlowPane cardsContainer;
    private final ProduitService produitService = new ProduitService();

    @FXML
    public void initialize() {
        loadProduits();
    }

    private void loadProduits() {
        try {
            List<Produit> products = produitService.readAll();
            updateCardView(products);
        } catch (Exception e) {
            showError("Erreur de chargement", e.getMessage());
        }
    }

    private void updateCardView(List<Produit> products) {
        cardsContainer.getChildren().clear();
        for (Produit produit : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/produit/ProductCard.fxml"));
                VBox card = loader.load();
                ProductCardController controller = loader.getController();
                controller.setProduit(produit);
                controller.setParentController(this);
                cardsContainer.getChildren().add(card);
            } catch (IOException e) {
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
            produitService.delete(produit.getIdProduit());
            loadProduits();
        } catch (Exception e) {
            showError("Erreur de suppression", e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 