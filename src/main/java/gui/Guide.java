package gui;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Guide {

    private Stage primaryStage;
    private Node elementASurligner;

    public Guide(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Affiche un guide pour ajouter un abonnement.
     */
    public void afficherGuideAjoutAbonnement() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Guide : Ajouter un Abonnement");
        alert.setHeaderText("Suivez ces étapes pour ajouter un abonnement :");

        // Contenu du guide
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("1. Cliquez sur le bouton 'Ajouter un abonnement'."),
                new Label("2. Remplissez les champs du formulaire (nom, description, durée, prix, salle)."),
                new Label("3. Cliquez sur 'Valider' pour enregistrer l'abonnement.")
        );

        // Personnalisation de l'alerte
        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20;");

        // Afficher l'alerte
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    /**
     * Affiche un guide pour ajouter une promotion.
     */
    public void afficherGuideAjoutPromotion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Guide : Ajouter une Promotion");
        alert.setHeaderText("Suivez ces étapes pour ajouter une promotion :");

        // Contenu du guide
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("1. Sélectionnez un abonnement dans la liste."),
                new Label("2. Cliquez sur le bouton 'Ajouter une promotion'."),
                new Label("3. Remplissez les champs du formulaire (code promo, réduction, dates)."),
                new Label("4. Cliquez sur 'Valider' pour enregistrer la promotion.")
        );

        // Personnalisation de l'alerte
        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20;");

        // Afficher l'alerte
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    /**
     * Surligne un élément de l'interface utilisateur.
     *
     * @param element L'élément à surligner.
     */
    public void surlignerElement(Node element) {
        if (element == null) return;

        // Effacer le surlignage précédent
        if (elementASurligner != null) {
            elementASurligner.setEffect(null);
        }

        // Ajouter un effet de surbrillance
        Glow glow = new Glow();
        glow.setLevel(0.8);
        element.setEffect(glow);

        // Ajouter un rectangle coloré autour de l'élément
        Rectangle highlight = new Rectangle(
                element.getBoundsInParent().getWidth(),
                element.getBoundsInParent().getHeight(),
                Color.TRANSPARENT
        );
        highlight.setStroke(Color.ORANGE);
        highlight.setStrokeWidth(2);
        highlight.setLayoutX(element.getLayoutX());
        highlight.setLayoutY(element.getLayoutY());

        // Ajouter le rectangle au parent de l'élément
        if (element.getParent() != null) {
            ((javafx.scene.Parent) element.getParent()).getChildrenUnmodifiable().add(highlight);
        }

        // Mettre à jour l'élément actuellement surligné
        elementASurligner = element;
    }

    /**
     * Efface le surlignage de l'élément actuel.
     */
    public void effacerSurlignage() {
        if (elementASurligner != null) {
            elementASurligner.setEffect(null);
            elementASurligner = null;
        }
    }
}