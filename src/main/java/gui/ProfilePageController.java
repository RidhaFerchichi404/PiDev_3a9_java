package gui;

import entities.User;
import services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProfilePageController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private FlowPane cardsPane;

    private UserService userService = new UserService();
    private List<User> allUsers; // Liste complète des utilisateurs

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Charger la liste initiale depuis la BDD via UserService
        allUsers = userService.getAllUsers();
        displayCards(allUsers);

        // Mettre en place la recherche en filtrant la liste à chaque modification du champ
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<User> filtered = allUsers.stream()
                    .filter(u -> (u.getFirstName() + " " + u.getLastName())
                            .toLowerCase().contains(newVal.toLowerCase()))
                    .collect(Collectors.toList());
            displayCards(filtered);
        });
    }

    // Méthode pour afficher les cartes dans le FlowPane
    private void displayCards(List<User> users) {
        cardsPane.getChildren().clear();
        for (User u : users) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileCard.fxml"));
                Parent card = loader.load();
                ProfileCardController cardController = loader.getController();
                cardController.setUser(u);
                // Définir un callback pour rafraîchir la page après modification/suppression
                cardController.setRefreshCallback(() -> refresh());
                cardsPane.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Recharge la liste d'utilisateurs et réaffiche les cartes (à appeler après une suppression ou modification)
    public void refresh() {
        allUsers = userService.getAllUsers();
        String filter = searchField.getText();
        if (filter != null && !filter.isEmpty()) {
            List<User> filtered = allUsers.stream()
                    .filter(u -> (u.getFirstName() + " " + u.getLastName())
                            .toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
            displayCards(filtered);
        } else {
            displayCards(allUsers);
        }
    }
}
