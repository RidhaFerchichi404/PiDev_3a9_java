package controller.produit;

import entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import java.util.concurrent.CompletableFuture;
import services.DeepSeekService;

public class ProductAnalysisController {
    @FXML private TextField ageField;
    @FXML private TextField weightField;
    @FXML private TextField heightField;
    @FXML private Label productNameLabel;
    @FXML private Label productCategoryLabel;
    @FXML private TextFlow analysisResult;
    @FXML private ProgressIndicator loadingIndicator;
    @FXML private Button analyzeButton;
    
    private Produit produit;
    private final DeepSeekService deepSeekService = new DeepSeekService();

    public void setProduit(Produit produit) {
        this.produit = produit;
        updateProductInfo();
    }

    private void updateProductInfo() {
        productNameLabel.setText(produit.getNom());
        productCategoryLabel.setText(produit.getCategorie());
    }

    @FXML
    private void handleAnalyze() {
        if (!validateInput()) {
            showError("Veuillez remplir tous les champs correctement.");
            return;
        }

        // Show loading state
        loadingIndicator.setVisible(true);
        analyzeButton.setDisable(true);
        analysisResult.getChildren().clear();

        // Prepare analysis data
        int age = Integer.parseInt(ageField.getText());
        double weight = Double.parseDouble(weightField.getText());
        double height = Double.parseDouble(heightField.getText());

        CompletableFuture.supplyAsync(() -> {
            try {
                return deepSeekService.analyzeProduct(produit, age, weight, height);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenAccept(result -> {
            Platform.runLater(() -> {
                displayAnalysisResult(result);
                loadingIndicator.setVisible(false);
                analyzeButton.setDisable(false);
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> {
                showError("Une erreur est survenue lors de l'analyse.");
                loadingIndicator.setVisible(false);
                analyzeButton.setDisable(false);
            });
            return null;
        });
    }

    private boolean validateInput() {
        try {
            if (ageField.getText().isEmpty() || weightField.getText().isEmpty() || heightField.getText().isEmpty()) {
                return false;
            }
            int age = Integer.parseInt(ageField.getText());
            double weight = Double.parseDouble(weightField.getText());
            double height = Double.parseDouble(heightField.getText());

            return age > 0 && age < 120 && weight > 0 && weight < 300 && height > 0 && height < 300;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void displayAnalysisResult(String analysis) {
        analysisResult.getChildren().clear();
        
        // Split the analysis into sections and format them
        String[] sections = analysis.split("\n\n");
        for (String section : sections) {
            Text text = new Text(section + "\n\n");
            text.setFill(Color.WHITE);
            analysisResult.getChildren().add(text);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 