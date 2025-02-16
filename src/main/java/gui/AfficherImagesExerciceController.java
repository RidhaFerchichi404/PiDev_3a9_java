package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.example.entities.Exercice;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherImagesExerciceController implements Initializable {

    @FXML private FlowPane imagesContainer;
    @FXML private Button closeButton;

    private Exercice exercice;

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
        loadImages();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Peut être utilisé si nécessaire
    }

    private void loadImages() {
        imagesContainer.getChildren().clear();

        if (exercice != null && exercice.getImage() != null && !exercice.getImage().isEmpty()) {
            List<String> imagePaths = Arrays.asList(exercice.getImage().split(";")); // Séparateur ";"

            for (String imagePath : imagePaths) {
                File file = new File(imagePath);
                ImageView imageView = new ImageView();
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

                try {
                    if (file.exists()) {
                        imageView.setImage(new Image(file.toURI().toString()));
                    } else {
                        imageView.setImage(new Image(imagePath, true));
                    }
                } catch (Exception e) {
                    System.out.println("Erreur de chargement d'image: " + e.getMessage());
                }

                imagesContainer.getChildren().add(imageView);
            }
        }
    }

    @FXML
    public void fermerFenetre(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
