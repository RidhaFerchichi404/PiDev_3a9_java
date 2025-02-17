package main;

import gui.AfficherAbonnement;
import gui.AjouterAbonnement;
import gui.ModifierAbonnement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class testFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ajouterLoader = new FXMLLoader(getClass().getResource("/AjouterAbonnement.fxml"));
            Parent ajouterRoot = ajouterLoader.load();

            FXMLLoader modifierLoader = new FXMLLoader(getClass().getResource("/ModifierAbonnement.fxml"));
            Parent modifierRoot = modifierLoader.load();

            FXMLLoader afficherLoader = new FXMLLoader(getClass().getResource("/AfficherAbonnement.fxml"));
            Parent afficherRoot = afficherLoader.load();

            VBox vbox = new VBox(afficherRoot); // Démarre avec l'affichage des abonnements
            Scene scene = new Scene(vbox);

            AjouterAbonnement ajouterController = ajouterLoader.getController();
            ModifierAbonnement modifierController = modifierLoader.getController();
            AfficherAbonnement afficherController = afficherLoader.getController();

            // Configuration de la navigation
            ajouterController.setModifierRoot(modifierRoot);
            ajouterController.setMainContainer(vbox);
            modifierController.setAjouterRoot(ajouterRoot);
            modifierController.setMainContainer(vbox);
            afficherController.setMainContainer(vbox);
            afficherController.setAjouterRoot(ajouterRoot);

            // Configuration de la fenêtre principale
            primaryStage.setTitle("Gestion des Abonnements");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des vues : " + e.getMessage());
        }
    }
}
