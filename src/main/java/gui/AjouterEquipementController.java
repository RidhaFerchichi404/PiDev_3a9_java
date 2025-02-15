package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.entities.Equipement;
import org.example.service.EquipementService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AjouterEquipementController {

    @FXML private TextField tfNom;
    @FXML private CheckBox cbFonctionnement;
    @FXML private DatePicker dpDerniereVerif;
    @FXML private DatePicker dpProchaineVerif;
    @FXML private Label titleLabel;
    @FXML private Button btnSave;

    private EquipementService equipementService;
    private Equipement equipementToUpdate;
    private int idSalle;
    private Runnable afterSaveAction;

    @FXML
    public void initialize() {
        equipementService = new EquipementService();
        
        // Validation du nom (max 50 caractères)
        tfNom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 50) {
                tfNom.setText(oldValue);
                showAlert("Erreur", "Le nom ne doit pas dépasser 50 caractères", Alert.AlertType.ERROR);
            }
        });

        // Validation des dates
        dpProchaineVerif.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (dpDerniereVerif.getValue() != null && newValue != null) {
                if (newValue.isBefore(dpDerniereVerif.getValue())) {
                    dpProchaineVerif.setValue(oldValue);
                    showAlert("Erreur", "La prochaine vérification doit être après la dernière vérification", Alert.AlertType.ERROR);
                }
            }
        });
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public void setAfterSaveAction(Runnable action) {
        this.afterSaveAction = action;
    }

    public void setEquipement(Equipement equipement) {
        this.equipementToUpdate = equipement;
        titleLabel.setText("Modifier l'Équipement");
        btnSave.setText("Modifier");
        
        tfNom.setText(equipement.getNom());
        cbFonctionnement.setSelected(equipement.isFonctionnement());
        
        if (equipement.getDerniereVerification() != null) {
            dpDerniereVerif.setValue(convertToLocalDate(equipement.getDerniereVerification()));
        }
        
        if (equipement.getProchaineVerification() != null) {
            dpProchaineVerif.setValue(convertToLocalDate(equipement.getProchaineVerification()));
        }
    }

    @FXML
    private void sauvegarder() {
        if (!validateInputs()) {
            return;
        }

        try {
            Equipement equipement;
            if (equipementToUpdate == null) {
                equipement = new Equipement(
                    idSalle,
                    tfNom.getText().trim(),
                    cbFonctionnement.isSelected(),
                    convertToDate(dpProchaineVerif.getValue()),
                    convertToDate(dpDerniereVerif.getValue()),
                    1  // ID utilisateur par défaut
                );
                equipementService.create(equipement);
            } else {
                equipementToUpdate.setNom(tfNom.getText().trim());
                equipementToUpdate.setFonctionnement(cbFonctionnement.isSelected());
                equipementToUpdate.setProchaineVerification(convertToDate(dpProchaineVerif.getValue()));
                equipementToUpdate.setDerniereVerification(convertToDate(dpDerniereVerif.getValue()));
                equipementService.update(equipementToUpdate);
            }

            if (afterSaveAction != null) {
                afterSaveAction.run();
            }
            
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'enregistrement: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void annuler() {
        closeWindow();
    }

    private boolean validateInputs() {
        if (tfNom.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le nom est obligatoire", Alert.AlertType.ERROR);
            return false;
        }

        if (dpDerniereVerif.getValue() == null) {
            showAlert("Erreur", "La date de dernière vérification est obligatoire", Alert.AlertType.ERROR);
            return false;
        }

        if (dpProchaineVerif.getValue() == null) {
            showAlert("Erreur", "La date de prochaine vérification est obligatoire", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        ((Stage) tfNom.getScene().getWindow()).close();
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
} 