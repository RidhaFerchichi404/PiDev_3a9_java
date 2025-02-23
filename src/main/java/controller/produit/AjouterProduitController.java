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

public class AjouterProduitController {
    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private TextField categorieField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private CheckBox disponibleCheck;
    @FXML private ImageView imagePreview;
    @FXML private Button uploadButton;

    private final ProduitService produitService = new ProduitService();
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

    @FXML
    private void handleSave() {
        try {
            String validationErrors = validateInputs();
            if (!validationErrors.isEmpty()) {
                showError("Erreur de validation", validationErrors);
                return;
            }

            Produit produit = new Produit(
                nomField.getText().trim(),
                descriptionField.getText().trim(),
                categorieField.getText().trim(),
                new BigDecimal(prixField.getText().trim()),
                Integer.parseInt(stockField.getText().trim()),
                disponibleCheck.isSelected(),
                selectedImagePath
            );

            produitService.create(produit);
            closeWindow();
        } catch (Exception e) {
            showError("Erreur", "Erreur lors de la création du produit: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private String validateInputs() {
        StringBuilder errors = new StringBuilder();

        // Validation du nom
        if (nomField.getText().trim().isEmpty()) {
            errors.append("- Le nom du produit est obligatoire\n");
        } else if (nomField.getText().length() < 3) {
            errors.append("- Le nom doit contenir au moins 3 caractères\n");
        }

        // Validation de la description
        if (descriptionField.getText().trim().isEmpty()) {
            errors.append("- La description est obligatoire\n");
        } else if (descriptionField.getText().length() < 10) {
            errors.append("- La description doit contenir au moins 10 caractères\n");
        }

        // Validation de la catégorie
        if (categorieField.getText().trim().isEmpty()) {
            errors.append("- La catégorie est obligatoire\n");
        }

        // Validation du prix
        try {
            BigDecimal prix = new BigDecimal(prixField.getText().trim());
            if (prix.compareTo(BigDecimal.ZERO) <= 0) {
                errors.append("- Le prix doit être supérieur à 0\n");
            }
        } catch (NumberFormatException e) {
            errors.append("- Le prix doit être un nombre valide\n");
        }

        // Validation du stock
        try {
            int stock = Integer.parseInt(stockField.getText().trim());
            if (stock < 0) {
                errors.append("- Le stock ne peut pas être négatif\n");
            }
        } catch (NumberFormatException e) {
            errors.append("- La quantité en stock doit être un nombre entier\n");
        }

        return errors.toString();
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