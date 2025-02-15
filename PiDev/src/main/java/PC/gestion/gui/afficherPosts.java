package PC.gestion.gui;

import PC.gestion.entities.Comment;
import PC.gestion.services.ServiceComment;
import PC.gestion.services.ServicePost;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import PC.gestion.entities.Post;
import javafx.stage.Stage;

public class afficherPosts {

    @FXML
    private Button BTNajouterComment;

    @FXML
    private Button BTNback;

    @FXML
    private ImageView IVimage;

    @FXML
    private TextArea TAdescription;

    @FXML
    private Label LBdate;

    @FXML
    private Label LBnom;

    @FXML
    private TextField TFcomment;

    @FXML
    private Button BTNviewComment;

    private Scene previousScene;
    private Post selectedPost;

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }


    public void addComment() {
        ServicePost servicePost = new ServicePost();
        String commentText = TFcomment.getText();

        int postId = selectedPost.getIdp();
        Date commentDate = new Date(System.currentTimeMillis());
        int likes = 0;
        int userId = selectedPost.getIdUser();

        Comment newComment = new Comment(commentText, new java.sql.Date(commentDate.getTime()), likes, postId, userId);
        ServiceComment serviceComment = new ServiceComment();
        try {
            serviceComment.ajouter(newComment);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("INFORMATION");
            alert.setContentText("Comment added successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR");
            alert.setContentText("Failed to add comment: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void setPost(Post selectedPost) {
        if (selectedPost != null) {
            this.selectedPost = selectedPost;
            LBnom.setText("test1");
            LBdate.setText("test2");
            IVimage.setImage(new Image("file:" + selectedPost.getImage()));
            TAdescription.setText(selectedPost.getDescription());
        }
    }

    @FXML
    private void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listerPosts.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) BTNback.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewCommentsButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherComments.fxml"));
            Parent root = loader.load();

            afficherComments controller = loader.getController();
            controller.setPostComments(this.selectedPost.getIdp());
            controller.setCurrentPost(this.selectedPost);
            controller.setPreviousSceneC(BTNviewComment.getScene());
            Stage stage = (Stage) BTNviewComment.getScene().getWindow();
            stage.setTitle("Comments");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

