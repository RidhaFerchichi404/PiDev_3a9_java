package gui;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class AdminDashboardController {

    @FXML
    public void initialize() {
        System.out.println("Admin Dashboard Initialized!");
    }

    @FXML
    private void handleTestButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Test");
        alert.setHeaderText(null);
        alert.setContentText("Test Button Clicked!");
        alert.showAndWait();
    }
}
