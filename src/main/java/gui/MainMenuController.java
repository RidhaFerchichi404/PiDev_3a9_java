package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button btnProduits;

    @FXML
    private Button btnPosts;

    @FXML
    private Button btnSalles;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogout;

    @FXML
    private StackPane mainContent;

    @FXML
    public void handleProduits() {
        displayContent("Produits Section");
    }

    @FXML
    public void handlePosts() {
        displayContent("Posts Section");
    }

    @FXML
    private void handleSalles() {
        try {
            // Charger la nouvelle vue (FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherfrontsalle.fxml"));
            Parent sallesView = loader.load();

            // Remplacer le contenu actuel avec la nouvelle vue
            mainContent.getChildren().setAll(sallesView);
        } catch (IOException e) {
            e.printStackTrace();
        }}
    @FXML
    private void handleProfile() {
        try {
            // Load the FXML for modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientModify.fxml"));
            Parent modifyView = loader.load();

            // Create a new stage for the modification form
            Stage stage = new Stage();
            stage.setTitle("Modify Profile");
            stage.setScene(new Scene(modifyView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load ClientModify.fxml");
        }
    }



    private void displayContent(String contentText) {
        // Clear previous content
        mainContent.getChildren().clear();

        // Create a new label with the content text
        Label contentLabel = new Label(contentText);
        contentLabel.getStyleClass().add("main-title");

        // Add the label to the main content area
        mainContent.getChildren().add(contentLabel);
    }
    @FXML
    private void handleLogout() {
        System.out.println("Logging out...");

        // Close the entire application
        javafx.application.Platform.exit();
    }

}
