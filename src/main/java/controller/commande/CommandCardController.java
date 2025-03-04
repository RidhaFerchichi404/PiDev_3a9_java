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
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.time.format.FormatStyle;

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
    @FXML private Button exportButton;

    private Commande commande;
    private ListCommandesController parentController;
    private final ProduitService produitService = new ProduitService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

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
        // Everyone can export their commands to PDF
        editButton.setVisible(isAdmin || isOwner);
        deleteButton.setVisible(isAdmin);
        exportButton.setVisible(true);
        
        System.out.println("Command card buttons - Edit: " + editButton.isVisible() + 
                         ", Delete: " + deleteButton.isVisible() +
                         ", Export: " + exportButton.isVisible() +
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

    @FXML
    private void handleExport() {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choisir le dossier de sauvegarde");
            File selectedDirectory = directoryChooser.showDialog(exportButton.getScene().getWindow());
            
            if (selectedDirectory != null) {
                String fileName = String.format("commande_%d_%s.pdf", 
                    commande.getIdCommande(),
                    commande.getDateCommande().format(fileFormatter));
                String filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName;
                
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Add title
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph title = new Paragraph("Bon de Commande", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" ")); // Spacing

                // Add order details
                Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

                document.add(new Paragraph("Détails de la Commande:", boldFont));
                document.add(new Paragraph("Numéro de commande: " + commande.getIdCommande(), normalFont));
                document.add(new Paragraph("Date: " + commande.getDateCommande().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), normalFont));
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Informations Client:", boldFont));
                document.add(new Paragraph("Nom: " + commande.getNomClient(), normalFont));
                document.add(new Paragraph("Téléphone: " + commande.getTelephone(), normalFont));
                document.add(new Paragraph("Adresse de livraison: " + commande.getAdresseLivraison(), normalFont));
                document.add(new Paragraph(" "));

                // Add product details
                document.add(new Paragraph("Produit Commandé:", boldFont));
                try {
                    Produit produit = produitService.readById(commande.getIdProduit());
                    document.add(new Paragraph("Nom du produit: " + produit.getNom(), normalFont));
                    document.add(new Paragraph("Catégorie: " + produit.getCategorie(), normalFont));
                    document.add(new Paragraph("Prix unitaire: " + produit.getPrix() + " TND", normalFont));
                } catch (Exception e) {
                    document.add(new Paragraph("Produit non trouvé", normalFont));
                }
                document.add(new Paragraph("Quantité: " + commande.getQuantite(), normalFont));
                document.add(new Paragraph(" "));

                // Add status
                document.add(new Paragraph("Statut de la commande: " + commande.getStatutCommande(), boldFont));

                document.close();

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export PDF");
                alert.setHeaderText("Export réussi");
                alert.setContentText("Le bon de commande a été exporté avec succès vers:\n" + filePath);
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur d'export", "Une erreur est survenue lors de l'export du PDF:\n" + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 