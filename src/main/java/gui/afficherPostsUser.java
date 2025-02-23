package gui;

import entities.Comment;
import javafx.scene.layout.StackPane;
import services.ServiceComment;
import services.ServicePost;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import entities.Post;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class afficherPostsUser {

    @FXML private Button BTNajouterComment;
    @FXML private Button BTNback;
    @FXML private ImageView IVimage;
    @FXML private Label LBdescription;
    @FXML private Label LBdate;
    @FXML private Label LBnom;
    @FXML private TextField TFcomment;
    @FXML private Button BTNviewComment;

    private Scene previousScene;
    private Post selectedPost;

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    public void addComment() {
        String commentText = TFcomment.getText().trim();
        if (commentText.isEmpty() || !commentText.matches("[a-zA-Z0-9\\p{Punct}\\s]+")) {
            showAlert("Error", "Invalid Comment", "Comment must only contain letters, punctuation, and numbers, and cannot be empty.", Alert.AlertType.ERROR);
            return;
        }

        int postId = selectedPost.getIdp();
        Date commentDate = new Date(System.currentTimeMillis());
        int likes = 0;
        int userId = selectedPost.getIdUser();

        Comment newComment = new Comment(commentText, new java.sql.Date(commentDate.getTime()), likes, postId, userId);
        ServiceComment serviceComment = new ServiceComment();
        try {
            serviceComment.ajouter(newComment);
            showAlert("Success", "INFORMATION", "Comment added successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Error", "ERROR", "Failed to add comment: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void setPost(Post selectedPost) {
        if (selectedPost != null) {
            this.selectedPost = selectedPost;
            try {
                ServicePost servicePost = new ServicePost();
                System.out.println("User ID: " + selectedPost.getIdUser());
                System.out.println("Post ID: " + selectedPost.getIdp());
                System.out.println("Post Date: " + selectedPost.getDate());
                Date postDate = selectedPost.getDate();
                if (postDate != null) {
                    LBdate.setText(postDate.toString());
                } else {
                    LBdate.setText("No date available");
                }
                LBnom.setText(servicePost.getUserNamePost(selectedPost.getIdUser()));
                IVimage.setImage(new Image("file:" + selectedPost.getImage()));
                LBdescription.setText(selectedPost.getDescription());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionPCUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) BTNback.getScene().getWindow();
            StackPane mainMenu = (StackPane) stage.getScene().lookup("#mainContent");
            mainMenu.getChildren().clear();
            mainMenu.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewCommentsButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherCommentsUser.fxml"));
            Parent root = loader.load();

            afficherCommentsUser controller = loader.getController();
            controller.setPost(this.selectedPost);

            Stage stage = (Stage) BTNviewComment.getScene().getWindow();
            StackPane mainMenu = (StackPane) stage.getScene().lookup("#mainContent");
            mainMenu.getChildren().clear();
            mainMenu.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}