package gui;

import entities.Abonnement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

import java.io.IOException;

public class PaiementController {

    @FXML
    private WebView webView;

    @FXML
    private Label abonnementLabel;

    private Abonnement abonnement;

    @FXML
    public void initialize() {
        // Activer JavaScript dans le WebView
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        // Intercepter les messages JavaScript
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Injecter un objet Java dans le JavaScript pour la communication
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaController", this);
            }
        });

        // Charger le formulaire de paiement Stripe
        String htmlContent = getStripePaymentForm("CLIENT_SECRET"); // Remplacez par le clientSecret
        webEngine.loadContent(htmlContent);
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
        if (abonnementLabel != null) {
            abonnementLabel.setText("Paiement pour : " + abonnement.getNom());
        } else {
            System.err.println("Erreur : abonnementLabel est null");
        }
    }

    public void setClientSecret(String clientSecret) {
        // Charger le formulaire de paiement Stripe avec le clientSecret
        String htmlContent = getStripePaymentForm(clientSecret);
        webView.getEngine().loadContent(htmlContent);
    }

    @FXML
    private void handleRetourButtonClick() {
        try {
            // Charger la vue précédente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichageuser.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) webView.getScene().getWindow();

            // Changer la scène pour afficher la vue précédente
            stage.setScene(new Scene(root));
            stage.setTitle("Abonnements avec Promotions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée depuis JavaScript pour afficher un message de succès
    public void showSuccessMessage(String message) {
        javafx.application.Platform.runLater(() -> {
            System.out.println("Paiement réussi : " + message);
            // Afficher un message de succès dans l'interface JavaFX
            showAlert("Succès", message, Alert.AlertType.INFORMATION);
        });
    }

    // Méthode appelée depuis JavaScript pour afficher un message d'erreur
    public void showErrorMessage(String message) {
        javafx.application.Platform.runLater(() -> {
            System.out.println("Erreur de paiement : " + message);
            // Afficher un message d'erreur dans l'interface JavaFX
            showAlert("Erreur", message, Alert.AlertType.ERROR);
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getStripePaymentForm(String clientSecret) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Paiement avec Stripe</title>\n" +
                "    <script src=\"https://js.stripe.com/v3/\"></script>\n" +
                "    <style>\n" +
                "        #card-element {\n" +
                "            padding: 10px;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 4px;\n" +
                "            background-color: white;\n" +
                "        }\n" +
                "\n" +
                "        #submit {\n" +
                "            margin-top: 10px;\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            border: none;\n" +
                "            border-radius: 4px;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "\n" +
                "        #submit:hover {\n" +
                "            background-color: #45a049;\n" +
                "        }\n" +
                "\n" +
                "        #error-message {\n" +
                "            color: red;\n" +
                "            margin-top: 10px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <form id=\"payment-form\">\n" +
                "        <div id=\"card-element\"></div>\n" +
                "        <button id=\"submit\">Payer</button>\n" +
                "        <div id=\"error-message\"></div>\n" +
                "    </form>\n" +
                "\n" +
                "    <script>\n" +
                "        const stripe = Stripe('pk_test_51QxPCdPteRZuLQ9NlMsCxUYUYUrjuBLlEs2NbIGx7o24jVMea2AazFQHtPTr4C18fjnfxVH3oqL5SHxzjxBZzku700V8hkMV7r'); // Clé publique Stripe\n" +
                "        const elements = stripe.elements();\n" +
                "        const cardElement = elements.create('card');\n" +
                "        cardElement.mount('#card-element');\n" +
                "\n" +
                "        const form = document.getElementById('payment-form');\n" +
                "        form.addEventListener('submit', async (event) => {\n" +
                "            event.preventDefault();\n" +
                "\n" +
                "            const { paymentIntent, error } = await stripe.confirmCardPayment(\n" +
                "                '" + clientSecret + "', // ClientSecret\n" +
                "                {\n" +
                "                    payment_method: {\n" +
                "                        card: cardElement,\n" +
                "                    },\n" +
                "                }\n" +
                "            );\n" +
                "\n" +
                "            if (error) {\n" +
                "                document.getElementById('error-message').textContent = error.message;\n" +
                "                javaController.showErrorMessage(error.message); // Appeler la méthode JavaFX\n" +
                "            } else {\n" +
                "                javaController.showSuccessMessage('Paiement réussi !'); // Appeler la méthode JavaFX\n" +
                "            }\n" +
                "        });\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}