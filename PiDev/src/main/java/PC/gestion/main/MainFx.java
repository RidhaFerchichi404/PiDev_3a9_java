package PC.gestion.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader modifierLoader = new FXMLLoader(getClass().getResource("/modifierPosts.fxml"));
            Parent modifierRoot = modifierLoader.load();

            FXMLLoader ajouterLoader = new FXMLLoader(getClass().getResource("/ajouterPosts.fxml"));
            Parent ajouterRoot = ajouterLoader.load();

            VBox vbox = new VBox(modifierRoot, ajouterRoot);
            vbox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
            Scene scene = new Scene(vbox);

            primaryStage.setTitle("Modifier and Ajouter Posts");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}