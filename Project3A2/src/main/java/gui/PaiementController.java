package gui;

import entities.Abonnement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class PaiementController {

    @FXML
    private WebView webView;
    @FXML
    private Label abonnementLabel;

    private Abonnement abonnement;

    @FXML
    public void initialize() {
        // Charger le formulaire de paiement Stripe
        String htmlContent = getStripePaymentForm("CLIENT_SECRET"); // Remplacez par le clientSecret
        webView.getEngine().loadContent(htmlContent);
    }
    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
        abonnementLabel.setText("Paiement pour : " + abonnement.getNom());
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

    private String getStripePaymentForm(String clientSecret) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Paiement avec Stripe</title>\n" +
                "    <script src=\"https://js.stripe.com/v3/\"></script>\n" +
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
                "            } else {\n" +
                "                alert('Paiement réussi !');\n" +
                "            }\n" +
                "        });\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}