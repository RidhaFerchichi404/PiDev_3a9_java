package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.entities.Exercice;
import org.example.service.ExerciceService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherExercicesController {

    @FXML private FlowPane exercicesContainer;
    @FXML private Label noExercicesLabel;
    @FXML private Button ajouterExerciceButton;

    private ExerciceService exerciceService = new ExerciceService();
    private int idEquipement;

    public void setIdEquipement(int idEquipement) {
        this.idEquipement = idEquipement;
        System.out.println("📌 ID équipement reçu: " + idEquipement);
        loadExercices();
    }

    private void loadExercices() {
        try {
            System.out.println("📌 Chargement des exercices pour l'équipement ID: " + idEquipement);

            exercicesContainer.getChildren().clear();
            List<Exercice> exercices = exerciceService.readByEquipementId(idEquipement);

            System.out.println("📌 Nombre d'exercices trouvés: " + exercices.size());

            if (exercices.isEmpty()) {
                noExercicesLabel.setVisible(true);
                exercicesContainer.setVisible(false);
            } else {
                noExercicesLabel.setVisible(false);
                exercicesContainer.setVisible(true);
                for (Exercice exercice : exercices) {
                    exercicesContainer.getChildren().add(createExerciceCard(exercice));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des exercices: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("❌ Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur inattendue lors du chargement des exercices.", Alert.AlertType.ERROR);
        }
    }

    // ✅ Création d'une carte d'exercice avec bouton "Voir les images"
    private VBox createExerciceCard(Exercice exercice) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #262626; -fx-border-color: #ff8c00; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; "
                + "-fx-spacing: 10; -fx-alignment: center;");
        card.setPrefWidth(280);

        Label nomLabel = new Label("Exercice: " + exercice.getNomExercice());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label descriptionLabel = new Label("Description: " + exercice.getDescription());
        descriptionLabel.setStyle("-fx-text-fill: #cccccc;");

        // ImageView
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        try {
            String imagePath = exercice.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString()));
                } else {
                    imageView.setImage(new Image(imagePath, true));
                }
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
            }
        } catch (Exception e) {
            System.out.println("Erreur de chargement de l'image: " + e.getMessage());
        }

        // ✅ Boutons d'actions
        HBox buttonsBox = new HBox(10);
        buttonsBox.setStyle("-fx-alignment: center;");

        Button voirImagesButton = new Button("Voir Images");
        voirImagesButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        voirImagesButton.setOnAction(event -> voirImagesExercice(exercice));

        Button editButton = new Button("Modifier");
        editButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-weight: bold;");
        editButton.setOnAction(event -> modifierExercice(exercice));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setOnAction(event -> supprimerExercice(exercice));

        buttonsBox.getChildren().addAll(voirImagesButton, editButton, deleteButton);

        card.getChildren().addAll(nomLabel, descriptionLabel, imageView, buttonsBox);
        return card;
    }

    @FXML
    private void ajouterExercice() {
        try {
            System.out.println("📌 Début ouverture du formulaire d'ajout...");
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ajouterExercice.fxml"));

            if (loader.getLocation() == null) {
                throw new IOException("❌ Le fichier FXML ajouterExercice.fxml est introuvable !");
            }

            Parent root = loader.load();
            System.out.println("✅ Chargement réussi !");

            AjouterExerciceController controller = loader.getController();
            controller.setIdEquipement(idEquipement);
            controller.setAfterSaveAction(this::loadExercices);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un exercice");
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'ouverture du formulaire: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout", Alert.AlertType.ERROR);
        }
    }

    private void modifierExercice(Exercice exercice) {
        try {
            System.out.println("🔄 Ouverture du formulaire de modification...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierExercice.fxml"));
            Parent root = loader.load();

            ModifierExerciceController controller = loader.getController();
            if (controller == null) {
                System.err.println("❌ Erreur : Impossible de récupérer le contrôleur !");
                return;
            }

            controller.setExercice(exercice);
            controller.setAfterSaveAction(this::loadExercices);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'exercice");
            stage.initModality(Modality.APPLICATION_MODAL); // Empêcher d'ouvrir plusieurs fois
            stage.show();

            System.out.println("✅ Modification en cours !");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la modification", Alert.AlertType.ERROR);
        }
    }


    private void supprimerExercice(Exercice exercice) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer l'exercice : " + exercice.getNomExercice() + " ?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                System.out.println("🔄 Suppression en cours de l'exercice ID: " + exercice.getId());

                // Utilisation correcte de la méthode delete() qui supprime par ID
                exerciceService.delete(exercice);

                System.out.println("✅ Suppression réussie !");
                loadExercices(); // Rafraîchir l'affichage des exercices après suppression
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la suppression : " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }


    private void voirImagesExercice(Exercice exercice) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherImagesExercice.fxml"));
            Parent root = loader.load();

            AfficherImagesExerciceController controller = loader.getController();
            controller.setExercice(exercice);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Images - " + exercice.getNomExercice());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'afficher les images", Alert.AlertType.ERROR);
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
