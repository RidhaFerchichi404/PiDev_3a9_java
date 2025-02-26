package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.awt.*;
import java.net.URI;

public class FlouciPaymentForm extends VBox {

    private TextField amountField;
    private TextField descriptionField;
    private Button payButton;

    public FlouciPaymentForm() {
        // Initialiser les composants
        amountField = new TextField();
        amountField.setPromptText("Montant (TND)");

        descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        payButton = new Button("Payer avec Flouci");
        payButton.setOnAction(e -> handlePayButtonClick());

        // Ajouter les composants au conteneur
        this.getChildren().addAll(
                new Label("Montant (TND):"), amountField,
                new Label("Description:"), descriptionField,
                payButton
        );

        // Style optionnel
        this.setSpacing(10);
        this.setPadding(new javafx.geometry.Insets(15));
    }

    private void handlePayButtonClick() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();

            // Appeler la méthode pour initier le paiement
            initiateFlouciPayment(amount, description);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant valide.", Alert.AlertType.ERROR);
        }
    }

    private void initiateFlouciPayment(double amount, String description) {
        // Appeler l'API Flouci pour initier le paiement
        String apiKey = "VOTRE_CLE_API"; // Remplacez par votre clé API Flouci
        String merchantId = "VOTRE_MERCHANT_ID"; // Remplacez par votre ID de marchand

        try {
            // Construire la requête JSON
            String jsonRequest = String.format(
                    "{\"amount\": %.2f, \"currency\": \"TND\", \"description\": \"%s\"}",
                    amount, description
            );

            // Envoyer la requête POST à l'API Flouci
            String response = sendHttpPostRequest(
                    "https://api.flouci.com/payment/init",
                    jsonRequest,
                    apiKey,
                    merchantId
            );

            // Traiter la réponse
            System.out.println("Réponse de Flouci: " + response);

            // Rediriger l'utilisateur vers l'URL de paiement
            String paymentUrl = extractPaymentUrlFromResponse(response);
            if (paymentUrl != null) {
                openBrowser(paymentUrl);
            } else {
                showAlert("Erreur", "Impossible d'extraire l'URL de paiement.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors du traitement du paiement.", Alert.AlertType.ERROR);
        }
    }

    private String sendHttpPostRequest(String url, String jsonRequest, String apiKey, String merchantId) throws Exception {
        // Utiliser Apache HttpClient pour envoyer une requête POST
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // Ajouter les en-têtes
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + apiKey);
        httpPost.setHeader("Merchant-Id", merchantId);

        // Ajouter le corps de la requête
        StringEntity entity = new StringEntity(jsonRequest);
        httpPost.setEntity(entity);

        // Exécuter la requête
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());

        // Fermer les ressources
        response.close();
        httpClient.close();

        return responseBody;
    }

    private String extractPaymentUrlFromResponse(String response) {
        // Extraire l'URL de paiement de la réponse JSON
        try {
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getString("payment_url");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void openBrowser(String url) {
        // Ouvrir l'URL de paiement dans le navigateur par défaut
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}