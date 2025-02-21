package PC.gestion.gui;

import PC.gestion.entities.Post;
import PC.gestion.services.ServicePost;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GestionPCUser {

    @FXML
    private FlowPane FPposts;

    @FXML
    public void initialize() {
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
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostsUser.fxml"));
                        Parent root = loader.load();
                        afficherPostsUser controller = loader.getController();
                        controller.setPost(post);
                        Stage stage = (Stage) FPposts.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


