package gui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import entities.Equipement;
import services.EquipementService;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.imageio.ImageIO;

public class AfficherEquipementsController {

    @FXML private FlowPane cardsContainer;
    @FXML private Label noEquipementsLabel;
    @FXML private DatePicker calendrierGlobal;

    private EquipementService equipementService = new EquipementService();
    private int idSalle;

    // ✅ Twilio Credentials
    private static final String ACCOUNT_SID = "AC6061f00ef5486c9be7937f0fe8b5e2a5";
    private static final String AUTH_TOKEN = "e3cad76f6696c81567438c47d1d29b4a";
    private static final String FROM_PHONE = "+18312221870";
    private static final String TO_PHONE = "+21692524011";

    @FXML
    public void initialize() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        loadEquipements();
        configurerCalendrier();
        verifierDatesEtEnvoyerAlertes();
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
        loadEquipements();
    }

    private void configurerCalendrier() {
        try {
            List<Equipement> equipements = equipementService.readAll();

            calendrierGlobal.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (empty || item == null) {
                                setText(null);
                                setStyle("");
                            } else {
                                for (Equipement equipement : equipements) {
                                    if (equipement.getDerniereVerification() != null && item.equals(equipement.getDerniereVerification().toLocalDate())) {
                                        setTextFill(Color.GREEN);
                                        setFont(Font.font("Arial", 14));
                                        setStyle("-fx-background-color: #90EE90;");
                                    }

                                    if (equipement.getProchaineVerification() != null && item.equals(equipement.getProchaineVerification().toLocalDate())) {
                                        setTextFill(Color.RED);
                                        setFont(Font.font("Arial", 14));
                                        setStyle("-fx-background-color: #FF7F7F;");
                                    }
                                }
                            }
                        }
                    };
                }
            });
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du chargement des équipements : " + e.getMessage());
        }
    }

    private void verifierDatesEtEnvoyerAlertes() {
        try {
            List<Equipement> equipements = equipementService.readAll();
            LocalDate today = LocalDate.now();

            for (Equipement equipement : equipements) {
                if (equipement.getProchaineVerification() != null && equipement.getProchaineVerification().toLocalDate().isBefore(today)) {
                    envoyerAlerteSMS(equipement);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification des équipements : " + e.getMessage());
        }
    }

    private void envoyerAlerteSMS(Equipement equipement) {
        String message = "⚠️ Alerte ! L'équipement " + equipement.getNom() + " nécessite une vérification immédiate.";
        System.out.println("📩 Envoi SMS : " + message);

        // ✅ Envoi du SMS via Twilio
        Message.creator(
                new com.twilio.type.PhoneNumber(TO_PHONE),
                new com.twilio.type.PhoneNumber(FROM_PHONE),
                message
        ).create();

        System.out.println("✅ SMS envoyé avec succès !");
    }
    private void loadEquipements() {
        try {
            cardsContainer.getChildren().clear();
            List<Equipement> equipements = equipementService.readBySalleId(idSalle);

            if (equipements.isEmpty()) {
                noEquipementsLabel.setVisible(true);
                cardsContainer.setVisible(false);
            } else {
                noEquipementsLabel.setVisible(false);
                cardsContainer.setVisible(true);
                for (Equipement equipement : equipements) {
                    cardsContainer.getChildren().add(createEquipementCard(equipement));
                }
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des équipements", Alert.AlertType.ERROR);
        }
    }

    // ✅ Création d'une carte d'équipement bien alignée
    private VBox createEquipementCard(Equipement equipement) {
        // ✅ Carte principale agrandie
        VBox card = new VBox(20); // Espacement augmenté
        card.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-border-color: #FF6600;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 25;" +
                        "-fx-spacing: 15;" +
                        "-fx-alignment: center;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.5), 12, 0, 6, 6);" +
                        "-fx-cursor: hand;"
        );
        card.setPrefWidth(350);
        card.setPrefHeight(500);

        // ✅ Nom de l'équipement
        Label nomLabel = new Label("Nom: " + equipement.getNom());
        nomLabel.setStyle("-fx-text-fill: #FF6600; -fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ État de l'équipement
        Label etatLabel = new Label("État: " + (equipement.isFonctionnement() ? "✔ Fonctionnel" : "❌ En panne"));
        etatLabel.setStyle(equipement.isFonctionnement() ?
                "-fx-text-fill: #4CAF50; -fx-font-size: 16px;" :
                "-fx-text-fill: #FF4444; -fx-font-size: 16px;");

        // ✅ Dates de vérification
        Label derniereVerifLabel = new Label("Dernière vérification: " + equipement.getDerniereVerification());
        derniereVerifLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label prochaineVerifLabel = new Label("Prochaine vérification: " + equipement.getProchaineVerification());
        prochaineVerifLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // ✅ QR Code
        ImageView qrCodeImage = new ImageView(generateQRCode(equipement));
        qrCodeImage.setFitWidth(150);
        qrCodeImage.setFitHeight(150);
        qrCodeImage.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 10, 0, 2, 2);");

        // ✅ Boutons d'action agrandis
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);

        Button voirExercicesButton = createStyledButton("Exercices", "#FF6600");
        voirExercicesButton.setOnAction(event -> voirExercices(equipement));

        Button editButton = createStyledButton("Modifier", "#FFA500");
        editButton.setOnAction(event -> modifierEquipement(equipement));

        Button deleteButton = createStyledButton("Supprimer", "#FF4444");
        deleteButton.setOnAction(event -> supprimerEquipement(equipement));

        buttonsBox.getChildren().addAll(voirExercicesButton, editButton, deleteButton);

        // ✅ Ajout des éléments à la carte
        card.getChildren().addAll(
                nomLabel, etatLabel, derniereVerifLabel,
                prochaineVerifLabel, qrCodeImage, buttonsBox
        );

        return card;
    }

    // ✅ Méthode pour styliser les boutons avec une taille plus grande
    // ✅ Méthode pour styliser les boutons avec une taille fixe et du texte complet
    private Button createStyledButton(String text, String colorHex) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                        "-fx-text-fill: black;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 16px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 20;" +  // ✅ Padding équilibré
                        "-fx-min-width: 120;" +  // ✅ Largeur minimale des boutons pour afficher tout le texte
                        "-fx-alignment: center;" +  // ✅ Texte centré
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.3), 8, 0, 2, 2);"
        );

        // ✅ Effet hover
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #FF3300;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 16px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 20;" +
                        "-fx-min-width: 120;" +
                        "-fx-alignment: center;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.5), 10, 0, 2, 2);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                        "-fx-text-fill: black;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 16px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 20;" +
                        "-fx-min-width: 120;" +
                        "-fx-alignment: center;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 102, 0, 0.3), 8, 0, 2, 2);"
        ));

        return button;
    }


    private Image generateQRCode(Equipement equipement) {
        try {
            String qrData = "Nom: " + equipement.getNom() + "\n" +
                    "État: " + (equipement.isFonctionnement() ? "Fonctionnel" : "En panne") + "\n" +
                    "Dernière vérification: " + equipement.getDerniereVerification() + "\n" +
                    "Prochaine vérification: " + equipement.getProchaineVerification();

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (WriterException e) {
            System.err.println("❌ Erreur lors de la génération du QR Code : " + e.getMessage());
            return null;
        }
    }
    @FXML
    private void ajouterEquipement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterEquipement.fxml"));
            Parent root = loader.load();

            AjouterEquipementController controller = loader.getController();
            controller.setIdSalle(idSalle);
            controller.setAfterSaveAction(this::loadEquipements);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un équipement");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire", Alert.AlertType.ERROR);
        }
    }

    private void modifierEquipement(Equipement equipement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierEquipement.fxml"));
            Parent root = loader.load();

            ModifierEquipementController controller = loader.getController();
            controller.setEquipement(equipement);
            controller.setAfterSaveAction(this::loadEquipements);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'équipement");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la modification", Alert.AlertType.ERROR);
        }
    }

    private void supprimerEquipement(Equipement equipement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cet équipement ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                equipementService.delete(equipement);
                loadEquipements();
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression", Alert.AlertType.ERROR);
            }
        }
    }
    private void voirExercices(Equipement equipement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherExercices.fxml"));
            Parent root = loader.load();

            AfficherExercicesController controller = loader.getController();
            controller.setIdEquipement(equipement.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Exercices - " + equipement.getNom());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la liste des exercices", Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
