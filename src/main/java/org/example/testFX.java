package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class testFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Chargement de l'interface...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutersalle.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            primaryStage.setTitle("Gestion des Salles de Sport");
            primaryStage.setScene(scene);
            primaryStage.show();
            System.out.println("Interface chargée avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'interface: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
