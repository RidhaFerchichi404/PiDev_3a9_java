package PC.gestion.gui;

import PC.gestion.entities.Comment;
import PC.gestion.entities.Post;
import PC.gestion.services.ServiceComment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class afficherCommentsUser {

    @FXML
    private Button BTNback;

    @FXML
    private Button BTNlike;

    @FXML
    private FlowPane FPcomments;

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
            FPcomments.getChildren().clear();

            for (Comment comment : serviceComment.getCommentsByPostId(postId)) {
                VBox vbox = new VBox();
                vbox.setSpacing(10);
                vbox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
                Label commentLabel = new Label(comment.getComment() + " (Likes: " + comment.getLikes() + ")");
                commentLabel.setUserData(comment);
                vbox.getChildren().add(commentLabel);
                vbox.setSpacing(20);
                vbox.setOnMouseClicked(event -> {
                    for (Node child : FPcomments.getChildren()) {
                        if (child instanceof VBox) {
                            ((VBox) child).getChildren().get(0).setStyle("");
                        }
                    }
                    commentLabel.setStyle("-fx-background-color: lightblue;");
                });
                FPcomments.getChildren().add(vbox);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction() {
        if (previousScene != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
                Parent root = loader.load();

                afficherPostsUser controller = loader.getController();
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
        for (Node node : FPcomments.getChildren()) {
            if (node instanceof VBox) {
                Label commentLabel = (Label) ((VBox) node).getChildren().get(0);
                if (commentLabel.getStyle().contains("lightblue")) {
                    commentLabel.setStyle("-fx-background-color: yellow;");
                    Comment selectedComment = (Comment) commentLabel.getUserData();
                    if (selectedComment != null) {
                        selectedComment.setLikes(selectedComment.getLikes() + 1);
                        ServiceComment serviceComment = new ServiceComment();
                        try {
                            serviceComment.update(selectedComment);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
        setPostComments(currentPost.getIdp());
    }

}
