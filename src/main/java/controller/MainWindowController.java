package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainWindowController {
    @FXML private StackPane contentArea;
    
    @FXML
    public void initialize() {
        // Show products by default
        showProduits();
    }
    
    @FXML
    private void showProduits() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/produit/ListProduits.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void showCommandes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/commande/ListCommandes.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 