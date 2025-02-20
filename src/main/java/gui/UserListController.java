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
        List<User> users = userService.readAll();
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
        VBox userCard = new VBox(10);
        userCard.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20; -fx-border-radius: 10; -fx-pref-width: 250; -fx-pref-height: 300;");

        ImageView userImage = new ImageView(new Image("file:images/user-placeholder.png"));
        userImage.setFitWidth(100);
        userImage.setFitHeight(100);

        Label userNameLabel = new Label(user.getFirstName());
        Label userEmailLabel = new Label(user.getEmail());
        Label userRoleLabel = new Label(user.getRole());

        userNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
        userEmailLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        userRoleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> handleDelete(user)); // Attach delete action

        HBox btnContainer = new HBox(deleteButton);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.setSpacing(5);

        userCard.getChildren().addAll(userImage, userNameLabel, userEmailLabel, userRoleLabel, btnContainer);

        userCard.setOnMouseClicked(event -> {
            selectedUser = user;
            System.out.println("Selected user: " + user.getFirstName());
        });

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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
