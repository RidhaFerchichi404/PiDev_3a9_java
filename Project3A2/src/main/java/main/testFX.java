package main;

import gui.*;
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
        Scene scene;
        try {
            // Chargement des vues FXML
            FXMLLoader ajouterLoader = new FXMLLoader(getClass().getResource("/AjouterAbonnement.fxml"));
            if (ajouterLoader.getLocation() == null) {
                System.err.println("Fichier FXML introuvable : AjouterAbonnement.fxml");
                return;
            }
            Parent ajouterRoot = ajouterLoader.load();

            FXMLLoader modifierLoader = new FXMLLoader(getClass().getResource("/ModifierAbonnement.fxml"));
            if (modifierLoader.getLocation() == null) {
                System.err.println("Fichier FXML introuvable : ModifierAbonnement.fxml");
                return;
            }
            Parent modifierRoot = modifierLoader.load();

            FXMLLoader afficherLoader = new FXMLLoader(getClass().getResource("/AfficherAbonnement.fxml"));
            if (afficherLoader.getLocation() == null) {
                System.err.println("Fichier FXML introuvable : AfficherAbonnement.fxml");
                return;
            }
            Parent afficherRoot = afficherLoader.load();

            FXMLLoader ajouterPromotionLoader = new FXMLLoader(getClass().getResource("/AjouterPromotion.fxml"));
            if (ajouterPromotionLoader.getLocation() == null) {
                System.err.println("Fichier FXML introuvable : AjouterPromotion.fxml");
                return;
            }
            Parent ajouterPromotionRoot = ajouterPromotionLoader.load();

            FXMLLoader promotionsLoader = new FXMLLoader(getClass().getResource("/affichageuser.fxml"));
            if (promotionsLoader.getLocation() == null) {
                System.err.println("Fichier FXML introuvable : AbonnementsAvecPromotions.fxml");
                return;
            }
            Parent promotionsRoot = promotionsLoader.load();

            // Création de la VBox pour la vue d'affichage des abonnements
            VBox vbox = new VBox(10); // Ajuste l'espacement entre les cartes
            vbox.getChildren().add(afficherRoot); // Ajoute l'affichage des abonnements
            scene = new Scene(vbox);

            // Récupération des contrôleurs
            AjouterAbonnement ajouterController = ajouterLoader.getController();
            ModifierAbonnement modifierController = modifierLoader.getController();
            AfficherAbonnement afficherController = afficherLoader.getController();
            AjouterPromotion ajouterPromotionController = ajouterPromotionLoader.getController();
            afficheruser promotionsController = promotionsLoader.getController();

            // Configuration des actions et de la navigation entre les vues
            ajouterController.setModifierRoot(modifierRoot);
            ajouterController.setMainContainer(vbox);
            afficherController.setMainContainer(vbox);
            afficherController.setAjouterRoot(ajouterRoot);

            // Configuration de la fenêtre principale
            primaryStage.setTitle("Gestion des Abonnements");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Initialiser le guide après que la scène est prête
            afficherController.initialiserGuide();

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des vues : " + e.getMessage());
            e.printStackTrace();
        }
    }
}