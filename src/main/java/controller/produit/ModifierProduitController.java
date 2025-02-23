package controller.produit;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import services.ProduitService;
import java.math.BigDecimal;
import java.io.File;

public class ModifierProduitController {
    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private TextField categorieField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private CheckBox disponibleCheck;
    @FXML private ImageView imagePreview;
    @FXML private Button uploadButton;

    private final ProduitService produitService = new ProduitService();
    private Produit produit;
    private String selectedImagePath = null;

    @FXML
    public void initialize() {
        uploadButton.setOnAction(e -> handleImageUpload());
    }

    @FXML
    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            // Show preview
            Image image = new Image(selectedFile.toURI().toString());
            imagePreview.setImage(image);
        }
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        populateFields();
    }

    private void populateFields() {
        nomField.setText(produit.getNom());
        descriptionField.setText(produit.getDescription());
        categorieField.setText(produit.getCategorie());
        prixField.setText(produit.getPrix().toString());
        stockField.setText(String.valueOf(produit.getQuantiteStock()));
        disponibleCheck.setSelected(produit.isDisponible());
        
        // Load existing image if available
        if (produit.getImagePath() != null && !produit.getImagePath().isEmpty()) {
            selectedImagePath = produit.getImagePath();
            try {
                File imageFile = new File(produit.getImagePath());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    imagePreview.setImage(image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Validation
            if (nomField.getText().isEmpty() || categorieField.getText().isEmpty() || 
                prixField.getText().isEmpty() || stockField.getText().isEmpty()) {
                showError("Erreur de validation", "Tous les champs sont obligatoires");
                return;
            }

            produit.setNom(nomField.getText());
            produit.setDescription(descriptionField.getText());
            produit.setCategorie(categorieField.getText());
            produit.setPrix(new BigDecimal(prixField.getText()));
            produit.setQuantiteStock(Integer.parseInt(stockField.getText()));
            produit.setDisponible(disponibleCheck.isSelected());
            produit.setImagePath(selectedImagePath); // Update image path

            produitService.update(produit);
            closeWindow();
        } catch (NumberFormatException e) {
            showError("Erreur de format", "Prix et stock doivent être des nombres valides");
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de la modification du produit: " + e.getMessage());
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
        ((Stage) nomField.getScene().getWindow()).close();
    }
} 