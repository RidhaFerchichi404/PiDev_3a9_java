package PC.gestion.gui;

import PC.gestion.entities.Post;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import PC.gestion.services.ServicePost;
import PC.gestion.entities.Post;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class modifierPosts implements Initializable {

    @FXML
    private Button BTNmodifier;

    @FXML
    private ListView<Post> LVmodifierPost;

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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServicePost servicePost = new ServicePost();
        try {
            LVmodifierPost.getItems().addAll(servicePost.afficherAllPosts());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LVmodifierPost.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TFdescriptionModifier.setText(newValue.getDescription());
                TFpathModifier.setText(newValue.getImage());
                switch (newValue.getType()) {
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
            }
        });
    }

    @FXML
    private void ModifierPoste() {
        Post selectedPost = LVmodifierPost.getSelectionModel().getSelectedItem();
        if (selectedPost != null) {
            selectedPost.setDescription(TFdescriptionModifier.getText());
            selectedPost.setImage(TFpathModifier.getText());
            selectedPost.setType(((RadioButton) type.getSelectedToggle()).getText());

            ServicePost servicePost = new ServicePost();
            try {
                servicePost.update(selectedPost);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Post modified successfully!");
                alert.showAndWait();
                LVmodifierPost.refresh();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to modify post: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

}
