package PC.gestion.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loaderUser = new FXMLLoader(getClass().getResource("/InterfaceUser.fxml"));
            Parent rootUser = loaderUser.load();


            Scene scene = new Scene(rootUser);

            primaryStage.setTitle("Posts Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}