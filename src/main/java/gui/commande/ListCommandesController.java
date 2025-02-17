package gui.commande;

import entities.Commande;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.services.CommandeService;
import java.io.IOException;
import java.util.List;

public class ListCommandesController {
    @FXML private FlowPane cardsContainer;
    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        loadCommandes();
    }

    private void loadCommandes() {
        try {
            List<Commande> commandes = commandeService.readAll();
            updateCardView(commandes);
        } catch (Exception e) {
            showError("Erreur de chargement", e.getMessage());
        }
    }

    private void updateCardView(List<Commande> commandes) {
        cardsContainer.getChildren().clear();
        for (Commande commande : commandes) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/commande/CommandCard.fxml"));
                VBox card = loader.load();
                CommandCardController controller = loader.getController();
                controller.setCommande(commande);
                controller.setParentController(this);
                cardsContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void refreshCommandes() {
        loadCommandes();
    }

    @FXML
    private void openAddCommande() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/commande/AjouterCommande.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Ajouter Commande");
            stage.setScene(scene);
            stage.showAndWait();
            loadCommandes();
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre d'ajout");
        }
    }

    public void openEditCommande(Commande commande) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/commande/ModifierCommande.fxml"));
            Scene scene = new Scene(loader.load());
            ModifierCommandeController controller = loader.getController();
            controller.setCommande(commande);
            Stage stage = new Stage();
            stage.setTitle("Modifier Commande");
            stage.setScene(scene);
            stage.showAndWait();
            loadCommandes();
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de modification");
        }
    }

    public void deleteCommande(Commande commande) {
        try {
            commandeService.delete(commande.getIdCommande());
            loadCommandes();
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