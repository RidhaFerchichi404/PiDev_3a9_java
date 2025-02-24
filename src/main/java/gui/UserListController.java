package gui;

import entities.User;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import services.UserService;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import utils.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserListController {

    @FXML
    private FlowPane cardsContainer;

    private UserService userService;
    private User selectedUser;

    public UserListController() {
        this.userService = new UserService();
    }

    public void initialize() {
        loadUserList();
    }

    private void loadUserList() {
        List<User> users = null;
        try {
            users = userService.readAll();
        } catch (SQLException e) {
            showError("Erreur de chargement", e.getMessage());
            return;
        }
        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        cardsContainer.getChildren().clear();

        for (User user : users) {
            VBox userCard = createUserCard(user);
            cardsContainer.getChildren().add(userCard);
        }
    }

    private VBox createUserCard(User user) {
        VBox userCard = new VBox(15);
        userCard.setStyle("-fx-background-color: #262626; " +
                "-fx-border-color: #ff8c00; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.4), 10, 0, 0, 3); " +
                "-fx-pref-width: 250; -fx-pref-height: 320;");
        userCard.setAlignment(Pos.CENTER);

        // ✅ Image de profil par défaut
        ImageView imageView = new ImageView();
        try {
            // Charge l'image par défaut
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/user-placeholder.png")));
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'image par défaut : " + e.getMessage());
        }

        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setStyle("-fx-background-radius: 50%; -fx-border-radius: 50%;");

        // ✅ Informations de l'utilisateur
        Label userNameLabel = new Label(user.getFirstName());
        Label userEmailLabel = new Label(user.getEmail());
        Label userRoleLabel = new Label(user.getRole());

        userNameLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 18px; -fx-font-weight: bold;");
        userEmailLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");
        userRoleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // ✅ Bouton Supprimer (orange vif)
        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        deleteButton.setOnAction(event -> handleDelete(user)); // Action de suppression

        // ✅ Conteneur des boutons
        HBox btnContainer = new HBox(deleteButton);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.setSpacing(10);

        // ✅ Action de sélection
        userCard.setOnMouseClicked(event -> {
            selectedUser = user;
            System.out.println("Utilisateur sélectionné : " + user.getFirstName());
        });

        // ✅ Ajout des éléments dans la carte
        userCard.getChildren().addAll(imageView, userNameLabel, userEmailLabel, userRoleLabel, btnContainer);

        return userCard;
    }


    private void handleDelete(User user) {
        try {
            userService.deleteUser(user);
            System.out.println("User deleted: " + user.getFirstName());
            loadUserList(); // Refresh the user list after deletion
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting user.");
        }
    }


    @FXML
    private void handleModifyButtonClick() {
        if (selectedUser == null) {
            System.out.println("No user selected for modification.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifyUser.fxml"));
            Parent modifyUserRoot = loader.load();

            ModifyUserController modifyController = loader.getController();
            modifyController.setUser(selectedUser); // Pass the selected user

            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modifier l'utilisateur");
            modifyStage.setScene(new Scene(modifyUserRoot));
            modifyStage.initModality(Modality.APPLICATION_MODAL);
            modifyStage.showAndWait();

            loadUserList(); // Refresh the user list after modification
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load the ModifyUser.fxml");
        }
    }

    @FXML
    private void handleajoutButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAdminUser.fxml"));
            Parent addUserRoot = loader.load();

            // Récupérer le contrôleur d'ajout pour d'éventuelles actions après enregistrement
            AjouterAdminController addUserController = loader.getController();

            // Configurer une action après l'ajout d'un utilisateur pour rafraîchir la liste
            addUserController.setAfterSaveAction(this::loadUserList);

            // Ouvrir une nouvelle fenêtre modale pour ajouter l'utilisateur
            Stage addUserStage = new Stage();
            addUserStage.setTitle("Ajouter un Utilisateur");
            addUserStage.setScene(new Scene(addUserRoot));
            addUserStage.initModality(Modality.APPLICATION_MODAL);
            addUserStage.showAndWait();

            loadUserList(); // Rafraîchir la liste des utilisateurs après ajout
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de AjouterAdminUser.fxml");
        }
    }


    private void showError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
