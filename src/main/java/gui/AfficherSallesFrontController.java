package gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.SalleDeSport;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import services.SalleDeSportService;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherSallesFrontController {

    @FXML private FlowPane cardsContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button chatbotButton;
    @FXML private Button historiqueButton;
    @FXML private Button statistiqueButton;
    @FXML private Button triEquipementsButton;

    private double selectedLat = 36.8065; // Default to Tunisia center
    private double selectedLng = 10.1815;
    private final SalleDeSportService salleService = new SalleDeSportService();
    private static final String OPENAI_API_KEY = ""; // Remplacez par votre clé API OpenAI
    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiYXJpamNoYWFvdXJpIiwiYSI6ImNtN2FxaXQyejAxdTUycHNmeDQ0b2dvMzgifQ.KShSjG7KoUBYB6ikABiceA";
    @FXML
    public void initialize() {
        afficherSalles();
        searchButton.setOnAction(event -> rechercherSallesParEquipement());
        chatbotButton.setOnAction(event -> ouvrirChatbot());
        historiqueButton.setOnAction(event -> afficherHistorique());

        statistiqueButton.setOnAction(event -> afficherMeilleureSalleParEquipement());
        triEquipementsButton.setOnAction(event -> trierSallesParEquipements());

    }
    private void afficherMeilleureSalleParEquipement() {
        try {
            cardsContainer.getChildren().clear();
            List<SalleDeSport> salles = salleService.readAll();
            SalleDeSport meilleureSalle = null;
            int maxEquipements = 0;
            int totalEquipements = 0;

            for (SalleDeSport salle : salles) {
                int nombreEquipements = salleService.getNombreEquipements(salle.getId());
                totalEquipements += nombreEquipements;
                if (nombreEquipements > maxEquipements) {
                    maxEquipements = nombreEquipements;
                    meilleureSalle = salle;
                }
            }

            if (meilleureSalle != null) {
                Label titre = new Label("Meilleure Salle par Équipement");
                titre.setStyle("-fx-font-size: 20px; -fx-text-fill: #FF6600; -fx-font-weight: bold;");
                cardsContainer.getChildren().add(titre);

                VBox salleCard = createSalleCard(meilleureSalle);
                int nombreEquipements = salleService.getNombreEquipements(meilleureSalle.getId());
                double pourcentage = (double) nombreEquipements / totalEquipements * 100;

                Label equipementsLabel = new Label("Équipements : " + nombreEquipements);
                equipementsLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");

                Label pourcentageLabel = new Label(String.format("Pourcentage d'équipements : %.2f%%", pourcentage));
                pourcentageLabel.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 14px;");

                salleCard.getChildren().addAll(equipementsLabel, pourcentageLabel);
                cardsContainer.getChildren().add(salleCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void trierSallesParEquipements() {
        try {
            cardsContainer.getChildren().clear();
            List<SalleDeSport> salles = salleService.readAll();

            List<SalleDeSport> sallesTriees = salles.stream()
                    .sorted(Comparator.comparingInt(s -> {
                        try {
                            return -salleService.getNombreEquipements(s.getId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .collect(Collectors.toList());

            Label titre = new Label("Salles triées par nombre d'équipements");
            titre.setStyle("-fx-font-size: 20px; -fx-text-fill: #FF6600; -fx-font-weight: bold;");
            cardsContainer.getChildren().add(titre);

            for (SalleDeSport salle : sallesTriees) {
                VBox salleCard = createSalleCard(salle);
                int nombreEquipements = salleService.getNombreEquipements(salle.getId());
                Label equipementsLabel = new Label("Équipements : " + nombreEquipements);
                equipementsLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");
                salleCard.getChildren().add(equipementsLabel);

                cardsContainer.getChildren().add(salleCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        // Création de la carte visuelle de la salle
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: #262626; -fx-border-color: #FF6600; -fx-border-radius: 20; "
                + "-fx-background-radius: 20; -fx-padding: 20; -fx-spacing: 15; -fx-alignment: center; -fx-cursor: hand;");
        card.setPrefWidth(350);
        card.setPrefHeight(400);

        // Effet d'ombre
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(10);
        card.setEffect(shadow);

        // Effets hover
        card.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            card.setStyle("-fx-background-color: #333333; -fx-border-color: #FF6600; -fx-border-radius: 20; "
                    + "-fx-background-radius: 20; -fx-padding: 20; -fx-spacing: 15; -fx-alignment: center; -fx-cursor: hand;");
        });
        card.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            card.setStyle("-fx-background-color: #262626; -fx-border-color: #FF6600; -fx-border-radius: 20; "
                    + "-fx-background-radius: 20; -fx-padding: 20; -fx-spacing: 15; -fx-alignment: center; -fx-cursor: hand;");
        });

        // Nom et zone de la salle
        Label nomLabel = new Label("Salle: " + salle.getNom());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Label zoneLabel = new Label("Zone: " + salle.getZone());
        zoneLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 16px;");

        // Image de la salle
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        try {
            String imagePath = salle.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                } else {
                    String resourcePath = "/images/" + new File(imagePath).getName();
                    InputStream is = getClass().getResourceAsStream(resourcePath);
                    if (is != null) {
                        Image image = new Image(is);
                        imageView.setImage(image);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur de chargement de l'image: " + e.getMessage());
        }

        // Bouton "Voir Équipements"
        Button voirEquipementsButton = new Button("Voir Équipements");
        voirEquipementsButton.setStyle("-fx-background-color: #FF6600; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 10; "
                + "-fx-cursor: hand; -fx-padding: 10 20;");
        voirEquipementsButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            voirEquipementsButton.setStyle("-fx-background-color: #CC5200; -fx-text-fill: white; "
                    + "-fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 10; "
                    + "-fx-cursor: hand; -fx-padding: 10 20;");
        });
        voirEquipementsButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            voirEquipementsButton.setStyle("-fx-background-color: #FF6600; -fx-text-fill: white; "
                    + "-fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 10; "
                    + "-fx-cursor: hand; -fx-padding: 10 20;");
        });
        voirEquipementsButton.setOnAction(event -> voirEquipements(salle));

        // Nouveau bouton pour afficher la zone via Mapbox
        Button voirZoneMapboxButton = new Button("Voir Zone Mapbox");
        voirZoneMapboxButton.setStyle("-fx-background-color: #FF6600; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 10; "
                + "-fx-cursor: hand; -fx-padding: 10 20;");
        voirZoneMapboxButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            voirZoneMapboxButton.setStyle("-fx-background-color: #CC5200; -fx-text-fill: white; "
                    + "-fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 10; "
                    + "-fx-cursor: hand; -fx-padding: 10 20;");
        });
        voirZoneMapboxButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            voirZoneMapboxButton.setStyle("-fx-background-color: #FF6600; -fx-text-fill: white; "
                    + "-fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 10; "
                    + "-fx-cursor: hand; -fx-padding: 10 20;");
        });
        voirZoneMapboxButton.setOnAction(event -> afficherMapbox(salle));

        card.getChildren().addAll(imageView, nomLabel, zoneLabel, voirEquipementsButton, voirZoneMapboxButton);
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

    // Méthode pour afficher une carte Mapbox avec un marqueur et popup indiquant la zone et le nom de la salle
    private void afficherMapbox(SalleDeSport salle) {
        Stage mapStage = new Stage();
        mapStage.setTitle("Zone de la salle : " + salle.getNom());
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        String zone = salle.getZone();
        String[] parts = zone.split(",");
        String html;
        if (parts.length == 2) {
            String latitude = parts[0].trim();
            String longitude = parts[1].trim();
            html =  "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "  <meta charset='utf-8' />\n" +
                    "  <title>Mapbox Map</title>\n" +
                    "  <meta name='viewport' content='width=device-width, initial-scale=1' />\n" +
                    "  <script src='https://api.mapbox.com/mapbox-gl-js/v2.9.1/mapbox-gl.js'></script>\n" +
                    "  <link href='https://api.mapbox.com/mapbox-gl-js/v2.9.1/mapbox-gl.css' rel='stylesheet' />\n" +
                    "  <style>\n" +
                    "    html, body { height: 100%; margin: 0; padding: 0; }\n" +
                    "    #map { height: 100%; width: 100%; }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div id='map'></div>\n" +
                    "  <script>\n" +
                    "    mapboxgl.accessToken = '" + MAPBOX_ACCESS_TOKEN + "';\n" +
                    "    var map = new mapboxgl.Map({\n" +
                    "      container: 'map',\n" +
                    "      style: 'mapbox://styles/mapbox/streets-v11',\n" +
                    "      center: [" + longitude + ", " + latitude + "],\n" +
                    "      zoom: 12\n" +
                    "    });\n" +
                    "  </script>\n" +
                    "</body>\n" +
                    "</html>";
        } else {
            html = "<html><body><h3>Zone non valide pour Mapbox</h3></body></html>";
        }
        engine.loadContent(html);
        Scene scene = new Scene(webView, 800, 600);
        mapStage.setScene(scene);
        mapStage.show();
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
            Path audioFilePath = Files.createTempFile("recorded_audio", ".wav");
            recordAudio(audioFilePath.toFile(), 5);
            byte[] audioData = Files.readAllBytes(audioFilePath);
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
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
            AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                throw new LineUnavailableException("Ligne audio non supportée !");
            }
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            System.out.println("🎤 Enregistrement en cours... (" + durationSeconds + " secondes)");
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
            recordingThread.join();
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
