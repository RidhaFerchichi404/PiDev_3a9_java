package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.example.entities.Exercice;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherImagesExerciceController implements Initializable {

    @FXML private TilePane imagesContainer;
    @FXML private Button closeButton;

    private Exercice exercice;

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
        loadImages();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation si nécessaire
    }

    private void loadImages() {
        imagesContainer.getChildren().clear();

        if (exercice != null && exercice.getImage() != null && !exercice.getImage().isEmpty()) {
            List<String> imagePaths = Arrays.asList(exercice.getImage().split(";"));

            for (String imagePath : imagePaths) {
                File file = new File(imagePath);
                ImageView imageView = new ImageView();
                imageView.setFitWidth(220);
                imageView.setFitHeight(180);
                imageView.setPreserveRatio(true);
                imageView.setStyle("-fx-border-color: white; -fx-border-radius: 5; -fx-padding: 5;");

                try {
                    if (file.exists()) {
                        imageView.setImage(new Image(file.toURI().toString()));
                    } else {
                        imageView.setImage(new Image(imagePath, true));
                    }
                } catch (Exception e) {
                    System.out.println("Erreur de chargement d'image: " + e.getMessage());
                    imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
                }

                imagesContainer.getChildren().add(imageView);
            }
        }
    }

    @FXML
    public void fermerFenetre(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        } else {
            System.out.println("Erreur : Impossible de fermer la fenêtre !");
        }
    }
}
