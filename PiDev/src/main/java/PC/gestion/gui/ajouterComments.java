package PC.gestion.gui;

import PC.gestion.entities.Post;
import PC.gestion.services.ServicePost;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ajouterComments {

    @FXML
    private Button BTNselectPostForComments;

    @FXML
    private ListView<Post> LVpostsAddComments;

    @FXML
    public void initialize() {
        ServicePost servicePost = new ServicePost();
        try {
            BTNselectPostForComments.setOnAction(event -> handleSelectPostForComments());
            ObservableList<Post> posts = FXCollections.observableArrayList(servicePost.afficherAllPosts());
            LVpostsAddComments.setItems(posts);
            LVpostsAddComments.setCellFactory(param -> new ListCell<Post>() {
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
    private void handleSelectPostForComments() {
        Post selectedPost = LVpostsAddComments.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterComments2.fxml"));
                Parent root = loader.load();

                ajouterComments2 controller = loader.getController();
                controller.setPostId(selectedPost.getIdp());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
