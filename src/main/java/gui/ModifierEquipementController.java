package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Equipement;
import services.EquipementService;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class ModifierEquipementController {

    @FXML private TextField nomField;
    @FXML private CheckBox fonctionnementCheck;
    @FXML private DatePicker derniereVerifDate;
    @FXML private DatePicker prochaineVerifDate;
    @FXML private Button enregistrerButton;

    private Equipement equipement;
    private Runnable afterSaveAction; // Fonction pour rafraîchir la liste après modification

    // ✅ Associer l'équipement aux champs de la fenêtre
    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
        System.out.println("📌 Chargement de l'équipement : " + equipement.getNom());

        nomField.setText(equipement.getNom());
        fonctionnementCheck.setSelected(equipement.isFonctionnement());

        // ✅ Vérification et conversion des dates sql.Date → LocalDate
        derniereVerifDate.setValue(convertToLocalDate(equipement.getDerniereVerification()));
        prochaineVerifDate.setValue(convertToLocalDate(equipement.getProchaineVerification()));
    }

    // ✅ Fonction pour convertir java.sql.Date en LocalDate (évite les erreurs)
    private LocalDate convertToLocalDate(Date sqlDate) {
        if (sqlDate == null) return null;
        return Instant.ofEpochMilli(sqlDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setAfterSaveAction(Runnable afterSaveAction) {
        this.afterSaveAction = afterSaveAction;
    }

    @FXML
    private void enregistrerModification() {
        try {
            // ✅ Vérification des champs avant l'enregistrement
            if (nomField.getText().trim().isEmpty()) {
                showAlert("Erreur", "Le champ 'Nom' ne peut pas être vide.", Alert.AlertType.ERROR);
                return;
            }

            equipement.setNom(nomField.getText());
            equipement.setFonctionnement(fonctionnementCheck.isSelected());

            // ✅ Vérification et conversion LocalDate → java.sql.Date
            equipement.setDerniereVerification(derniereVerifDate.getValue() != null
                    ? Date.valueOf(derniereVerifDate.getValue()) : null);

            equipement.setProchaineVerification(prochaineVerifDate.getValue() != null
                    ? Date.valueOf(prochaineVerifDate.getValue()) : null);

            EquipementService service = new EquipementService();
            service.update(equipement);
            System.out.println("✅ Équipement modifié avec succès !");

            if (afterSaveAction != null) afterSaveAction.run(); // Recharge la liste
            fermerFenetre(); // Fermer la fenêtre après modification

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification : " + e.getMessage());
            showAlert("Erreur", "Impossible de modifier l'équipement.", Alert.AlertType.ERROR);
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
