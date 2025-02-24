package gui;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;

import java.sql.SQLException;

public class ModifyUserController {

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtCin;

    @FXML
    private Button btnSave;

    private User currentUser;
    private UserService userService = new UserService();  // Initialisation du service

    /**
     * Remplit les champs du formulaire avec les données de l'utilisateur
     */
    public void setUser(User user) {
        this.currentUser = user;

        // ✅ Récupération correcte des champs (éviter les valeurs nulles)
        txtFirstName.setText(user.getFirstName() != null ? user.getFirstName() : "");
        txtLastName.setText(user.getLastName() != null ? user.getLastName() : "");
        txtEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        txtPhoneNumber.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : ""); // Téléphone
        txtCin.setText(user.getCin() != null ? user.getCin() : ""); // CIN

        // ✅ Désactiver le champ CIN si l'utilisateur a moins de 18 ans
        if (user.getAge() < 18) {
            txtCin.setDisable(true);
        } else {
            txtCin.setDisable(false);
        }
    }

    /**
     * Sauvegarder les modifications de l'utilisateur
     */
    @FXML
    public void saveChanges() {
        // ✅ Mise à jour des champs
        currentUser.setFirstName(txtFirstName.getText());
        currentUser.setLastName(txtLastName.getText());
        currentUser.setEmail(txtEmail.getText());

        // ✅ Validation des champs pour éviter les null
        String phoneNumber = txtPhoneNumber.getText();
        currentUser.setPhoneNumber(!phoneNumber.isEmpty() ? phoneNumber : null);

        // ✅ Sauvegarder le CIN seulement si l'utilisateur est majeur
        if (currentUser.getAge() >= 18) {
            String cin = txtCin.getText();
            currentUser.setCin(!cin.isEmpty() ? cin : null);
        } else {
            currentUser.setCin(null);
        }

        // ✅ Mise à jour de l'utilisateur
        try {
            userService.update(currentUser); // Mise à jour des données
            System.out.println("✅ Utilisateur mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Échec de la mise à jour de l'utilisateur : " + e.getMessage());
        }

        // ✅ Fermer la fenêtre après la sauvegarde
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}
