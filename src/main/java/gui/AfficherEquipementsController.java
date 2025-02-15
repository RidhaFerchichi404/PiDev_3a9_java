package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.entities.Equipement;
import org.example.service.EquipementService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AfficherEquipementsController {

    @FXML private TableView<Equipement> tableEquipements;
    @FXML private TableColumn<Equipement, String> colNom;
    @FXML private TableColumn<Equipement, Boolean> colFonctionnement;
    @FXML private TableColumn<Equipement, Date> colDerniereVerif;
    @FXML private TableColumn<Equipement, Date> colProchaineVerif;
    @FXML private TableColumn<Equipement, Void> colActions;
    @FXML private Label noEquipementsLabel;

    private EquipementService equipementService = new EquipementService();
    private int idSalle;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    public void initialize() {
        setupColumns();
        loadEquipements();
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
        loadEquipements();
    }

    private void setupColumns() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        colFonctionnement.setCellValueFactory(new PropertyValueFactory<>("fonctionnement"));
        colFonctionnement.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "✔ Fonctionnel" : "❌ En panne");
                    setStyle(item ? "-fx-text-fill: #4CAF50; -fx-font-weight: bold;"
                            : "-fx-text-fill: #ff0000; -fx-font-weight: bold;");
                }
            }
        });

        colDerniereVerif.setCellValueFactory(new PropertyValueFactory<>("derniereVerification"));
        colDerniereVerif.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : dateFormat.format(item));
                setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            }
        });

        colProchaineVerif.setCellValueFactory(new PropertyValueFactory<>("prochaineVerification"));
        colProchaineVerif.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : dateFormat.format(item));
                setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            }
        });

        colActions.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox buttonsBox = new HBox(10, editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand;");
                deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-cursor: hand;");

                editButton.setOnAction(event -> {
                    Equipement equipement = getTableView().getItems().get(getIndex());
                    modifierEquipement(equipement);
                });

                deleteButton.setOnAction(event -> {
                    Equipement equipement = getTableView().getItems().get(getIndex());
                    supprimerEquipement(equipement);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });
    }

    private void loadEquipements() {
        try {
            List<Equipement> equipements = equipementService.readBySalleId(idSalle);
            tableEquipements.getItems().setAll(equipements);

            // ✅ Afficher ou cacher le tableau et le message
            if (equipements.isEmpty()) {
                tableEquipements.setVisible(false);
                noEquipementsLabel.setVisible(true);
            } else {
                tableEquipements.setVisible(true);
                noEquipementsLabel.setVisible(false);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des équipements", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void ajouterEquipement() {
        showAlert("Action", "Ajout d'un équipement (à implémenter)", Alert.AlertType.INFORMATION);
    }

    private void modifierEquipement(Equipement equipement) {
        showAlert("Action", "Modification de l'équipement : " + equipement.getNom(), Alert.AlertType.INFORMATION);
    }

    private void supprimerEquipement(Equipement equipement) {
        showAlert("Action", "Suppression de l'équipement : " + equipement.getNom(), Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
