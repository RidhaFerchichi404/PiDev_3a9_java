package gui;

import entities.Comment;
import entities.Post;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import services.ServiceComment;
import services.ServicePost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
public class afficherCommentsUser {

    @FXML
    private Button BTNback;

    @FXML
    private Button BTNlike;

    @FXML
    private FlowPane FPcomments;

    private Post currentPost;
    private Comment selectedComment;

    public void setPost(Post post) {
        this.currentPost = post;
        loadComments();
    }

    private void loadComments() {
        try {
            System.out.println("📌 Chargement des commentaires pour le post ID : " + currentPost.getIdp());
            ServicePost servicePost = new ServicePost();
            ArrayList<Comment> comments = servicePost.getCommentsForPost(currentPost.getIdp());
            FPcomments.getChildren().clear();

            if (comments.isEmpty()) {
                System.out.println("⚠ Aucun commentaire trouvé !");
                Label noCommentLabel = new Label("Aucun commentaire disponible.");
                noCommentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff4444;");
                FPcomments.getChildren().add(noCommentLabel);
            } else {
                for (Comment comment : comments) {
                    System.out.println("✅ Commentaire trouvé : " + comment.getComment());
                    FPcomments.getChildren().add(createCommentCard(comment));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors du chargement des commentaires : " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des commentaires", Alert.AlertType.ERROR);
        }
    }

    private VBox createCommentCard(Comment comment) throws SQLException {
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

        Label labelComment = new Label("Comment : " + comment.getComment());
        labelComment.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        Label commentLikes = new Label("Likes : " + comment.getLikes());
        commentLikes.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        Label userName = new Label("Author : " + new ServiceComment().getUserName(comment.getIdUser()));
        userName.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        card.getChildren().addAll(userName, labelComment, commentLikes);

        card.setOnMouseClicked(event -> {
            if (selectedComment != null) {
                FPcomments.getChildren().forEach(node -> node.setStyle(
                        "-fx-background-color: #1A1A1A; " +
                                "-fx-border-color: #FF6600; " +
                                "-fx-border-radius: 20; " +
                                "-fx-background-radius: 20; " +
                                "-fx-padding: 20; " +
                                "-fx-spacing: 15; " +
                                "-fx-alignment: center; " +
                                "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.5), 12, 0, 4, 4); " +
                                "-fx-cursor: hand;"));
            }
            selectedComment = comment;
            card.setStyle(
                    "-fx-background-color: #262626; " +
                            "-fx-border-color: #FF6600; " +
                            "-fx-border-radius: 20; " +
                            "-fx-background-radius: 20; " +
                            "-fx-padding: 20; " +
                            "-fx-spacing: 15; " +
                            "-fx-alignment: center; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.8), 16, 0, 4, 4); " +
                            "-fx-cursor: hand;");
        });

        return card;
    }



    @FXML
    void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
            Parent root = loader.load();

            afficherPostsUser controller = loader.getController();
            controller.setPost(currentPost);

            Stage stage = (Stage) BTNback.getScene().getWindow();
            StackPane mainMenu = (StackPane) stage.getScene().lookup("#mainContent");
            mainMenu.getChildren().clear();
            mainMenu.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLikeButtonAction(ActionEvent event) {
        if (selectedComment != null) {
            selectedComment.setLikes(selectedComment.getLikes() + 1);
            ServiceComment serviceComment = new ServiceComment();
            try {
                serviceComment.update(selectedComment);
                loadComments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
