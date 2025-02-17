package gui;

import entities.Abonnement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.stage.Stage;
import services.AbonnementService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherAbonnement {

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
    private TableColumn<Abonnement, String> colSalleNom;
    @FXML
    private TableColumn<Abonnement, Void> colActions; // Nouvelle colonne pour les boutons de suppression

    private VBox mainContainer;
    private Parent ajouterRoot;
    private AbonnementService abonnementService = new AbonnementService();

    public void setMainContainer(VBox mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void setAjouterRoot(Parent ajouterRoot) {
        this.ajouterRoot = ajouterRoot;
    }

    @FXML
    public void naviguerVersAjout() {
        if (mainContainer != null && ajouterRoot != null) {
            mainContainer.getChildren().setAll(ajouterRoot);
        }
    }

    // Méthode pour ouvrir le formulaire d'ajout de promotion
    private Runnable addPromotionAction;

    public void setAddPromotionAction(Runnable addPromotionAction) {
        this.addPromotionAction = addPromotionAction;
    }

    @FXML
    public void openAddPromotionForm() {
        if (addPromotionAction != null) {
            addPromotionAction.run();
        }}


    @FXML
    public void initialize() {
        System.out.println("Initialisation AfficherAbonnement...");

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptiona"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colSalleNom.setCellValueFactory(new PropertyValueFactory<>("salleNom"));

        ajouterBoutonSuppression(); // Ajout des boutons de suppression

        chargerAbonnements();
    }

    private void chargerAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementService.readAll();
            System.out.println("Nombre d'abonnements récupérés : " + abonnements.size());
            ObservableList<Abonnement> observableList = FXCollections.observableArrayList(abonnements);
            tableAbonnements.setItems(observableList);
            tableAbonnements.refresh();
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des abonnements : " + e.getMessage());
            e.printStackTrace(); // Ajoutez cette ligne pour voir la stack trace complète
        }
    }
    /*@FXML
    public void openAddPromotionForm() {
        try {
            // Charger le fichier FXML pour le formulaire d'ajout de promotion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajout_promotion.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour afficher le formulaire d'ajout de promotion
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Promotion");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private void ajouterBoutonSuppression() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnSupprimer = new Button("Supprimer");

            {
                btnSupprimer.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-border-radius: 5px;");
                btnSupprimer.setOnAction(event -> {
                    Abonnement abonnement = getTableView().getItems().get(getIndex());
                    supprimerAbonnement(abonnement);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnSupprimer);
                }
            }
        });
    }

    private void supprimerAbonnement(Abonnement abonnement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet abonnement ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    abonnementService.delete(abonnement);
                    chargerAbonnements(); // Rafraîchissement après suppression
                    System.out.println("Abonnement supprimé avec succès");
                } catch (SQLException e) {
                    System.out.println("Erreur lors de la suppression : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


}
