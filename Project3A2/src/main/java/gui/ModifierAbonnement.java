package gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import entities.Abonnement;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import services.AbonnementService;

public class ModifierAbonnement {


        private VBox mainContainer;
        private Parent ajouterRoot;

        public void setMainContainer(VBox mainContainer) {
            this.mainContainer = mainContainer;
        }

        public void setAjouterRoot(Parent ajouterRoot) {
            this.ajouterRoot = ajouterRoot;
        }

        // Méthode appelée lors de l'action d'ajout
        public void handleAjouterAction() {
            mainContainer.getChildren().setAll(ajouterRoot);
        }

    @FXML
    private TextField TFNom;
    @FXML
    private TextField TFDescription;
    @FXML
    private TextField TFSalleNom;
    @FXML
    private TextField TFPrix;
    @FXML
    private TextField TFDuree;

    private Abonnement abonnementActuel;


    // Méthode pour initialiser le formulaire avec les données de l'abonnement
    public void chargerAbonnement(Abonnement abonnement) {
        this.abonnementActuel = abonnement;
        TFNom.setText(abonnement.getNom());
        TFDescription.setText(abonnement.getDescriptiona());
        TFSalleNom.setText(abonnement.getSalleNom());
        TFPrix.setText(String.valueOf(abonnement.getPrix()));
        TFDuree.setText(String.valueOf(abonnement.getDuree()));
    }

    // Méthode appelée lors du clic sur le bouton "Enregistrer"
    @FXML
    private void modifierAbonnement(ActionEvent event) {
        try {
            // Récupération des valeurs des champs
            String nom = TFNom.getText();
            String description = TFDescription.getText();
            String salleNom = TFSalleNom.getText();
            double prix = Double.parseDouble(TFPrix.getText());
            int duree = Integer.parseInt(TFDuree.getText());

            // Validation des entrées
            if (nom.isEmpty() || description.isEmpty() || salleNom.isEmpty()) {
                throw new IllegalArgumentException("Les champs Nom, Description et SalleNom ne peuvent pas être vides.");
            }

            // Mise à jour de l'abonnement
            abonnementActuel.setNom(nom);
            abonnementActuel.setDescriptiona(description);
            abonnementActuel.setSalleNom(salleNom);
            abonnementActuel.setPrix(prix);
            abonnementActuel.setDuree(duree);

            // Appel du service pour mettre à jour l'abonnement dans la base de données
            AbonnementService abonnementService = new AbonnementService();
            abonnementService.update(abonnementActuel);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Abonnement modifié avec succès !");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Le prix et la durée doivent être des nombres valides.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la modification de l'abonnement : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
