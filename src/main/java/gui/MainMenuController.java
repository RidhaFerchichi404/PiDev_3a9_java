package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void handleAddUser(ActionEvent event) throws IOException {
        // Charger la scène AjouterUser.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/AjouterUser.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleViewCards(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ProfileCard.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Profile Cards");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
