package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import entities.Post;
import entities.Comment;
import services.ServicePost;
import services.ServiceComment;

import java.sql.SQLException;
import java.util.List;

public class modifierCommentsAdmin {


    @javafx.fxml.FXML
    private Button BTNmodifier;


    @FXML
    private Button BTNsupprimer;


    @FXML
    private FlowPane FPcomments;


    @FXML
    private FlowPane FPposts;


    @FXML
    private TextField TFcomment;


    @FXML
    private TextField TFnbLikes;

    private Comment selectedComment;
    private int postId;

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @FXML
    public void initialize() {
        loadPosts();
    }

    private void loadPosts() {
        ServicePost servicePost = new ServicePost();
        try {
            List<Post> posts = servicePost.afficherAllPosts();
            for (Post post : posts) {
                VBox card = createPostCard(post);
                FPposts.getChildren().add(card);
                card.setOnMouseClicked(event -> {
                    loadComments(post);
                    //highlightSelectedPost(card);
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

        card.getChildren().addAll(postImage, typeLabel);
        return card;
    }

    private void loadComments(Post post) {
        try {
            System.out.println("📌 Chargement des commentaires pour le post ID : " + post.getIdp());
            ServiceComment serviceComment = new ServiceComment();
            List<Comment> comments = serviceComment.getCommentsByPostId(post.getIdp());
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
            TFcomment.setText(comment.getComment());
            TFnbLikes.setText(String.valueOf(comment.getLikes()));
        });

        return card;
    }



    @javafx.fxml.FXML
    void handleDeleteComment(ActionEvent event) {
        if (selectedComment != null) {
            ServiceComment serviceComment = new ServiceComment();
            ServicePost servicePost = new ServicePost();
            try {
                serviceComment.delete(selectedComment);
                loadComments(servicePost.getPostById(selectedComment.getIdPost()));
                selectedComment = null;
                TFcomment.clear();
                TFnbLikes.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @javafx.fxml.FXML
    void handleModifyComment(ActionEvent event) {
        if (selectedComment != null) {
            selectedComment.setComment(TFcomment.getText());
            selectedComment.setLikes(Integer.parseInt(TFnbLikes.getText()));
            ServiceComment serviceComment = new ServiceComment();
            try {
                serviceComment.update(selectedComment);
                ServicePost servicePost = new ServicePost();
                loadComments(servicePost.getPostById(selectedComment.getIdPost()));
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



