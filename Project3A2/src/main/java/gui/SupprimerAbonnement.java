package gui;

import entities.Abonnement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class SupprimerAbonnement {
    @FXML
    private TableView<Abonnement> tableAbonnements;
    @FXML
    private TableColumn<Abonnement, String> colNom;
    @FXML
    private TableColumn<Abonnement, String> colDescription;
    @FXML
    private TableColumn<Abonnement, Integer> colDuree;
    @FXML
    private TableColumn<Abonnement, Double> colPrix;
    @FXML
    private TableColumn<Abonnement, Void> colActions;

    private ObservableList<Abonnement> abonnements = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configuration des colonnes
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Ajouter des données fictives


        // Ajouter une colonne d'actions avec un bouton de suppression
        Callback<TableColumn<Abonnement, Void>, TableCell<Abonnement, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Abonnement, Void> call(final TableColumn<Abonnement, Void> param) {
                final TableCell<Abonnement, Void> cell = new TableCell<>() {
                    private final Button btnSupprimer = new Button("Supprimer");

                    {
                        btnSupprimer.setOnAction(event -> {
                            Abonnement abonnement = getTableView().getItems().get(getIndex());
                            supprimerAbonnement(abonnement);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnSupprimer);
                        }
                    }
                };
                return cell;
            }
        };

        colActions.setCellFactory(cellFactory);

        tableAbonnements.setItems(abonnements);
    }

    private void supprimerAbonnement(Abonnement abonnement) {
        abonnements.remove(abonnement);
        // Ajoutez ici la logique pour supprimer l'abonnement de la base de données si nécessaire
        System.out.println("Abonnement supprimé : " + abonnement.getNom());
    }
}
