package PC.gestion.gui;

import PC.gestion.entities.Post;
import PC.gestion.services.ServicePost;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ajouterCommentsUser {

    @FXML
    private Button BTNselectPostForComments;

    @FXML
    private FlowPane FPcomments;

    @FXML
    public void initialize() {
        ServicePost servicePost = new ServicePost();
        try {
            BTNselectPostForComments.setOnAction(event -> handleSelectPostForComments());
            ObservableList<Post> posts = FXCollections.observableArrayList(servicePost.afficherAllPosts());
            for (Post post : posts) {
                ImageView imageView = new ImageView(new Image("file:" + post.getImage()));
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                Button postButton = new Button(post.getDescription(), imageView);
                postButton.setOnAction(event -> {
                    Post selectedPost = post;
                    postButton.setUserData(selectedPost);
                });
                FPcomments.getChildren().add(postButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSelectPostForComments() {
        for (Node node : FPcomments.getChildren()) {
            if (node instanceof Button) {
                Button postButton = (Button) node;
                postButton.setOnAction(event -> {
                    Post selectedPost = (Post) postButton.getUserData();
                    if (selectedPost != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterCommentsAdmin.fxml"));
                            Parent root = loader.load();

                            ajouterCommentsAdmin controller = loader.getController();
                            controller.setPostId(selectedPost.getIdp());

                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

}
