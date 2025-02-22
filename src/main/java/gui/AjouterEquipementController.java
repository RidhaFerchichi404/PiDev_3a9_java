package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.entities.Equipement;
import org.example.service.EquipementService;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterEquipementController {

    @FXML private TextField nomField;
    @FXML private CheckBox fonctionnementCheck;
    @FXML private DatePicker derniereVerifDate;
    @FXML private DatePicker prochaineVerifDate;
    @FXML private Button enregistrerButton;

    private int idSalle;
    private int idUser = 1;  // ✅ ID utilisateur par défaut (modifiable si besoin)
    private Runnable afterSaveAction;

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public void setAfterSaveAction(Runnable afterSaveAction) {
        this.afterSaveAction = afterSaveAction;
    }

    @FXML
    private void enregistrerEquipement() {
        try {
            // ✅ Vérification des champs avant enregistrement
            if (nomField.getText().trim().isEmpty()) {
                showAlert("Erreur", "Le champ 'Nom' ne peut pas être vide.", Alert.AlertType.ERROR);
                return;
            }

            LocalDate derniereVerif = derniereVerifDate.getValue();
            LocalDate prochaineVerif = prochaineVerifDate.getValue();

            // ✅ Validation des dates
            if (derniereVerif != null && prochaineVerif != null) {
                if (derniereVerif.isAfter(prochaineVerif)) {
                    showAlert("Erreur", "La dernière vérification ne peut pas être après la prochaine vérification.", Alert.AlertType.ERROR);
                    return;
                }
            }

            if (prochaineVerif != null && prochaineVerif.isBefore(LocalDate.now())) {
                showAlert("Erreur", "La prochaine vérification ne peut pas être dans le passé.", Alert.AlertType.ERROR);
                return;
            }

            Equipement equipement = new Equipement();
            equipement.setNom(nomField.getText().trim());
            equipement.setFonctionnement(fonctionnementCheck.isSelected());
            equipement.setIdSalle(idSalle);
            equipement.setIdUser(idUser);  // ✅ Ajout de l'utilisateur lié à l'équipement

            // ✅ Conversion des dates LocalDate → java.sql.Date
            equipement.setDerniereVerification(derniereVerif != null ? Date.valueOf(derniereVerif) : null);
            equipement.setProchaineVerification(prochaineVerif != null ? Date.valueOf(prochaineVerif) : null);

            // ✅ Ajout dans la base de données
            EquipementService service = new EquipementService();
            service.create(equipement);
            System.out.println("✅ Équipement ajouté avec succès !");

            if (afterSaveAction != null) afterSaveAction.run(); // Recharge la liste après ajout
            fermerFenetre();

        } catch (SQLException e) {
            System.err.println("❌ ERREUR lors de l'ajout : " + e.getMessage());
            showAlert("Erreur", "Impossible d'ajouter l'équipement.\nVérifie que l'utilisateur existe.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void fermerFenetre() {
        ((Stage) enregistrerButton.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
