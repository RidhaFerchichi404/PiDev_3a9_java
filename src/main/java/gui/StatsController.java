package gui;

import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.UserService;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StatsController implements Initializable {

    @FXML
    private VBox chartContainer;
    @FXML
    private Button backButton;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadAgeStatistics();
            loadLocationStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAgeStatistics() throws SQLException {
        List<User> users = userService.readAll();
        Map<String, Integer> ageGroups = new HashMap<>(){{
            put("0-18", 0);
            put("19-30", 0);
            put("31-45", 0);
            put("46+", 0);
        }};

        for (User user : users) {
            int age = user.getAge();
            if (age <= 18) ageGroups.compute("0-18", (k, v) -> v + 1);
            else if (age <= 30) ageGroups.compute("19-30", (k, v) -> v + 1);
            else if (age <= 45) ageGroups.compute("31-45", (k, v) -> v + 1);
            else ageGroups.compute("46+", (k, v) -> v + 1);
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> ageChart = new BarChart<>(xAxis, yAxis);

        xAxis.setLabel("Tranches d'âge");
        yAxis.setLabel("Nombre d'utilisateurs");
        ageChart.setTitle("Répartition par âge");
        ageChart.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Démographie");
        ageGroups.forEach((group, count) ->
                series.getData().add(new XYChart.Data<>(group, count)));

        ageChart.getData().add(series);
        chartContainer.getChildren().add(ageChart);
    }

    private void loadLocationStatistics() throws SQLException {
        List<User> users = userService.readAll();
        Map<String, Integer> locations = new HashMap<>();

        for (User user : users) {
            String location = user.getLocation();
            if (location == null || location.isEmpty()) {
                location = "Non renseigné";
            }
            locations.put(location, locations.getOrDefault(location, 0) + 1);
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        locations.forEach((loc, count) ->
                pieData.add(new PieChart.Data(String.format("%s (%d)", loc, count), count)));

        PieChart locationChart = new PieChart(pieData);
        locationChart.setTitle("Répartition géographique");
        locationChart.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
        locationChart.setLabelsVisible(true);
        locationChart.setLegendVisible(false);

        chartContainer.getChildren().add(locationChart);
    }

    @FXML
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}