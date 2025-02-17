package gui;

import entities.User;
import services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class ProfileCardController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Button modifyButton;

    @FXML
    private Button deleteButton;

    private User user;
    private Runnable refreshCallback; // Callback pour rafraîchir la page principale

    private UserService userService = new UserService();

    public void setUser(User user) {
        this.user = user;
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        emailLabel.setText(user.getEmail());
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    private void handleModify() {
        // Ici, vous pouvez ouvrir un formulaire de modification ou effectuer une action de mise à jour
        System.out.println("Modifier l'utilisateur : " + user.getFirstName());
        // Par exemple, on peut appeler userService.updateUser(user);
        // Ensuite, rafraîchir la page principale
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }

    @FXML
    private void handleDelete() {
        // Appeler le service pour supprimer l'utilisateur
        userService.deleteUser(user);
        // Puis rafraîchir la page principale
        if (refreshCallback != null) {
            refreshCallback.run();
        }
    }
}
