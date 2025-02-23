package controller.commande;

import entities.Commande;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.CommandeService;
import utils.Session;
import java.io.IOException;
import java.util.List;

public class ListCommandesController {
    @FXML private FlowPane cardsContainer;
    @FXML private Button addCommandeButton;
    @FXML private Button refreshButton;
    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        updateButtonVisibility();
        loadCommandes();
    }

    private void updateButtonVisibility() {
        String userRole = Session.getRole();
        System.out.println("ListCommandesController - Current user role: " + userRole);
        
        // Add Commande button is visible for clients and coaches
        addCommandeButton.setVisible(!userRole.equalsIgnoreCase("ADMIN"));
        // Refresh button is visible for all roles
        refreshButton.setVisible(true);
        
        System.out.println("Button visibility - Add: " + addCommandeButton.isVisible() + 
                         ", Refresh: " + refreshButton.isVisible());
    }

    private void loadCommandes() {
        try {
            List<Commande> commandes;
            String userRole = Session.getRole();
            Long currentUserId = Session.getCurrentUser().getId();
            
            if ("ADMIN".equalsIgnoreCase(userRole)) {
                // Admin sees all commands
                commandes = commandeService.readAll();
            } else {
                // Clients and coaches see only their commands
                commandes = commandeService.readAllByUser(currentUserId);
            }
            
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
        // Only allow editing if user is admin or if it's their own command
        if ("ADMIN".equalsIgnoreCase(Session.getRole()) || 
            commande.getIdUser() == Session.getCurrentUser().getId()) {
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
        } else {
            showError("Accès refusé", "Vous n'avez pas la permission de modifier cette commande");
        }
    }

    public void deleteCommande(Commande commande) {
        // Only allow deletion if user is admin
        if ("ADMIN".equalsIgnoreCase(Session.getRole())) {
            try {
                commandeService.delete(commande);
                loadCommandes();
            } catch (Exception e) {
                showError("Erreur de suppression", e.getMessage());
            }
        } else {
            showError("Accès refusé", "Vous n'avez pas la permission de supprimer des commandes");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 