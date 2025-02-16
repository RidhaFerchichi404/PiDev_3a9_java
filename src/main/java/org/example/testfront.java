package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class testfront extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("📌 Chargement de l'interface des salles...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherfrontsalle.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);  // Définit une taille initiale correcte

            primaryStage.setTitle("🏋️ Welcome to Sportify 🏋️");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);  // ✅ Autorise le redimensionnement
            primaryStage.show();
            System.out.println("✅ Interface chargée avec succès !");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement de l'interface: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
