package gui;

import entities.User;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import services.UserService;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import utils.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserListController {

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private TextField searchTextField;  // Search text field

    private UserService userService;
    private User selectedUser;
    private VBox selectedCard;
    private final String defaultCardStyle = "-fx-background-color: #262626; "
            + "-fx-border-color: #ff8c0055; -fx-border-width: 2; "
            + "-fx-border-radius: 10; -fx-background-radius: 10; "
            + "-fx-padding: 20; -fx-pref-width: 250; -fx-pref-height: 320; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(255,140,0,0.3), 10, 0, 0, 3);";

    private final String hoveredCardStyle = defaultCardStyle
            + "-fx-background-color: #2d2d2d; "
            + "-fx-border-color: #ff8c00; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(255,140,0,0.5), 15, 0.1, 0, 5);";

    private final String selectedCardStyle = defaultCardStyle
            + "-fx-background-color: #333333; "
            + "-fx-border-color: #ffcc00; "
            + "-fx-effect: dropshadow(three-pass-box, rgba(255,204,0,0.8), 20, 0.2, 0, 7);";

    public UserListController() {
        this.userService = new UserService();
    }

    public void initialize() {
        loadUserList();
        // Set up search functionality
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
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
        userCard.setStyle(defaultCardStyle);
        userCard.setAlignment(Pos.CENTER);

        // Style constant pour les éléments internes
        String imageStyle = "-fx-background-radius: 50%; -fx-border-radius: 50%;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);";

        // Configuration de l'image
        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/user-placeholder.png")));
        } catch (Exception e) {
            System.out.println("Erreur image : " + e.getMessage());
        }
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setStyle(imageStyle);

        // Labels avec style constant
        Label userNameLabel = new Label(user.getFirstName());
        Label userEmailLabel = new Label(user.getEmail());
        Label userRoleLabel = new Label(user.getRole());

        // Application des styles
        userNameLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 18px; -fx-font-weight: bold;");
        userEmailLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");
        userRoleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Bouton Supprimer
        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;"
                + "-fx-background-radius: 5; -fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1);");
        deleteButton.setOnAction(event -> handleDelete(user));

        HBox btnContainer = new HBox(deleteButton);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.setSpacing(10);

        // Gestion améliorée des événements
        userCard.setOnMouseEntered(event -> {
            if (userCard != selectedCard) {
                userCard.setStyle(hoveredCardStyle);
                animateCard(userCard, 1.02); // Animation légère
            }
        });

        userCard.setOnMouseExited(event -> {
            if (userCard != selectedCard) {
                userCard.setStyle(defaultCardStyle);
                animateCard(userCard, 1.0);
            }
        });

        userCard.setOnMouseClicked(event -> handleCardSelection(userCard, user));

        userCard.getChildren().addAll(imageView, userNameLabel, userEmailLabel, userRoleLabel, btnContainer);
        return userCard;
    }

    private void animateCard(VBox card, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
        st.setFromX(card.getScaleX());
        st.setFromY(card.getScaleY());
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    private void handleCardSelection(VBox newCard, User user) {
        if (selectedCard != null) {
            selectedCard.setStyle(defaultCardStyle);
            animateCard(selectedCard, 1.0);
        }

        selectedCard = newCard;
        selectedUser = user;
        newCard.setStyle(selectedCardStyle);
        animateCard(newCard, 1.02);
        System.out.println("Sélection : " + user.getFirstName());
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

    private void filterUsers(String searchQuery) {
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

        // Filter users based on the search query
        List<User> filteredUsers = users.stream()
                .filter(user -> user.getFirstName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        user.getLastName().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());

        // Clear existing cards and reload with filtered users
        cardsContainer.getChildren().clear();
        for (User user : filteredUsers) {
            VBox userCard = createUserCard(user);
            cardsContainer.getChildren().add(userCard);
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
    // Add this method to handle the stats button click
    @FXML
    private void handleStatsButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stat.fxml"));
            Parent statsRoot = loader.load();

            Stage statsStage = new Stage();
            statsStage.setTitle("Statistiques");
            statsStage.setScene(new Scene(statsRoot));
            statsStage.initModality(Modality.APPLICATION_MODAL); // Optional: Makes the window modal
            statsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir la page des statistiques.");
        }
    }

    @FXML
    private void handleSearchTextChanged(KeyEvent event) {
        String searchText = searchTextField.getText().toLowerCase();
        List<User> filteredUsers = null;

        try {
            List<User> allUsers = userService.readAll();
            filteredUsers = allUsers.stream()
                    .filter(user -> user.getFirstName().toLowerCase().contains(searchText) ||
                            user.getLastName().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            showError("Erreur de recherche", e.getMessage());
            return;
        }

        cardsContainer.getChildren().clear();

        if (filteredUsers != null) {
            for (User user : filteredUsers) {
                VBox userCard = createUserCard(user);
                cardsContainer.getChildren().add(userCard);
            }
        }
    }
}