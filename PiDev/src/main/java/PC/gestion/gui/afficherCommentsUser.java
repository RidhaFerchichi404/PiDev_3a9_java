package PC.gestion.gui;

import PC.gestion.entities.Comment;
import PC.gestion.entities.Post;
import PC.gestion.services.ServiceComment;
import PC.gestion.services.ServicePost;
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
import java.util.List;

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
             ServicePost servicePost = new ServicePost();
             ServiceComment serviceComment = new ServiceComment();
             try {
                 List<Comment> comments = servicePost.getCommentsForPost(currentPost.getIdp());
                 FPcomments.getChildren().clear();
                 for (Comment comment : comments) {
                     VBox vBox = new VBox(10);
                     Label labelComment = new Label("Comment : " + comment.getComment());
                     Label commentLikes = new Label("Likes : " + String.valueOf(comment.getLikes()));
                     Label userName = new Label("Author : " + String.valueOf(serviceComment.getUserName(comment.getIdUser())));

                     vBox.getChildren().add(userName);
                     vBox.getChildren().add(labelComment);
                     vBox.getChildren().add(commentLikes);
                     vBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

                     FPcomments.getChildren().add(vBox);
                     vBox.setOnMouseClicked(event -> {
                         if (selectedComment != null) {
                             FPcomments.getChildren().forEach(node -> node.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;"));
                         }
                         selectedComment = comment;
                         vBox.setStyle("-fx-border-color: #e19754; -fx-border-width: 1; -fx-padding: 10;");
                     });
                 }
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }



    @FXML
    void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
            Parent root = loader.load();
            afficherPostsUser controller = loader.getController();
            controller.setPost(currentPost);
            Stage stage = (Stage) FPcomments.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
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
}
