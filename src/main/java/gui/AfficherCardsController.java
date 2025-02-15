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
import org.example.entities.SalleDeSport;
import org.example.service.SalleDeSportService;

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
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #262626; " +
                     "-fx-padding: 15; " +
                     "-fx-border-color: #ff8c00; " +
                     "-fx-border-radius: 10; " +
                     "-fx-background-radius: 10; " +
                     "-fx-min-width: 250; " +
                     "-fx-max-width: 250;");

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        try {
            String imagePath = salle.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                } else {
                    // Essayer de charger comme URL
                    Image image = new Image(imagePath, true);
                    imageView.setImage(image);
                }
            } else {
                // Charger l'image par défaut si le chemin est null ou vide
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
            }
        } catch (Exception e) {
            System.out.println("Erreur de chargement de l'image pour " + salle.getNom() + ": " + e.getMessage());
            try {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
            } catch (Exception ex) {
                System.out.println("Impossible de charger l'image par défaut");
            }
        }

        // Nom de la salle
        Label nameLabel = new Label(salle.getNom());
        nameLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Zone
        Label zoneLabel = new Label("📍 " + salle.getZone());
        zoneLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Boutons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);

        Button editButton = new Button("✏️ Modifier");
        editButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; " +
                          "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
        editButton.setOnAction(e -> modifierSalle(salle));

        Button deleteButton = new Button("🗑️ Supprimer");
        deleteButton.setStyle("-fx-background-color: #ff3333; -fx-text-fill: white; " +
                            "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
        deleteButton.setOnAction(e -> supprimerSalle(salle));

        Button equipementsButton = new Button("🏋️ Équipements");
        equipementsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                                 "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
        equipementsButton.setOnAction(e -> voirEquipements(salle));

        buttonsBox.getChildren().addAll(editButton, deleteButton, equipementsButton);
        card.getChildren().addAll(imageView, nameLabel, zoneLabel, buttonsBox);

        return card;
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
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Salle");
            stage.show();

            ((Stage) cardsContainer.getScene().getWindow()).close();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void modifierSalle(SalleDeSport salle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierSalle.fxml"));
            Parent root = loader.load();
            
            ModifierSalleController controller = loader.getController();
            controller.setSalle(salle);
            
            // Créer une nouvelle scène dans la fenêtre actuelle
            Stage currentStage = (Stage) cardsContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("Modifier la Salle");
            currentStage.show();
            
            // Ajouter un gestionnaire d'événements pour rafraîchir l'affichage après la modification
            currentStage.setOnHidden(event -> {
                loadSalles();  // Rafraîchir la liste des salles
            });
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de modification: " + e.getMessage(), Alert.AlertType.ERROR);
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

