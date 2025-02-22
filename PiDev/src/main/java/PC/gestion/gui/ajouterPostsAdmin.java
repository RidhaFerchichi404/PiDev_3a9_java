package PC.gestion.gui;

import PC.gestion.entities.Post;
import PC.gestion.services.ServicePost;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class ajouterPostsAdmin {

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
        if (image.isEmpty() || description.isEmpty() || !description.matches("[a-zA-Z0-9,.!?'’#\\s*\\-–]+") || type.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("WARNING");
            if (image.isEmpty()) {
                alert.setContentText("Image path cannot be empty.");
            } else if (description.isEmpty() || !description.matches("[a-zA-Z0-9,.!?'’#\\s*\\-–]+")) {
                alert.setContentText("Description cannot be empty and must only contain letters and numbers.");
            } else if (type.isEmpty()) {
                alert.setContentText("Type cannot be empty.");
            }
            alert.showAndWait();
            return;
        }
        Post post = new Post(description, image, type);
        ServicePost servicePost = new ServicePost();
        try {
            servicePost.ajouter(post);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("INFORMATION");
            alert.setContentText("Post added successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR");
            alert.setContentText("Failed to add post: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
    }
}
}
