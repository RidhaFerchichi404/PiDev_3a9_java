package main;

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


    public void start(Stage primaryStage) {
        try {
            // Chargement des fichiers FXML
            FXMLLoader ajouterLoader = new FXMLLoader(getClass().getResource("/AjouterAbonnement.fxml"));
            Parent ajouterRoot = ajouterLoader.load();

            FXMLLoader modifierLoader = new FXMLLoader(getClass().getResource("/ModifierAbonnement.fxml"));
            Parent modifierRoot = modifierLoader.load();

            // Création d'un conteneur principal
            VBox vbox = new VBox(ajouterRoot); // Par défaut, afficher la vue d'ajout
            Scene scene = new Scene(vbox);

            // Récupération des contrôleurs
            AjouterAbonnement ajouterController = ajouterLoader.getController();
            ModifierAbonnement modifierController = modifierLoader.getController();

            // Configuration de la navigation entre les vues
            ajouterController.setModifierRoot(modifierRoot);
            ajouterController.setMainContainer(vbox);
            modifierController.setAjouterRoot(ajouterRoot);
            modifierController.setMainContainer(vbox);

            // Configuration de la fenêtre principale
            primaryStage.setTitle("Gestion des Abonnements");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des vues : " + e.getMessage());
        }
    }

    }

