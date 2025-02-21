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

public class afficherPostsUser {

    @FXML
    private Button BTNajouterComment;

    @FXML
    private Button BTNback;

    @FXML
    private ImageView IVimage;

    @FXML
    private Label LBdescription;

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
        String commentText = TFcomment.getText().trim();
        if (commentText.isEmpty() || !commentText.matches("[a-zA-Z0-9\\p{Punct}\\s]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Comment");
            alert.setContentText("Comment must only contain letters, punctuation, and numbers, and cannot be empty.");
            alert.showAndWait();
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
            stage.setScene(new Scene(root));
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
            stage.setTitle("Comments");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

