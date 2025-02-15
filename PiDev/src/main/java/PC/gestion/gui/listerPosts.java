package PC.gestion.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import PC.gestion.entities.Post;
import PC.gestion.services.ServicePost;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class listerPosts {

    @FXML
    private Button BTNshowSelectedPost;

    @FXML
    private ListView<Post> LVposts;

    @FXML
    public void initialize() {
        ServicePost servicePost = new ServicePost();
        try {
            ObservableList<Post> posts = FXCollections.observableArrayList(servicePost.afficherAllPosts());
            LVposts.setItems(posts);
            LVposts.setCellFactory(param -> new ListCell<Post>() {
                private ImageView imageView = new ImageView();

                @Override
                protected void updateItem(Post post, boolean empty) {
                    super.updateItem(post, empty);
                    if (empty || post == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        imageView.setImage(new Image("file:" + post.getImage()));
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);
                        setText(post.getDescription());
                        setGraphic(imageView);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleShowSelectedPost() {
        Post selectedPost = LVposts.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPosts.fxml"));
                Parent root = loader.load();

                afficherPosts controller = loader.getController();
                controller.setPost(selectedPost);
                controller.setPreviousScene(BTNshowSelectedPost.getScene());

                Stage stage = (Stage) BTNshowSelectedPost.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Afficher Post");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Post Selected");
            alert.setContentText("Please select a post in the list.");
            alert.showAndWait();
        }
    }

}
