package controller.commande;

import entities.Commande;
import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import services.ProduitService;
import java.time.format.DateTimeFormatter;
import utils.Session;

public class CommandCardController {
    @FXML private Label orderIdLabel;
    @FXML private Label dateLabel;
    @FXML private Label clientLabel;
    @FXML private Label productLabel;
    @FXML private Label quantityLabel;
    @FXML private Label statusLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private Commande commande;
    private ListCommandesController parentController;
    private final ProduitService produitService = new ProduitService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setCommande(Commande commande) {
        this.commande = commande;
        updateCardContent();
        updateButtonVisibility();
    }

    public void setParentController(ListCommandesController controller) {
        this.parentController = controller;
    }

    private void updateCardContent() {
        orderIdLabel.setText("Commande #" + commande.getIdCommande());
        dateLabel.setText(commande.getDateCommande().format(dateFormatter));
        clientLabel.setText(commande.getNomClient());
        
        try {
            Produit produit = produitService.readById(commande.getIdProduit());
            productLabel.setText(produit.getNom());
        } catch (Exception e) {
            productLabel.setText("Produit non trouvé");
        }
        
        quantityLabel.setText("Quantité: " + commande.getQuantite());
        statusLabel.setText(commande.getStatutCommande());
        phoneLabel.setText("📞 " + commande.getTelephone());
        addressLabel.setText("📍 " + commande.getAdresseLivraison());
        
        // Update status style
        updateStatusStyle();
    }

    private void updateStatusStyle() {
        statusLabel.getStyleClass().removeAll("status-pending", "status-processing", "status-delivered", "status-cancelled");
        
        switch (commande.getStatutCommande().toLowerCase()) {
            case "en attente":
                statusLabel.getStyleClass().add("status-pending");
                break;
            case "en cours":
                statusLabel.getStyleClass().add("status-processing");
                break;
            case "livrée":
                statusLabel.getStyleClass().add("status-delivered");
                break;
            case "annulée":
                statusLabel.getStyleClass().add("status-cancelled");
                break;
        }
    }

    private void updateButtonVisibility() {
        String userRole = Session.getRole();
        boolean isAdmin = "ADMIN".equalsIgnoreCase(userRole);
        boolean isOwner = Session.getCurrentUser().getId().equals(commande.getIdUser());

        // Admin can edit and delete all commands
        // Users can only edit their own commands
        editButton.setVisible(isAdmin || isOwner);
        deleteButton.setVisible(isAdmin);
        
        System.out.println("Command card buttons - Edit: " + editButton.isVisible() + 
                         ", Delete: " + deleteButton.isVisible() +
                         ", IsOwner: " + isOwner);
    }

    @FXML
    private void handleEdit() {
        parentController.openEditCommande(commande);
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la commande");
        alert.setContentText("Voulez-vous vraiment supprimer la commande #" + commande.getIdCommande() + " ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            parentController.deleteCommande(commande);
        }
    }
} 