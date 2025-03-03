package gui;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsService {
    // 🔹 Configuration Twilio (Priorité aux variables d'environnement pour la sécurité)

    // 🔹 Initialisation Twilio
    static {
        if (ACCOUNT_SID == null || ACCOUNT_SID.isEmpty() ||
                AUTH_TOKEN == null || AUTH_TOKEN.isEmpty() ||
                FROM_PHONE == null || FROM_PHONE.isEmpty()) {
            System.err.println("❌ Erreur : Les informations Twilio sont manquantes !");
        } else {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            System.out.println("✅ Twilio initialisé avec succès !");
        }
    }

    public static void sendSms(String phoneNumber, String messageBody) {
        if (ACCOUNT_SID == null || ACCOUNT_SID.isEmpty() ||
                AUTH_TOKEN == null || AUTH_TOKEN.isEmpty() ||
                FROM_PHONE == null || FROM_PHONE.isEmpty()) {
            System.err.println("❌ Impossible d'envoyer le SMS, Twilio n'est pas configuré.");
            return;
        }

        // Vérifie et ajoute l'indicatif international si nécessaire
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+216" + phoneNumber;  // Change "+216" par ton code pays si nécessaire
        }

        try {
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(FROM_PHONE),
                    messageBody
            ).create();

            System.out.println("✅ SMS envoyé avec succès à " + phoneNumber + " : " + message.getSid());
        } catch (com.twilio.exception.ApiException e) {
            System.err.println("❌ Twilio API Erreur : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erreur inattendue lors de l'envoi du SMS : " + e.getMessage());
        }
    }
}
