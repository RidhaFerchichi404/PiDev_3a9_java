package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.entities.SalleDeSport;
import org.example.service.SalleDeSportService;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficherSallesController implements Initializable {

    @FXML
    private VBox mainContainer;
    private FlowPane cardsContainer;
    private final SalleDeSportService salleService = new SalleDeSportService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupMainContainer();
        afficherSalles();
    }

    private void setupMainContainer() {
        cardsContainer = new FlowPane();
        cardsContainer.setHgap(30);
        cardsContainer.setVgap(30);
        cardsContainer.setPadding(new Insets(20));
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setStyle("-fx-background-color: #1a1a1a;");

        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        mainContainer.getChildren().add(scrollPane);
        mainContainer.setStyle("-fx-background-color: #1a1a1a;");
    }

    private VBox createSalleCard(SalleDeSport salle) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(300);
        card.setStyle("-fx-background-color: #262626; " +
                     "-fx-border-color: #ff8c00; " +
                     "-fx-border-width: 2px; " +
                     "-fx-border-radius: 15px; " +
                     "-fx-background-radius: 15px;");

        // Image Container
        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setMinHeight(200);
        imageBox.setMaxHeight(200);
        imageBox.setStyle("-fx-background-color: #333333; " +
                         "-fx-background-radius: 10px;");

        try {
            String imagePath = salle.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                // Essayer de charger l'image depuis le chemin absolu
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(280);
                    imageView.setFitHeight(180);
                    imageView.setPreserveRatio(true);
                    imageBox.getChildren().add(imageView);
                    System.out.println("Image chargée depuis: " + imagePath);
                } else {
                    // Si le fichier n'existe pas, essayer de charger depuis les ressources
                    String resourcePath = "/images/" + new File(imagePath).getName();
                    InputStream is = getClass().getResourceAsStream(resourcePath);
                    if (is != null) {
                        Image image = new Image(is);
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(280);
                        imageView.setFitHeight(180);
                        imageView.setPreserveRatio(true);
                        imageBox.getChildren().add(imageView);
                        System.out.println("Image chargée depuis les ressources: " + resourcePath);
                    } else {
                        addDefaultImage(imageBox);
                    }
                }
            } else {
                addDefaultImage(imageBox);
            }
        } catch (Exception e) {
            System.out.println("Erreur de chargement de l'image: " + e.getMessage());
            addDefaultImage(imageBox);
        }

        // Nom de la salle
        Label nameLabel = new Label(salle.getNom());
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ff8c00;");

        // Zone
        Label zoneLabel = new Label("📍 " + salle.getZone());
        zoneLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Boutons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);

        Button editButton = createButton("✏️ Modifier", "#ff8c00");
        Button deleteButton = createButton("🗑️ Supprimer", "#ff3333");

        editButton.setOnAction(e -> modifierSalle(salle));
        deleteButton.setOnAction(e -> supprimerSalle(salle));

        buttonsBox.getChildren().addAll(editButton, deleteButton);

        card.getChildren().addAll(imageBox, nameLabel, zoneLabel, buttonsBox);
        return card;
    }

    private void addDefaultImage(VBox container) {
        Label defaultIcon = new Label("🏋️");
        defaultIcon.setStyle("-fx-font-size: 64px; -fx-text-fill: #ff8c00;");
        container.getChildren().add(defaultIcon);
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; " +
                       "-fx-text-fill: white; " +
                       "-fx-font-size: 14px; " +
                       "-fx-padding: 8 20; " +
                       "-fx-background-radius: 20;");
        return button;
    }

    @FXML
    public void afficherSalles() {
        try {
            cardsContainer.getChildren().clear();
            for (SalleDeSport salle : salleService.readAll()) {
                cardsContainer.getChildren().add(createSalleCard(salle));
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des salles: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void supprimerSalle(SalleDeSport salle) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer la salle " + salle.getNom() + " ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                salleService.delete(salle);
                afficherSalles();
                showAlert("Succès", "Salle supprimée avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void modifierSalle(SalleDeSport salle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierSalle.fxml"));
            Parent root = loader.load();

            AjouterSalleController controller = loader.getController();
            controller.setModificationMode(salle);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier une Salle");
            stage.show();

            ((Stage) mainContainer.getScene().getWindow()).close();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de modification", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void retourAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutersalle.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Salle");
            stage.show();

            ((Stage) mainContainer.getScene().getWindow()).close();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du retour à l'ajout", Alert.AlertType.ERROR);
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
