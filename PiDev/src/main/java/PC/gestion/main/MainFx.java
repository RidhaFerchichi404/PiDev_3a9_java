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
            FXMLLoader loaderAdmin = new FXMLLoader(getClass().getResource("/InterfaceAdmin.fxml"));
            Parent rootAdmin = loaderAdmin.load();
            FXMLLoader loaderUser = new FXMLLoader(getClass().getResource("/InterfaceUser.fxml"));
            Parent rootUser = loaderUser.load();

            HBox root = new HBox(rootAdmin, rootUser);
            Scene scene = new Scene(root);

            primaryStage.setTitle("Posts Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}