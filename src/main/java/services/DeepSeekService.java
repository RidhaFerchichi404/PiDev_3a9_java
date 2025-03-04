package services;

import entities.Produit;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class DeepSeekService {
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    
    private final HttpClient httpClient;
    private final Gson gson;

    public DeepSeekService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public String analyzeProduct(Produit produit, int age, double weight, double height) throws Exception {
        // Calculate BMI
        double bmi = weight / ((height/100) * (height/100));
        
        // Prepare the system message
        String systemPrompt = "Tu es Alex, un coach sportif sympathique et dynamique avec 10 ans d'expérience. " +
                            "Tu as l'habitude de parler aux clients de manière décontractée et amicale, comme à des amis. " +
                            "Tu réponds toujours aux questions de manière conversationnelle, en utilisant 'tu' et en donnant des exemples concrets. " +
                            "Tu aimes utiliser des émojis et des expressions familières pour rendre la conversation plus naturelle. " +
                            "Quand tu donnes des conseils sur les produits, tu partages aussi ta propre expérience.";
        
        // Prepare the user message with all relevant information and predefined questions
        String userMessage = String.format(
            "Salut Alex ! J'ai un nouveau client qui s'intéresse à %s. Voici son profil :\n\n" +
            "🏋️ Infos produit :\n" +
            "- Nom : %s\n" +
            "- Catégorie : %s\n" +
            "- Description : %s\n\n" +
            "👤 Profil client :\n" +
            "- Âge : %d ans\n" +
            "- Poids : %.1f kg\n" +
            "- Taille : %.1f cm\n" +
            "- IMC : %.1f\n\n" +
            "Le client a quelques questions, peux-tu y répondre ?\n\n" +
            "1. \"Est-ce que ce produit me convient ? Je débute...\"\n" +
            "2. \"Comment je dois l'utiliser pour avoir les meilleurs résultats ?\"\n" +
            "3. \"Y a-t-il des risques ou des précautions à prendre ?\"\n" +
            "4. \"Quels résultats je peux espérer ?\"\n" +
            "5. \"As-tu des astuces personnelles à partager ?\"\n\n" +
            "Réponds comme si tu parlais directement au client, avec ton style décontracté habituel ! 😊",
            produit.getNom(),
            produit.getNom(),
            produit.getCategorie(),
            produit.getDescription(),
            age,
            weight,
            height,
            bmi
        );

        // Prepare the request body for OpenAI
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");
        requestBody.addProperty("temperature", 0.9); // Higher temperature for more creative, conversational responses
        requestBody.addProperty("max_tokens", 800); // Slightly increased for natural conversation
        requestBody.addProperty("presence_penalty", 0.6); // Encourage more diverse responses
        requestBody.addProperty("frequency_penalty", 0.4); // Reduce repetition
        
        JsonArray messages = new JsonArray();
        
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", systemPrompt);
        messages.add(systemMessage);
        
        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", userMessage);
        messages.add(userMsg);
        
        requestBody.add("messages", messages);

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        // Send request and get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Check if request was successful
        if (response.statusCode() != 200) {
            System.err.println("API Error: " + response.body());
            return simulateAnalysis(produit, age, weight, height);
        }

        // Parse the response
        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        try {
            String analysisResult = jsonResponse
                .getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("message")
                .get("content")
                .getAsString();
            return analysisResult;
        } catch (Exception e) {
            System.err.println("Error parsing API response: " + e.getMessage());
            return simulateAnalysis(produit, age, weight, height);
        }
    }

    // Temporary method to simulate response in case of API failure
    private String simulateAnalysis(Produit produit, int age, double weight, double height) {
        double bmi = weight / ((height/100) * (height/100));
        StringBuilder analysis = new StringBuilder();

        analysis.append("Analyse Personnalisée pour ").append(produit.getNom()).append("\n\n");

        // Product-specific analysis
        analysis.append("Caractéristiques du Produit:\n");
        analysis.append("• Type: ").append(produit.getCategorie()).append("\n");
        analysis.append("• Description: ").append(produit.getDescription()).append("\n\n");

        // User profile analysis
        analysis.append("Profil Utilisateur:\n");
        analysis.append("• Âge: ").append(age).append(" ans\n");
        analysis.append("• IMC: ").append(String.format("%.1f", bmi)).append("\n\n");

        // Compatibility and recommendations
        analysis.append("Analyse de Compatibilité:\n");
        if (produit.getCategorie().toLowerCase().contains("supplement")) {
            analysis.append("• Recommandation basée sur l'âge et l'IMC\n");
            analysis.append("• Dosage recommandé en fonction du poids\n");
            analysis.append("• Précautions particulières\n\n");
        } else if (produit.getCategorie().toLowerCase().contains("equipment")) {
            analysis.append("• Adaptation à la morphologie\n");
            analysis.append("• Recommandations d'utilisation\n");
            analysis.append("• Niveau d'expertise requis\n\n");
        }

        // Benefits and considerations
        analysis.append("Bénéfices Potentiels:\n");
        analysis.append("• Avantages spécifiques pour votre profil\n");
        analysis.append("• Points d'attention particuliers\n");
        analysis.append("• Suggestions d'utilisation optimale\n\n");

        return analysis.toString();
    }
} 