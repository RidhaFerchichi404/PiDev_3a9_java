package gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import entities.Abonnement;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.AbonnementService;

import java.io.IOException;
import java.sql.SQLException;

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
    private TextField TFRechercheNom;
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
    private AbonnementService abonnementService = new AbonnementService();
    private Abonnement abonnementSelectionne;

    @FXML
    void naviguerVersAjout(ActionEvent event) {
        if (mainContainer != null && ajouterRoot != null) {
            mainContainer.getChildren().setAll(ajouterRoot); // Afficher la vue d'ajout
        }
    }
    @FXML
    void rechercherAbonnement() {
        String nomRechercher = TFRechercheNom.getText().trim();
        if (nomRechercher.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez entrer un nom d'abonnement !");
            return;
        }

        try {
            abonnementSelectionne = abonnementService.getAbonnementByNom(nomRechercher);
            if (abonnementSelectionne != null) {
                TFNom.setText(abonnementSelectionne.getNom());
                TFDescription.setText(abonnementSelectionne.getDescriptiona());
                TFDuree.setText(String.valueOf(abonnementSelectionne.getDuree()));
                TFPrix.setText(String.valueOf(abonnementSelectionne.getPrix()));
                TFSalleNom.setText(abonnementSelectionne.getSalleNom());
            } else {
                afficherAlerte("Aucun résultat", "Aucun abonnement trouvé avec ce nom.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Erreur lors de la recherche : " + e.getMessage());
        }
    }
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
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
   /* @FXML
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
    }*/
    @FXML
    /*private void modifierAbonnement(ActionEvent event) {
        try {
            /*if (abonnementActuel == null) {
                afficherAlerte("Erreur", "Aucun abonnement à modifier.");
                return;
            }

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
    }*/
    void modifierAbonnement(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs
            String nom = TFNom.getText();
            String description = TFDescription.getText();
            String salleNom = TFSalleNom.getText();
            double prix = Double.parseDouble(TFPrix.getText());
            int duree = Integer.parseInt(TFDuree.getText());

            // Vérifier que les champs ne sont pas vides
            if (nom.isEmpty() || description.isEmpty() || salleNom.isEmpty()) {
                System.out.println("Tous les champs doivent être remplis.");
                return;
            }

            // Créer un objet Abonnement avec les nouvelles valeurs
            Abonnement abonnement = new Abonnement(nom, description, duree, prix, salleNom);

            // Modifier l'abonnement dans la base de données
            abonnementService.update(abonnement);

            System.out.println("Abonnement modifié avec succès !");
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format : Le prix et la durée doivent être des nombres valides.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification de l'abonnement : " + e.getMessage());
            e.printStackTrace(); // Afficher la stack trace pour le débogage
        }
    }
    @FXML
    private TableView<Abonnement> tableAbonnements;
    @FXML
    private void naviguerVersAffichageAbonnements() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AfficherAbonnements.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) tableAbonnements.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
