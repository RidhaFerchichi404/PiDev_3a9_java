package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.entities.Exercice;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherImagesExerciceController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private ImageView imageView;
    @FXML private Button closeButton;

    private Exercice exercice;
    private List<String> imagePaths;
    private int indexImage = 0;

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
        loadImages();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation si nécessaire
    }

    private void loadImages() {
        if (exercice != null && exercice.getImage() != null && !exercice.getImage().isEmpty()) {
            imagePaths = Arrays.asList(exercice.getImage().split(";"));
            afficherImage(indexImage);
        }
    }

    private void afficherImage(int index) {
        if (imagePaths == null || imagePaths.isEmpty()) return;

        String imagePath = imagePaths.get(index);
        File file = new File(imagePath);

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
