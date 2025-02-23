package gui;

import entities.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServicePost;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GestionPCUser {

    @javafx.fxml.FXML
    private FlowPane FPposts;

    @javafx.fxml.FXML
    private ComboBox<String> ComboBox;

    @javafx.fxml.FXML
    public void initialize() {
         ServicePost servicePost = new ServicePost();
         try {
             List<Post> posts = servicePost.afficherAllPosts();
             displayPosts(posts);
         } catch (SQLException e) {
             e.printStackTrace();
         }

         ComboBox.setOnAction(event -> {
             String selectedType = ComboBox.getValue();
             if (selectedType != null && !selectedType.isEmpty()) {
                 List<Post> filteredPosts = servicePost.afficherPostsByType(selectedType);
                 displayPosts(filteredPosts);
             }else if (selectedType.isEmpty()) {
            try {
                List<Post> allPosts = servicePost.afficherAllPosts();
                displayPosts(allPosts);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
         });
    }

    public void handleAscendentSort(ActionEvent actionEvent) {
        ServicePost servicePost = new ServicePost();
        try {
            List<Post> posts = servicePost.afficherAllPosts();
            posts.sort((p1, p2) -> p1.getType().compareToIgnoreCase(p2.getType()));
            displayPosts(posts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleDescendentSort(ActionEvent actionEvent) {
        ServicePost servicePost = new ServicePost();
        try {
            List<Post> posts = servicePost.afficherAllPosts();
            posts.sort((p1, p2) -> p2.getType().compareToIgnoreCase(p1.getType()));
            displayPosts(posts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleMostCommented(ActionEvent actionEvent) {
        ServicePost servicePost = new ServicePost();
        try {
            List<Post> posts = servicePost.sortPostsByMostComments();
            displayPosts(posts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private void displayPosts(List<Post> posts) {
            FPposts.getChildren().clear();
            if (posts.isEmpty()) {
                System.out.println("⚠ No posts found!");
                Label noPostLabel = new Label("No posts available.");
                noPostLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff4444;");
                FPposts.getChildren().add(noPostLabel);
            } else {
                for (Post post : posts) {
                    System.out.println("✅ Post found: " + post.getType());
                    FPposts.getChildren().add(createPostCard(post));
                }
            }
        }

        private VBox createPostCard(Post post) {
            VBox card = new VBox(15);
            card.setStyle(
                    "-fx-background-color: #1A1A1A; " +
                            "-fx-border-color: #FF6600; " +
                            "-fx-border-radius: 20; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 20; " +
                            "-fx-spacing: 15; " +
                            "-fx-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.5), 12, 0, 4, 4); " +
                            "-fx-cursor: hand;");

            card.setPrefWidth(300);
            card.setPrefHeight(300);

            card.setOnMouseEntered(e -> card.setStyle(
                    "-fx-background-color: #262626; " +
                            "-fx-border-color: #FF6600; " +
                            "-fx-border-radius: 20; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 20; " +
                            "-fx-spacing: 15; " +
                            "-fx-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.8), 16, 0, 4, 4); " +
                            "-fx-cursor: hand;"));

            card.setOnMouseExited(e -> card.setStyle(
                    "-fx-background-color: #1A1A1A; " +
                            "-fx-border-color: #FF6600; " +
                            "-fx-border-radius: 20; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 20; " +
                            "-fx-spacing: 15; " +
                            "-fx-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.5), 12, 0, 4, 4); " +
                            "-fx-cursor: hand;"));

            ImageView postImage = new ImageView();
            try {
                Image image = new Image("file:" + post.getImage());
                postImage.setImage(image);
                postImage.setFitWidth(200);
                postImage.setFitHeight(150);
                postImage.setPreserveRatio(true);
                postImage.setStyle("-fx-border-radius: 10; -fx-background-radius: 10;");
            } catch (Exception e) {
                System.out.println("❌ Error loading post image: " + e.getMessage());
            }

            Label typeLabel = new Label(" Post Type: " + post.getType());
            typeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

            Button viewPostButton = new Button(" View Post");
            viewPostButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #FF6600, #CC5200); " +
                            "-fx-text-fill: #000000; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 10 20; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.4), 8, 0, 2, 2);");

            viewPostButton.setOnMouseEntered(e -> viewPostButton.setStyle(
                    "-fx-background-color: #FF3300; " +
                            "-fx-text-fill: #000000; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 10 20; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.6), 10, 0, 2, 2);"));

            viewPostButton.setOnMouseExited(e -> viewPostButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #FF6600, #CC5200); " +
                            "-fx-text-fill: #000000; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 10 20; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.4), 8, 0, 2, 2);"));

            viewPostButton.setOnAction(event -> viewPost(post));

            card.getChildren().addAll(postImage, typeLabel, viewPostButton);
            return card;
        }

    private void viewPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
            Parent root = loader.load();
            afficherPostsUser controller = loader.getController();
            controller.setPost(post);
            StackPane mainMenu = (StackPane) FPposts.getScene().lookup("#mainContent");
            mainMenu.getChildren().clear();
            mainMenu.getChildren().add(root);
        } catch (IOException e) {
            System.err.println("❌ Error loading post view: " + e.getMessage());
            e.printStackTrace();


        }
    }
}
