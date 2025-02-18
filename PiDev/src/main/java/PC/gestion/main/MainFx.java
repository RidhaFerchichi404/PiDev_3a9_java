package PC.gestion.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
            FXMLLoader listerLoader = new FXMLLoader(getClass().getResource("/listerPosts.fxml"));
            Parent listerRoot = listerLoader.load();

            FXMLLoader ajouterLoader = new FXMLLoader(getClass().getResource("/ajouterComments.fxml"));
            Parent ajouterRoot = ajouterLoader.load();

            FXMLLoader modifierLoader = new FXMLLoader(getClass().getResource("/modifierPosts.fxml"));
            Parent modifierRoot = modifierLoader.load();

            TabPane tabPane = new TabPane();

            Tab listerTab = new Tab("Lister Posts", listerRoot);
            Tab ajouterTab = new Tab("Ajouter Comments", ajouterRoot);
            Tab modifierTab = new Tab("Modifier Posts", modifierRoot);

            tabPane.getTabs().addAll(listerTab, ajouterTab, modifierTab);

            Scene scene = new Scene(tabPane);

            primaryStage.setTitle("Posts Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}