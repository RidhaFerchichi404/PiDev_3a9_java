package gui;

import entities.User;
import javafx.geometry.Pos;
import services.UserService;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.List;

public class UserListController {

    @FXML
    private FlowPane cardsContainer; // This holds the user cards

    private UserService userService;
    private User selectedUser; // To store the currently selected user


    public UserListController() {
        this.userService = new UserService(); // Instantiate the UserService
    }

    // This method is called when the scene is loaded
    public void initialize() {
        loadUserList();
    }


    private void loadUserList() {
        try {
            List<User> users = userService.readAll(); // Get all users from the database

            if (users == null || users.isEmpty()) {
                showEmptyMessage();
                return;
            }

            cardsContainer.getChildren().clear(); // Clear any existing cards

            for (User user : users) {
                VBox userCard = createUserCard(user); // Create a card for each user
                cardsContainer.getChildren().add(userCard); // Add the card to the container
            }

        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for more info on the error
            showError("Error loading users", "Unable to load users from the database.");
        }
    }


    private VBox createUserCard(User user) {
        // Create the VBox for the user card
        VBox userCard = new VBox(10);
        userCard.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 10; -fx-padding: 10;");
        userCard.setPrefWidth(180);

        // User image (placeholder or real image)
        ImageView userImage = new ImageView(new Image("file:images/user-placeholder.png"));  // Placeholder image
        userImage.setFitWidth(100);
        userImage.setFitHeight(100);

        // User name
        Label userNameLabel = new Label(user.getFirstName());
        userNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // User email
        Label userEmailLabel = new Label(user.getEmail());
        userEmailLabel.setStyle("-fx-font-size: 12px;");

        // User role
        Label userRoleLabel = new Label(user.getRole());
        userRoleLabel.setStyle("-fx-font-size: 12px;");

        // Create a container for the labels
        VBox labelsContainer = new VBox(5);
        labelsContainer.getChildren().addAll(userNameLabel, userEmailLabel, userRoleLabel);

        // Create buttons for edit and delete
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        editButton.setOnAction(event -> handleEdit(user));

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> handleDelete(user));

        // Create a container for the buttons
        HBox buttonsContainer = new HBox(10);
        buttonsContainer.getChildren().addAll(editButton, deleteButton);
        buttonsContainer.setAlignment(Pos.CENTER);

        // Add components to the user card
        userCard.getChildren().addAll(userImage, labelsContainer, buttonsContainer);

        // Add a click event to select the user for modification
        userCard.setOnMouseClicked(event -> {
            selectedUser = user; // Set the selected user
            System.out.println("User selected: " + user.getFirstName()); // Optional log
        });

        return userCard;
    }


    private void handleEdit(User user) {
        // Handle the edit button click (e.g., open user details for editing)
        System.out.println("Editing user: " + user.getFirstName());
        // Implement the edit functionality here
    }

    private void handleDelete(User user) {
        // Handle the delete button click
        try {
            userService.deleteUser(user);
            cardsContainer.getChildren().removeIf(node -> {
                VBox card = (VBox) node;
                Label nameLabel = (Label) card.getChildren().get(0);
                return nameLabel.getText().equals(user.getFirstName());
            });
            System.out.println("User deleted: " + user.getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error deleting user", "Unable to delete the user.");
        }
    }


    private void showEmptyMessage() {
        // Handle the case where there are no users
        System.out.println("No users found");
        // Optionally, display a message in the UI
    }

    private void showError(String title, String message) {
        // Handle error message
        System.out.println(title + ": " + message);
        // Optionally, display an error message in the UI
    }
    // Method to handle Modify button click
    public void openModifyUserForm(User selectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyUser.fxml"));
            Parent modifyUserRoot = loader.load();

            // Access ModifyUserController to set the selected user
            ModifyUserController modifyController = loader.getController();
            modifyController.setUser(selectedUser); // Pass the selected user

            // Open in a new Stage
            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modifier Utilisateur");
            modifyStage.setScene(new Scene(modifyUserRoot));
            modifyStage.showAndWait(); // Wait for modifications

            // After closing, refresh the user list
            refreshUserList(); // Implement this to reload the users into the FlowPane
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void refreshUserList() {
        List<User> users = userService.getAllUsers(); // Get updated list from the database
        cardsContainer.getChildren().clear(); // Clear old cards

        for (User user : users) {
            // Create and add each user card (use your existing card creation logic here)
            Node userCard = createUserCard(user);
            cardsContainer.getChildren().add(userCard);
        }

    }
    @FXML
    private void handleModifyButtonClick() {
        if (selectedUser == null) {
            System.out.println("No user selected for modification.");
            return; // If no user is selected, we don't proceed
        }

        try {
            // Load the FXML for ModifyUser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyUser.fxml"));
            Parent modifyUserRoot = loader.load();  // Load the FXML

            // Get the controller of the ModifyUser view
            ModifyUserController modifyUserController = loader.getController();

            // Pass the selected user to the ModifyUserController
            modifyUserController.setUser(selectedUser);

            // Create a new Stage for the ModifyUser view
            Stage stage = new Stage();
            stage.setTitle("Modifier l'utilisateur");
            stage.setScene(new Scene(modifyUserRoot));  // Set the scene for the new stage
            stage.initModality(Modality.APPLICATION_MODAL); // Optional: to make it modal (blocks the main window)
            stage.show();  // Show the new stage
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load the ModifyUser view.");
        }
    }




}
