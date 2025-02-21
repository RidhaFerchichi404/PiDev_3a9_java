package PC.gestion.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import PC.gestion.entities.Post;
import PC.gestion.entities.Comment;
import PC.gestion.services.ServicePost;
import PC.gestion.services.ServiceComment;

import java.sql.SQLException;
import java.util.List;

public class ajouterCommentsAdmin {

    @FXML
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
                VBox vBox = new VBox(10);
                vBox.setAlignment(Pos.CENTER);
                vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                ImageView imageView = new ImageView(new Image("file:" + post.getImage()));
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                Label labelType = new Label(post.getType());

                vBox.getChildren().addAll(imageView, labelType);
                FPposts.getChildren().add(vBox);
                vBox.setOnMouseClicked(event -> {
                    loadComments(post);
                    highlightSelectedPost(vBox);
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void highlightSelectedPost(VBox selectedVBox) {
        for (Node node : FPposts.getChildren()) {
            if (node instanceof VBox) {
                VBox vBox = (VBox) node;
                vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");
            }
        }
        selectedVBox.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-padding: 10;");
    }

    private void loadComments(Post post) {
        FPcomments.getChildren().clear();
        ServiceComment serviceComment = new ServiceComment();
        try {
            List<Comment> comments = serviceComment.getCommentsByPostId(post.getIdp());
            for (Comment comment : comments) {
                VBox vBox = new VBox(10);
                vBox.setAlignment(Pos.CENTER);
                vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                Label labelComment = new Label(comment.getComment());
                Label labelLikes = new Label("Likes: " + comment.getLikes());

                vBox.getChildren().addAll(labelComment, labelLikes);
                FPcomments.getChildren().add(vBox);
                vBox.setOnMouseClicked(event -> {
                    selectComment(comment);
                    highlightSelectedComment(vBox);
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void highlightSelectedComment(VBox selectedVBox) {
        for (Node node : FPcomments.getChildren()) {
            if (node instanceof VBox) {
                VBox vBox = (VBox) node;
                vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");
            }
        }
        selectedVBox.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-padding: 10;");
    }

    private void selectComment(Comment comment) {
        selectedComment = comment;
        TFcomment.setText(comment.getComment());
        TFnbLikes.setText(String.valueOf(comment.getLikes()));
    }

    @FXML
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

    @FXML
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

}



