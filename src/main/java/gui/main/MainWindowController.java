package gui.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MainWindowController {
    @FXML
    private BorderPane mainPane;

    @FXML
    public void showProduitsList() {
        loadView("/gui/produit/ListProduits.fxml");
    }

    @FXML
    public void showCommandesList() {
        loadView("/gui/commande/ListCommandes.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane view = loader.load();
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 