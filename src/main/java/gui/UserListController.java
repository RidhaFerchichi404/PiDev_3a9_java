package gui;

import entities.User;
import javafx.geometry.Pos;
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

import java.io.IOException;
import java.util.List;

public class UserListController {

    @FXML
    private FlowPane cardsContainer;

    private UserService userService;
    private User selectedUser;

    public UserListController() {
        // Instantiate your service
        this.userService = new UserService();
    }

    public void initialize() {
        loadUserList();
    }

    // Load all users from DB and display as cards
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

    // Create a card (VBox) for each user
    private VBox createUserCard(User user) {
        VBox userCard = new VBox(10);
        userCard.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-radius: 5;");
        userCard.setPrefWidth(180);

        ImageView userImage = new ImageView(new Image("file:images/user-placeholder.png"));
        userImage.setFitWidth(80);
        userImage.setFitHeight(80);

        Label userNameLabel = new Label(user.getFirstName());
        Label userEmailLabel = new Label(user.getEmail());
        Label userRoleLabel = new Label(user.getRole());

        userNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        userEmailLabel.setStyle("-fx-font-size: 12px;");
        userRoleLabel.setStyle("-fx-font-size: 12px;");

        // Delete button only (to demonstrate that editing is done elsewhere)
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> handleDelete(user));

        // Put the delete button in a small HBox
        HBox btnContainer = new HBox(deleteButton);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.setSpacing(5);

        // Add everything to the card
        userCard.getChildren().addAll(userImage, userNameLabel, userEmailLabel, userRoleLabel, btnContainer);

        // Selecting a user by clicking the card
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
            loadUserList(); // Refresh after deletion
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting user.");
        }
    }

    // --- The key method to open ModifyUser.fxml (without editing logic here) ---
    @FXML
    private void handleModifyButtonClick() {
        if (selectedUser == null) {
            System.out.println("No user selected for modification.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifyUser.fxml"));
            Parent modifyUserRoot = loader.load();

            // Let ModifyUserController do the actual editing
            ModifyUserController modifyController = loader.getController();
            modifyController.setUser(selectedUser); // Pass the user object

            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modifier l'utilisateur");
            modifyStage.setScene(new Scene(modifyUserRoot));
            modifyStage.initModality(Modality.APPLICATION_MODAL);
            modifyStage.showAndWait();

            // Refresh after the Modify window closes
            loadUserList();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load the ModifyUser.fxml");
        }
    }
}
