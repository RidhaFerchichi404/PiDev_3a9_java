package PC.gestion.gui;

import PC.gestion.entities.Comment;
import PC.gestion.entities.Post;
import PC.gestion.services.ServiceComment;
import PC.gestion.services.ServicePost;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class afficherComments {

    @FXML
    private Button BTNback;

    @FXML
    private Button BTNlike;

    @FXML
    private ListView<Comment> LVcomments;

    private Scene previousScene;
    public void setPreviousSceneC(Scene scene) {
        this.previousScene = scene;
    }
    private Post currentPost;
    public void setCurrentPost(Post post) {
        this.currentPost = post;
    }
    @FXML
    public void setPostComments(int postId) {
        ServiceComment serviceComment = new ServiceComment();
        try {
            LVcomments.getItems().setAll(serviceComment.getCommentsByPostId(postId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction() {
        if (previousScene != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPosts.fxml"));
                Parent root = loader.load();

                afficherPosts controller = loader.getController();
                Post selectedPost = currentPost;
                controller.setPost(selectedPost);

                Stage stage = (Stage) BTNback.getScene().getWindow();

                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLikeButtonAction() {
        Comment selectedComment = LVcomments.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            selectedComment.setLikes(selectedComment.getLikes() + 1);
            ServiceComment serviceComment = new ServiceComment();
            try {
                serviceComment.update(selectedComment);
                LVcomments.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
