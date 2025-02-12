package PC.gestion.gui;

import PC.gestion.entities.Post;
import PC.gestion.services.ServicePost;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class ajouterPosts {

    @FXML
    private Button BTNajouterPost;

    @FXML
    private RadioButton RBmarathonPost;

    @FXML
    private RadioButton RBpromotionPost;

    @FXML
    private RadioButton RBregimePost;

    @FXML
    private TextField TFdescriptionPost;

    @FXML
    private TextField TFimagePathPost;

    @FXML
    private ToggleGroup type;

    @FXML
    void ajouterPost() {
        String description = TFdescriptionPost.getText();
        String image = TFimagePathPost.getText();
        String type = "";
        if (RBmarathonPost.isSelected()) {
            type = "Marathon";
        } else if (RBpromotionPost.isSelected()) {
            type = "Promotion";
        } else if (RBregimePost.isSelected()) {
            type = "Regime";
        }
        Post post = new Post(description, image, type);
        ServicePost servicePost = new ServicePost();
        try {
            servicePost.ajouter(post);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Post added successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add post: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
    }
}
}
