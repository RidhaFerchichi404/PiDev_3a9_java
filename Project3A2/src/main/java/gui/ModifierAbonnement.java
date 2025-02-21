package gui;

import entities.Abonnement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.AbonnementService;

public class ModifierAbonnement {

    @FXML
    private TextField TFNom;
    @FXML
    private TextField TFDescription;
    @FXML
    private TextField TFDuree;
    @FXML
    private TextField TFPrix;
    @FXML
    private TextField TFSalleNom;

    private Abonnement abonnementActuel; // L'abonnement à modifier
    private AbonnementService abonnementService = new AbonnementService(); // Service pour interagir avec la base de données

    /**
     * Charge les données de l'abonnement dans le formulaire.
     *
     * @param abonnement L'abonnement à modifier.
     */
    public void chargerAbonnement(Abonnement abonnement) {
        this.abonnementActuel = abonnement;
        TFNom.setText(abonnement.getNom());
        TFDescription.setText(abonnement.getDescriptiona());
        TFDuree.setText(String.valueOf(abonnement.getDuree()));
        TFPrix.setText(String.valueOf(abonnement.getPrix()));
        TFSalleNom.setText(abonnement.getSalleNom());
    }

    /**
     * Enregistre les modifications de l'abonnement.
     */
    @FXML
    private void enregistrerModifications() {
        try {
            // Valider les entrées
            if (TFNom.getText().isEmpty() || TFDescription.getText().isEmpty() ||
                    TFDuree.getText().isEmpty() || TFPrix.getText().isEmpty() || TFSalleNom.getText().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            // Mettre à jour les valeurs de l'abonnement
            abonnementActuel.setNom(TFNom.getText());
            abonnementActuel.setDescriptiona(TFDescription.getText());
            abonnementActuel.setDuree(Integer.parseInt(TFDuree.getText()));
            abonnementActuel.setPrix(Double.parseDouble(TFPrix.getText()));
            abonnementActuel.setSalleNom(TFSalleNom.getText());

            // Enregistrer les modifications dans la base de données
            abonnementService.update(abonnementActuel);

            // Afficher un message de succès
            afficherAlerte("Succès", "Abonnement modifié avec succès.", Alert.AlertType.INFORMATION);

            // Fermer la fenêtre de modification
            fermerFenetre();

        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "La durée et le prix doivent être des nombres valides.", Alert.AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            afficherAlerte("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            afficherAlerte("Erreur", "Une erreur est survenue lors de la modification : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Affiche une alerte avec un message.
     *
     * @param titre   Le titre de l'alerte.
     * @param message Le message à afficher.
     * @param type    Le type d'alerte (INFORMATION, ERROR, etc.).
     */
    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Ferme la fenêtre de modification.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) TFNom.getScene().getWindow();
        stage.close();
    }
}