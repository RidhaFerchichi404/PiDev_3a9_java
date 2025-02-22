package gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.SalleDeSport;
import org.example.service.SalleDeSportService;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public class AfficherSallesFrontController {

    @FXML private FlowPane cardsContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button chatbotButton;
    @FXML private Button historiqueButton;


    private final SalleDeSportService salleService = new SalleDeSportService();
    
    @FXML
    public void initialize() {
        afficherSalles();
        searchButton.setOnAction(event -> rechercherSallesParEquipement());
        chatbotButton.setOnAction(event -> ouvrirChatbot());
        historiqueButton.setOnAction(event -> afficherHistorique());
    }

    private void afficherSalles() {
        try {
            cardsContainer.getChildren().clear();
            List<SalleDeSport> salles = salleService.readAll();

            if (salles.isEmpty()) {
                Label noSalleLabel = new Label("Aucune salle disponible");
                noSalleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff4444;");
                cardsContainer.getChildren().add(noSalleLabel);
            } else {
                for (SalleDeSport salle : salles) {
                    cardsContainer.getChildren().add(createSalleCard(salle));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rechercherSallesParEquipement() {
        String equipementRecherche = searchField.getText().trim();
        if (equipementRecherche.isEmpty()) {
            showAlert("Champ vide", "Veuillez entrer un équipement.", Alert.AlertType.WARNING);
            return;
        }

        try {
            cardsContainer.getChildren().clear();
            List<SalleDeSport> salles = salleService.getSallesByEquipement(equipementRecherche);

            if (salles.isEmpty()) {
                Label noSalleLabel = new Label("Aucune salle ne possède cet équipement.");
                noSalleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff4444;");
                cardsContainer.getChildren().add(noSalleLabel);
            } else {
                for (SalleDeSport salle : salles) {
                    cardsContainer.getChildren().add(createSalleCard(salle));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createSalleCard(SalleDeSport salle) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #262626; -fx-border-color: #ff8c00; "
                + "-fx-border-radius: 15; -fx-background-radius: 15; -fx-padding: 15; "
                + "-fx-spacing: 10; -fx-alignment: center;");
        card.setPrefWidth(350);
        card.setPrefHeight(380);

        Label nomLabel = new Label("Salle: " + salle.getNom());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label zoneLabel = new Label("Zone: " + salle.getZone());
        zoneLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 16px;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);

        try {
            String imagePath = salle.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString()));
                } else {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
                }
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
        }

        Button voirEquipementsButton = new Button("Voir Équipements");
        voirEquipementsButton.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 16px;");
        voirEquipementsButton.setOnAction(event -> voirEquipements(salle));




        card.getChildren().addAll(imageView, nomLabel, zoneLabel, voirEquipementsButton);
        return card;
    }

    private void voirEquipements(SalleDeSport salle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEquipementsFront.fxml"));
            Parent root = loader.load();

            AfficherEquipementsFrontController controller = loader.getController();
            controller.setIdSalle(salle.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Équipements - " + salle.getNom());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'afficher les équipements", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private void ouvrirChatbot() {
        Stage chatStage = new Stage();
        chatStage.setTitle("💬 Chatbot - Assistance");

        VBox chatBox = new VBox(10);
        chatBox.setStyle("-fx-padding: 20; -fx-background-color: #121212;");

        TextArea chatHistory = new TextArea();
        chatHistory.setEditable(false);
        chatHistory.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: white; -fx-font-size: 14px;");

        TextField userInput = new TextField();
        userInput.setPromptText("Posez une question...");
        userInput.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px;");

        Button sendButton = new Button("Envoyer");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        Button speechToTextButton = new Button("🎤 Parler");
        speechToTextButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 14px;");


        sendButton.setOnAction(event -> {
            String question = userInput.getText().trim();
            if (!question.isEmpty()) {
                chatHistory.appendText("\n👤 Vous : " + question);
                String response = appelerChatbot(question);
                chatHistory.appendText("\n🤖 Chatbot : " + response);
                enregistrerHistorique("Vous: " + question + "\nChatbot: " + response);
                userInput.clear();
            }
        });

        speechToTextButton.setOnAction(event -> {
            String voiceText = speechToText();
            if (!voiceText.isEmpty()) {
                userInput.setText(voiceText);
            }
        });

        VBox inputBox = new VBox(5, userInput, sendButton, speechToTextButton);
        chatBox.getChildren().addAll(chatHistory, inputBox);

        Scene scene = new Scene(chatBox, 500, 400);
        chatStage.setScene(scene);
        chatStage.show();
    }

    private String appelerChatbot(String question) {
        try {
            String apiUrl = "https://api.openai.com/v1/chat/completions";
            String requestBody = "{" +
                    "\"model\": \"gpt-3.5-turbo\"," +
                    "\"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}]," +
                    "\"max_tokens\": 150" +
                    "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Désolé, une erreur est survenue.";
        }
    }

    private String speechToText() {
        try {
            // Créer un fichier temporaire pour l'audio
            Path audioFilePath = Files.createTempFile("recorded_audio", ".wav");
            recordAudio(audioFilePath.toFile(), 5);  // Enregistre 5 secondes d'audio

            // Lire le fichier audio
            byte[] audioData = Files.readAllBytes(audioFilePath);

            // Construire la requête HTTP avec multipart/form-data
            String boundary = "----Boundary" + System.currentTimeMillis();
            String apiUrl = "https://api.openai.com/v1/audio/transcriptions";

            String bodyStart = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"model\"\r\n\r\n" +
                    "whisper-1\r\n" +
                    "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"file\"; filename=\"audio.wav\"\r\n" +
                    "Content-Type: audio/wav\r\n\r\n";

            String bodyEnd = "\r\n--" + boundary + "--\r\n";

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(bodyStart.getBytes());
            byteArrayOutputStream.write(audioData);
            byteArrayOutputStream.write(bodyEnd.getBytes());
            byte[] requestBody = byteArrayOutputStream.toByteArray();

            // Envoyer la requête à OpenAI
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Vérifier si la réponse est valide
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            if (!jsonResponse.has("text")) {
                System.err.println("❌ Erreur API OpenAI : " + response.body());
                return "Erreur : OpenAI n'a pas retourné de texte.";
            }

            return jsonResponse.get("text").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Échec de la reconnaissance vocale.";
        }
    }




    private void recordAudio(File file, int durationSeconds) {
        try {
            // 🔹 Configuration du format audio
            AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                throw new LineUnavailableException("Ligne audio non supportée !");
            }

            // 🔹 Ouvrir la ligne de capture audio
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            System.out.println("🎤 Enregistrement en cours... (" + durationSeconds + " secondes)");

            // 🔹 Enregistrement en arrière-plan
            Thread recordingThread = new Thread(() -> {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                     AudioInputStream audioStream = new AudioInputStream(line)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    long endTime = System.currentTimeMillis() + (durationSeconds * 1000);

                    while (System.currentTimeMillis() < endTime) {
                        bytesRead = line.read(buffer, 0, buffer.length);
                        out.write(buffer, 0, bytesRead);
                    }

                    // 🔹 Sauvegarder en .wav
                    try (FileOutputStream fos = new FileOutputStream(file);
                         BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                        AudioSystem.write(new AudioInputStream(
                                        new ByteArrayInputStream(out.toByteArray()), format,
                                        out.toByteArray().length / format.getFrameSize()),
                                AudioFileFormat.Type.WAVE, bos);
                    }

                    System.out.println("✅ Enregistrement terminé : " + file.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            recordingThread.start();
            recordingThread.join(); // Attend la fin de l'enregistrement

            // 🔹 Arrêter l'enregistrement
            line.stop();
            line.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de l'enregistrement audio !");
        }
    }

    private void enregistrerHistorique(String message) {
        try (FileWriter writer = new FileWriter("historique_chatbot.txt", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void afficherHistorique() {
        Stage historiqueStage = new Stage();
        historiqueStage.setTitle("📜 Historique du Chatbot");

        VBox historiqueBox = new VBox(10);
        historiqueBox.setStyle("-fx-padding: 20; -fx-background-color: #121212;");

        TextArea historiqueTextArea = new TextArea();
        historiqueTextArea.setEditable(false);
        historiqueTextArea.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: white; -fx-font-size: 14px;");

        // 🔹 Lire le fichier historique
        try (BufferedReader reader = new BufferedReader(new FileReader("historique_chatbot.txt"))) {
            StringBuilder contenu = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                contenu.append(ligne).append("\n");
            }
            historiqueTextArea.setText(contenu.toString());
        } catch (IOException e) {
            historiqueTextArea.setText("⚠️ Aucun historique trouvé !");
        }

        Button fermerButton = new Button("❌ Fermer");
        fermerButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        fermerButton.setOnAction(event -> historiqueStage.close());

        historiqueBox.getChildren().addAll(historiqueTextArea, fermerButton);

        Scene scene = new Scene(historiqueBox, 500, 400);
        historiqueStage.setScene(scene);
        historiqueStage.show();
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
