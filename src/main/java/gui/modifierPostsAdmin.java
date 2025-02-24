package gui;

import entities.Post;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import services.ServicePost;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class modifierPostsAdmin implements Initializable {

    @javafx.fxml.FXML
    private Button BTNmodifier;

    @javafx.fxml.FXML
    private Button BTNsupprimer;

    @javafx.fxml.FXML
    private FlowPane FPposts;

    @javafx.fxml.FXML
    private RadioButton RBmarathonModifier;

    @javafx.fxml.FXML
    private RadioButton RBpromotionModifier;

    @javafx.fxml.FXML
    private RadioButton RBregimeModifier;

    @javafx.fxml.FXML
    private TextField TFdescriptionModifier;

    @javafx.fxml.FXML
    private TextField TFpathModifier;

    @javafx.fxml.FXML
    private ToggleGroup type;

    private Post selectedPost;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServicePost servicePost = new ServicePost();
        try {
            List<Post> posts = servicePost.afficherAllPosts();
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
        card.setOnMouseClicked(event -> {
            selectedPost = post;
            TFdescriptionModifier.setText(post.getDescription());
            TFpathModifier.setText(post.getImage());
            switch (post.getType()) {
                case "Marathon":
                    RBmarathonModifier.setSelected(true);
                    break;
                case "Promotion":
                    RBpromotionModifier.setSelected(true);
                    break;
                case "Regime":
                    RBregimeModifier.setSelected(true);
                    break;
            }
        });

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

        card.getChildren().addAll(postImage, typeLabel);
        return card;
    }

    @javafx.fxml.FXML
    private void ModifierPoste() {
        if (selectedPost != null) {
            if (TFdescriptionModifier.getText().isEmpty() || TFpathModifier.getText().isEmpty() || type.getSelectedToggle() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Validation Error");
                alert.setContentText("Description, image path, and type cannot be empty.");
                alert.showAndWait();
                return;
            }
            selectedPost.setDescription(TFdescriptionModifier.getText());
            selectedPost.setImage(TFpathModifier.getText());
            selectedPost.setType(((RadioButton) type.getSelectedToggle()).getText());

            ServicePost servicePost = new ServicePost();
            try {
                servicePost.update(selectedPost);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("INFORMATION");
                alert.setContentText("Post modified successfully!");
                alert.showAndWait();
                selectedPost = null;
                refreshPosts();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Failed to modify post: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @javafx.fxml.FXML
    private void SupprimerPoste() {
        if (selectedPost != null) {
            ServicePost servicePost = new ServicePost();
            try {
                servicePost.delete(selectedPost);
                FPposts.getChildren().removeIf(node -> {
                    if (node instanceof VBox) {
                        VBox vBox = (VBox) node;
                        for (Node child : vBox.getChildren()) {
                            if (child instanceof Label) {
                                Label label = (Label) child;
                                return label.getText().equals(" Post Type: " + selectedPost.getType());
                            }
                        }
                    }
                    return false;
                });
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("INFORMATION");
                alert.setContentText("Post deleted successfully!");
                alert.showAndWait();
                selectedPost = null;
                refreshPosts();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("ERROR");
                alert.setContentText("Failed to delete post: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void refreshPosts() {
        FPposts.getChildren().clear();
        ServicePost servicePost = new ServicePost();
        try {
            List<Post> posts = servicePost.afficherAllPosts();
            displayPosts(posts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
