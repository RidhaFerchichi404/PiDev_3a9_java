package gui;

import entities.Abonnement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import services.AbonnementService;

public class AjouterAbonnement {
    @FXML
    private TextField TFNom;
    @FXML
    private TextField TfDescriptiona;
    @FXML
    private TextField TFDuree;
    @FXML
    private TextField TFPrix;
    @FXML
    private TextField TFSalleNom;
    @FXML
    private Button BTAjouter;
    private VBox mainContainer;
    private Parent modifierRoot;

    public void setMainContainer(VBox mainContainer) {
        this.mainContainer = mainContainer;
    }

    public void setModifierRoot(Parent modifierRoot) {
        this.modifierRoot = modifierRoot;
    }


    @FXML
    void AjouterAbonnement(ActionEvent event) {
        try {
            // Récupération des valeurs des champs
            String nom = TFNom.getText();
            String description = TfDescriptiona.getText();
            String dureeText = TFDuree.getText();
            String prixText = TFPrix.getText();
            String salleNom = TFSalleNom.getText();

            // Validation des entrées
            if (nom.isEmpty() || description.isEmpty() || dureeText.isEmpty() || prixText.isEmpty() || salleNom.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            // Conversion des champs numériques
            int duree = Integer.parseInt(dureeText);
            double prix = Double.parseDouble(prixText);

            // Création de l'abonnement
            Abonnement abonnement = new Abonnement(nom, description, duree, prix, salleNom);
            AbonnementService abonnementService = new AbonnementService();
            abonnementService.create(abonnement);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Abonnement ajouté avec succès !");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            // Gestion des erreurs de format numérique
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de format");
            alert.setHeaderText(null);
            alert.setContentText("Le prix et la durée doivent être des valeurs numériques valides.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            // Gestion des erreurs de validation
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            // Gestion des autres exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur est survenue lors de l'ajout de l'abonnement : " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();

        }}

        }
