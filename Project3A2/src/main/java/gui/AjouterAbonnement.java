package gui;

import entities.Abonnement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.AbonnementService;

public class AjouterAbonnement {

    @FXML
    private Button BTAjouter;

    @FXML
    private TextField TFDuree;

    @FXML
    private TextField TFNom;

    @FXML
    private TextField TFPrix;

    @FXML
    private TextField TFSalleNom;

    @FXML
    private TextField TfDescriptiona;

    @FXML
    void AjouterAbonnement(ActionEvent event) {
        try {
            // Validation des entrées
            String nom = TFNom.getText();
            String prixText = TFPrix.getText();
            String salle = TFSalleNom.getText();
            String description = TfDescriptiona.getText();
            String dureeText = TFDuree.getText();

            if (nom.isEmpty() || salle.isEmpty() || description.isEmpty() || prixText.isEmpty() || dureeText.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            int prix = Integer.parseInt(prixText);
            int duree = Integer.parseInt(dureeText);

            // Création de l'abonnement
            Abonnement abonnement = new Abonnement(nom, description, duree, prix, salle);
            AbonnementService abonnementService = new AbonnementService();
            abonnementService.create(abonnement);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Abonnement ajouté avec succès !");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Le prix et la durée doivent être des nombres valides.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de l'ajout de l'abonnement : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
