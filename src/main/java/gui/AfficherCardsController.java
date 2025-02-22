package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import entities.SalleDeSport;
import services.SalleDeSportService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherCardsController implements Initializable {
    @FXML
    private FlowPane cardsContainer;

    private SalleDeSportService salleService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialisation du contrôleur");
        try {
            salleService = new SalleDeSportService();
            cardsContainer.setPadding(new Insets(20));
            cardsContainer.setHgap(20);
            cardsContainer.setVgap(20);
            actualiser();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void actualiser() {
        System.out.println("Actualisation des cards");
        if (cardsContainer == null) {
            System.err.println("cardsContainer est null!");
            return;
        }

        cardsContainer.getChildren().clear();
        try {
            List<SalleDeSport> salles = salleService.readAll();
            System.out.println("Nombre de salles trouvées: " + salles.size());
            for (SalleDeSport salle : salles) {
                cardsContainer.getChildren().add(createSalleCard(salle));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
            showAlert("Erreur", "Erreur lors du chargement des salles: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createSalleCard(SalleDeSport salle) {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                "-fx-background-color: #1a1a1a; " +
                        "-fx-padding: 20; " +
                        "-fx-border-color: #ff6600; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.4), 12, 0, 4, 4); " +
                        "-fx-min-width: 350; " +
                        "-fx-max-width: 350; " +
                        "-fx-min-height: 450;" +
                        "-fx-cursor: hand;");

        // ✅ Effet dynamique de survol
        card.setOnMouseEntered(event -> card.setStyle(
                "-fx-background-color: #262626; " +
                        "-fx-border-color: #ff6600; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.8), 15, 0, 6, 6); " +
                        "-fx-min-width: 350; " +
                        "-fx-max-width: 350; " +
                        "-fx-min-height: 450;"));

        card.setOnMouseExited(event -> card.setStyle(
                "-fx-background-color: #1a1a1a; " +
                        "-fx-border-color: #ff6600; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.4), 12, 0, 4, 4); " +
                        "-fx-min-width: 350; " +
                        "-fx-max-width: 350; " +
                        "-fx-min-height: 450;"));

        // ✅ Image centrée
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        try {
            String imagePath = salle.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                } else {
                    Image image = new Image(imagePath, true);
                    imageView.setImage(image);
                }
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
        }

        // ✅ Nom de la salle
        Label nameLabel = new Label(salle.getNom());
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(300);
        nameLabel.setStyle("-fx-text-fill: #ff6600; -fx-font-size: 22px; -fx-font-weight: bold;");

        // ✅ Zone
        Label zoneLabel = new Label("📍 " + salle.getZone());
        zoneLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        // ✅ Boutons alignés horizontalement
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);

        Button editButton = createStyledButton("Modifier");
        editButton.setOnAction(e -> modifierSalle(salle));

        Button deleteButton = createStyledButton("Supprimer");
        deleteButton.setOnAction(e -> supprimerSalle(salle));

        Button equipementsButton = createStyledButton("materiels");
        equipementsButton.setOnAction(e -> voirEquipements(salle));

        buttonsBox.getChildren().addAll(editButton, deleteButton, equipementsButton);

        // ✅ Regroupement des éléments
        VBox contentBox = new VBox(15, imageView, nameLabel, zoneLabel, buttonsBox);
        contentBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(contentBox);
        return card;
    }

    // ✅ Méthode pour créer des boutons stylés et uniformes
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(100);
        button.setStyle("-fx-background-color: #ff6600; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-cursor: hand;");

        // ✅ Effet de survol
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #ff3300; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #ff6600; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"));

        return button;
    }


    private void supprimerSalle(SalleDeSport salle) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer la salle " + salle.getNom() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                salleService.delete(salle);
                actualiser();
                showAlert("Succès", "Salle supprimée avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void ouvrirAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutersalle.fxml"));
            Parent sallesView = loader.load();

            // Remplacer le contenu actuel avec la nouvelle vue
            cardsContainer.getChildren().setAll(sallesView);
        } catch (IOException e) {
            e.printStackTrace();
        }}



    private void modifierSalle(SalleDeSport salle) {
        try {
            // Charger la vue de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierSalle.fxml"));
            Parent modifierSalleView = loader.load();

            // Passer les données de la salle sélectionnée au contrôleur
            ModifierSalleController controller = loader.getController();
            controller.setSalle(salle);

            // Remplacer le contenu du conteneur actuel (cardsContainer)
            cardsContainer.getChildren().setAll(modifierSalleView);

        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de modification : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace(); // Pour le débogage
        }
    }


    // Assurez-vous que cette méthode est présente pour rafraîchir l'affichage
    private void loadSalles() {
        try {
            cardsContainer.getChildren().clear();
            List<SalleDeSport> salles = salleService.readAll();
            for (SalleDeSport salle : salles) {
                cardsContainer.getChildren().add(createSalleCard(salle));
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des salles: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void voirEquipements(SalleDeSport salle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherEquipements.fxml"));
            Parent root = loader.load();

            AfficherEquipementsController controller = loader.getController();
            controller.setIdSalle(salle.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Équipements - " + salle.getNom());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la liste des équipements: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

