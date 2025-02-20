package gui;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private StackPane mainContent;
    @FXML
    private Button logoutButton;

    @FXML
    public void handleProduits() {
        displayContent("Produits Section");
    }

    @FXML
    public void handlePosts() {
        displayContent("Posts Section");
    }

    @FXML
    public void handleSalles() {
        displayContent("Salles Section");
    }


    @FXML
    private void handleProfile() {
        try {
            // Load the User List view from FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserList.fxml"));
            Parent userListView = loader.load();

            // Replace the content of the mainContent StackPane with the new view
            mainContent.getChildren().clear();
            mainContent.getChildren().add(userListView);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load UserList.fxml");
        }
    }
    @FXML
    private void logoutUser() {
        System.out.println("Logging out...");

        // Close the entire application
        javafx.application.Platform.exit();
    }







    private void displayContent(String contentText) {
        // Clear previous content
        mainContent.getChildren().clear();

        // Create a new label with the content text
        Label contentLabel = new Label(contentText);
        contentLabel.getStyleClass().add("main-title");

        // Add the label to the main content area
        mainContent.getChildren().add(contentLabel);

        // Apply a slide-in animation
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), contentLabel);
        slideIn.setFromX(-mainContent.getWidth());
        slideIn.setToX(0);
        slideIn.play();
    }
}
