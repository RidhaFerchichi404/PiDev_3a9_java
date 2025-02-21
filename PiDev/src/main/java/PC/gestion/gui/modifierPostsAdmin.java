package PC.gestion.gui;

import PC.gestion.entities.Post;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import PC.gestion.services.ServicePost;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class modifierPostsAdmin implements Initializable {

    @FXML
    private Button BTNmodifier;

    @FXML
    private Button BTNsupprimer;

    @FXML
    private FlowPane FPposts;
    ;

    @FXML
    private RadioButton RBmarathonModifier;

    @FXML
    private RadioButton RBpromotionModifier;

    @FXML
    private RadioButton RBregimeModifier;

    @FXML
    private TextField TFdescriptionModifier;

    @FXML
    private TextField TFpathModifier;

    @FXML
    private ToggleGroup type;

    private Post selectedPost;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                    selectedPost = post;
                    TFdescriptionModifier.setText(post.getDescription());
                    TFpathModifier.setText(post.getImage());
                    switch (post.getType()) {
                        case "Marathon":
                            RBmarathonModifier.setSelected(true);
                            break;
                        case "Promotion":
                            RBpromotionModifier.setSelected(true);
                            break;
                        case "Regime":
                            RBregimeModifier.setSelected(true);
                            break;
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ModifierPoste() {
        if (selectedPost != null) {
            if (TFdescriptionModifier.getText().isEmpty() || TFpathModifier.getText().isEmpty() || type.getSelectedToggle() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Validation Error");
                alert.setContentText("Description, image path, and type cannot be empty.");
                alert.showAndWait();
                return;
            }
            selectedPost.setDescription(TFdescriptionModifier.getText());
            selectedPost.setImage(TFpathModifier.getText());
            selectedPost.setType(((RadioButton) type.getSelectedToggle()).getText());

            ServicePost servicePost = new ServicePost();
            try {
                servicePost.update(selectedPost);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("INFORMATION");
                alert.setContentText("Post modified successfully!");
                alert.showAndWait();
                selectedPost = null;
                refreshPosts();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Failed to modify post: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

@FXML
            private void SupprimerPoste() {
                if (selectedPost != null) {
                    ServicePost servicePost = new ServicePost();
                    try {
                        servicePost.delete(selectedPost);
                        FPposts.getChildren().removeIf(node -> {
                            if (node instanceof VBox) {
                                VBox vBox = (VBox) node;
                                for (Node child : vBox.getChildren()) {
                                    if (child instanceof Label) {
                                        Label label = (Label) child;
                                        return label.getText().equals(selectedPost.getType());
                                    }
                                }
                            }
                            return false;
                        });
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("INFORMATION");
                        alert.setContentText("Post deleted successfully!");
                        alert.showAndWait();
                        selectedPost = null;
                        refreshPosts();
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("ERROR");
                        alert.setContentText("Failed to delete post: " + e.getMessage());
                        alert.showAndWait();
                        e.printStackTrace();
                    }
                }
            }

private void refreshPosts() {
    FPposts.getChildren().clear();
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
                selectedPost = post;
                TFdescriptionModifier.setText(post.getDescription());
                TFpathModifier.setText(post.getImage());
                switch (post.getType()) {
                    case "Marathon":
                        RBmarathonModifier.setSelected(true);
                        break;
                    case "Promotion":
                        RBpromotionModifier.setSelected(true);
                        break;
                    case "Regime":
                        RBregimeModifier.setSelected(true);
                        break;
                }
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
