package controller.commande;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.stage.Stage;
import java.util.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javafx.concurrent.Worker.State;
import netscape.javascript.JSObject;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.win32.*;

public class AddressInputController {
    @FXML private ComboBox<String> governorateCombo;
    @FXML private ComboBox<String> cityCombo;
    @FXML private TextField streetField;
    @FXML private TextField buildingField;
    @FXML private TextField postalCodeField;
    @FXML private TextArea previewArea;
    @FXML private WebView mapView;
    @FXML private TextField searchField;
    
    private Map<String, List<String>> citiesByGovernorate;
    private Runnable onAddressValidated;
    private double selectedLat = 36.8065; // Default to Tunisia center
    private double selectedLng = 10.1815;
    
    @FXML
    public void initialize() {
        try {
            System.out.println("Initializing AddressInputController...");
            setupGovernoratesAndCities();
            setupListeners();
            initializeMap();
            updatePreview();
            System.out.println("AddressInputController initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing AddressInputController: " + e.getMessage());
            e.printStackTrace();
            showError("Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }
    
    private void initializeMap() {
        try {
            System.out.println("Initializing map...");
            // Load OpenLayers map
            String mapContent = getMapHtml();
            System.out.println("Map HTML content loaded");
            
            mapView.getEngine().getLoadWorker().stateProperty().addListener((obs, old, newState) -> {
                System.out.println("WebView state changed to: " + newState);
                if (newState == State.SUCCEEDED) {
                    try {
                        // Get the JavaScript window object
                        JSObject window = (JSObject) mapView.getEngine().executeScript("window");
                        
                        // Add this controller as a JavaScript member
                        window.setMember("controller", this);
                        
                        // Initialize the map centered on Tunisia
                        mapView.getEngine().executeScript("initializeMap(" + selectedLat + ", " + selectedLng + ")");
                        System.out.println("Map initialized successfully");
                    } catch (Exception e) {
                        System.err.println("Error initializing map JavaScript: " + e.getMessage());
                        e.printStackTrace();
                        showError("Erreur", "Erreur lors de l'initialisation de la carte: " + e.getMessage());
                    }
                } else if (newState == State.FAILED) {
                    System.err.println("WebView loading failed");
                    showError("Erreur", "Impossible de charger la carte");
                }
            });
            
            mapView.getEngine().loadContent(mapContent);
        } catch (Exception e) {
            System.err.println("Error in initializeMap: " + e.getMessage());
            e.printStackTrace();
            showError("Erreur", "Erreur lors de l'initialisation de la carte: " + e.getMessage());
        }
    }
    
    // Called from JavaScript when a location is selected on the map
    public void updateLocation(double lat, double lng) {
        selectedLat = lat;
        selectedLng = lng;
        
        // Reverse geocode the coordinates to get address details
        reverseGeocode(lat, lng);
    }
    
    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;
        
        // Geocode the search query
        try {
            String encodedQuery = URLEncoder.encode(query + ", Tunisia", StandardCharsets.UTF_8);
            String url = "https://nominatim.openstreetmap.org/search?q=" + encodedQuery + "&format=json";
            
            // Make HTTP request and parse JSON response
            // For brevity, using a simple approach. In production, use proper HTTP client
            mapView.getEngine().executeScript(
                "fetch('" + url + "')" +
                ".then(response => response.json())" +
                ".then(data => {" +
                "    if (data && data.length > 0) {" +
                "        const location = data[0];" +
                "        map.getView().setCenter(ol.proj.fromLonLat([parseFloat(location.lon), parseFloat(location.lat)]));" +
                "        map.getView().setZoom(16);" +
                "        updateMarker([parseFloat(location.lon), parseFloat(location.lat)]);" +
                "        controller.updateLocation(parseFloat(location.lat), parseFloat(location.lon));" +
                "    }" +
                "})"
            );
        } catch (Exception e) {
            showError("Erreur", "Impossible de rechercher l'adresse");
        }
    }
    
    private void reverseGeocode(double lat, double lng) {
        try {
            String url = "https://nominatim.openstreetmap.org/reverse?lat=" + lat + "&lon=" + lng + "&format=json";
            
            // Make HTTP request and parse JSON response
            mapView.getEngine().executeScript(
                "fetch('" + url + "')" +
                ".then(response => response.json())" +
                ".then(data => {" +
                "    if (data && data.address) {" +
                "        controller.updateAddressFields(JSON.stringify(data.address));" +
                "    }" +
                "})"
            );
        } catch (Exception e) {
            showError("Erreur", "Impossible de récupérer les détails de l'adresse");
        }
    }
    
    // Called from JavaScript with address details
    public void updateAddressFields(String addressJson) {
        try {
            // Parse the JSON and update fields
            // This is a simplified version. In production, use proper JSON parsing
            if (addressJson.contains("city")) {
                String city = addressJson.split("city\":\"")[1].split("\"")[0];
                cityCombo.setValue(city);
            }
            if (addressJson.contains("road")) {
                String street = addressJson.split("road\":\"")[1].split("\"")[0];
                streetField.setText(street);
            }
            if (addressJson.contains("postcode")) {
                String postcode = addressJson.split("postcode\":\"")[1].split("\"")[0];
                postalCodeField.setText(postcode);
            }
            updatePreview();
        } catch (Exception e) {
            // Handle parsing errors
        }
    }
    
    @FXML
    private void handleMyLocation() {
        // Create loading indicator
        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(50, 50);
        
        // Create overlay with semi-transparent background
        StackPane overlay = new StackPane(progress);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        
        // Get the VBox that contains the map (root container from FXML)
        VBox root = (VBox) mapView.getParent().getParent();
        
        // Add overlay as a new child to the root VBox
        root.getChildren().add(overlay);

        // Use browser's geolocation API through JavaScript
        try {
            mapView.getEngine().executeScript("""
                if ('geolocation' in navigator) {
                    navigator.geolocation.getCurrentPosition(
                        function(position) {
                            var lat = position.coords.latitude;
                            var lng = position.coords.longitude;
                            
                            map.getView().animate({
                                center: ol.proj.fromLonLat([lng, lat]),
                                zoom: 18,
                                duration: 1000
                            });
                            updateMarker([lng, lat]);
                            controller.updateLocation(lat, lng);
                            showGeolocateError('Position GPS trouvée!', 'success');
                            setTimeout(hideGeolocateError, 3000);
                        },
                        function(error) {
                            var message;
                            switch(error.code) {
                                case error.PERMISSION_DENIED:
                                    message = 'Accès à la localisation refusé';
                                    break;
                                case error.POSITION_UNAVAILABLE:
                                    message = 'Position non disponible';
                                    break;
                                case error.TIMEOUT:
                                    message = 'Délai d\\'attente dépassé';
                                    break;
                                default:
                                    message = 'Erreur inconnue';
                            }
                            showGeolocateError(message, 'error');
                            setTimeout(hideGeolocateError, 3000);
                        },
                        {
                            enableHighAccuracy: true,
                            timeout: 5000,
                            maximumAge: 0
                        }
                    );
                } else {
                    showGeolocateError('Géolocalisation non supportée', 'error');
                    setTimeout(hideGeolocateError, 3000);
                }
            """);
        } catch (Exception e) {
            showError("Erreur", "Impossible d'accéder au service de géolocalisation");
        } finally {
            // Remove overlay after a short delay
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        root.getChildren().remove(overlay);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    // Windows Location API interface
    private interface LocationApi extends StdCallLibrary {
        int RequestLocationPermission();
        int GetLocation(LocationReport report);
    }

    // Location report structure
    private static class LocationReport extends Structure {
        public double latitude;
        public double longitude;
        public double accuracy;
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("latitude", "longitude", "accuracy");
        }
    }
    
    private String getMapHtml() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta http-equiv="Content-Security-Policy" content="default-src * 'unsafe-inline' 'unsafe-eval'; script-src * 'unsafe-inline' 'unsafe-eval'; connect-src * 'unsafe-inline'; img-src * data: blob: 'unsafe-inline'; frame-src *; style-src * 'unsafe-inline';">
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.9.0/css/ol.css">
                <script src="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.9.0/build/ol.js"></script>
                <style>
                    html, body, #map {
                        width: 100%;
                        height: 100%;
                        margin: 0;
                        padding: 0;
                    }
                    .geolocate-error {
                        position: absolute;
                        bottom: 20px;
                        left: 50%;
                        transform: translateX(-50%);
                        background: rgba(0,0,0,0.8);
                        color: white;
                        padding: 10px 20px;
                        border-radius: 4px;
                        display: none;
                        z-index: 1000;
                        text-align: center;
                        min-width: 200px;
                        max-width: 80%;
                    }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <div id="geolocate-error" class="geolocate-error"></div>
                <script>
                    var map, marker;
                    
                    function initializeMap(lat, lng) {
                        marker = new ol.Feature({
                            geometry: new ol.geom.Point(ol.proj.fromLonLat([lng, lat]))
                        });
                        
                        var vectorSource = new ol.source.Vector({
                            features: [marker]
                        });
                        
                        var vectorLayer = new ol.layer.Vector({
                            source: vectorSource,
                            style: new ol.style.Style({
                                image: new ol.style.Circle({
                                    radius: 8,
                                    fill: new ol.style.Fill({color: '#ff0000'})
                                })
                            })
                        });
                        
                        map = new ol.Map({
                            target: 'map',
                            layers: [
                                new ol.layer.Tile({
                                    source: new ol.source.OSM()
                                }),
                                vectorLayer
                            ],
                            view: new ol.View({
                                center: ol.proj.fromLonLat([lng, lat]),
                                zoom: 13
                            })
                        });
                        
                        map.on('click', function(evt) {
                            var coords = ol.proj.transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326');
                            updateMarker(coords);
                            controller.updateLocation(coords[1], coords[0]);
                        });
                    }
                    
                    function updateMarker(coords) {
                        marker.getGeometry().setCoordinates(ol.proj.fromLonLat(coords));
                    }
                    
                    function showGeolocateError(message, type = 'error') {
                        const errorDiv = document.getElementById('geolocate-error');
                        errorDiv.textContent = message;
                        errorDiv.style.display = 'block';
                        
                        switch(type) {
                            case 'success':
                                errorDiv.style.background = 'rgba(40,167,69,0.9)';
                                break;
                            case 'info':
                                errorDiv.style.background = 'rgba(0,123,255,0.9)';
                                break;
                            default:
                                errorDiv.style.background = 'rgba(220,53,69,0.9)';
                        }
                    }
                    
                    function hideGeolocateError() {
                        document.getElementById('geolocate-error').style.display = 'none';
                    }
                </script>
            </body>
            </html>
        """;
    }
    
    private void setupGovernoratesAndCities() {
        citiesByGovernorate = new HashMap<>();
        
        // Initialize with Tunisia's governorates and major cities
        citiesByGovernorate.put("Tunis", Arrays.asList("Tunis", "Le Bardo", "La Marsa", "Carthage"));
        citiesByGovernorate.put("Ariana", Arrays.asList("Ariana", "La Soukra", "Raoued", "Sidi Thabet"));
        citiesByGovernorate.put("Ben Arous", Arrays.asList("Ben Arous", "El Mourouj", "Radès", "Mégrine"));
        citiesByGovernorate.put("Manouba", Arrays.asList("Manouba", "Den Den", "Oued Ellil", "Tebourba"));
        citiesByGovernorate.put("Nabeul", Arrays.asList("Nabeul", "Hammamet", "Dar Chaabane", "Korba"));
        citiesByGovernorate.put("Zaghouan", Arrays.asList("Zaghouan", "Zriba", "Bir Mcherga", "El Fahs"));
        citiesByGovernorate.put("Bizerte", Arrays.asList("Bizerte", "Menzel Bourguiba", "Mateur", "Ras Jebel"));
        citiesByGovernorate.put("Béja", Arrays.asList("Béja", "Medjez el-Bab", "Téboursouk", "Testour"));
        citiesByGovernorate.put("Jendouba", Arrays.asList("Jendouba", "Bou Salem", "Tabarka", "Aïn Draham"));
        citiesByGovernorate.put("Kef", Arrays.asList("Le Kef", "Dahmani", "Sers", "Tajerouine"));
        
        governorateCombo.setItems(FXCollections.observableArrayList(citiesByGovernorate.keySet()));
        
        // When a governorate is selected, update the cities
        governorateCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cityCombo.setItems(FXCollections.observableArrayList(citiesByGovernorate.get(newVal)));
                cityCombo.setDisable(false);
            } else {
                cityCombo.setItems(null);
                cityCombo.setDisable(true);
            }
            updatePreview();
        });
    }
    
    private void setupListeners() {
        // Add listeners to all fields to update preview
        cityCombo.valueProperty().addListener((obs, old, newVal) -> updatePreview());
        streetField.textProperty().addListener((obs, old, newVal) -> updatePreview());
        buildingField.textProperty().addListener((obs, old, newVal) -> updatePreview());
        postalCodeField.textProperty().addListener((obs, old, newVal) -> updatePreview());
        
        // Add validation for postal code (numbers only)
        postalCodeField.textProperty().addListener((obs, old, newVal) -> {
            if (!newVal.matches("\\d*")) {
                postalCodeField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }
    
    private void updatePreview() {
        StringBuilder address = new StringBuilder();
        
        if (buildingField.getText() != null && !buildingField.getText().trim().isEmpty()) {
            address.append(buildingField.getText().trim()).append(", ");
        }
        
        if (streetField.getText() != null && !streetField.getText().trim().isEmpty()) {
            address.append(streetField.getText().trim()).append(", ");
        }
        
        if (cityCombo.getValue() != null) {
            address.append(cityCombo.getValue());
            
            if (postalCodeField.getText() != null && !postalCodeField.getText().trim().isEmpty()) {
                address.append(" ").append(postalCodeField.getText().trim());
            }
            
            address.append(", ");
        }
        
        if (governorateCombo.getValue() != null) {
            address.append(governorateCombo.getValue());
        }
        
        previewArea.setText(address.toString());
    }
    
    @FXML
    private void handleValidate() {
        if (validateFields()) {
            if (onAddressValidated != null) {
                onAddressValidated.run();
            }
            ((Stage) governorateCombo.getScene().getWindow()).close();
        }
    }
    
    @FXML
    private void handleCancel() {
        ((Stage) mapView.getScene().getWindow()).close();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        
        // Style the error dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
        
        alert.showAndWait();
    }
    
    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();
        
        if (governorateCombo.getValue() == null) {
            errors.append("- Veuillez sélectionner un gouvernorat\n");
        }
        
        if (cityCombo.getValue() == null) {
            errors.append("- Veuillez sélectionner une ville\n");
        }
        
        if (streetField.getText().trim().isEmpty()) {
            errors.append("- Veuillez entrer une rue\n");
        }
        
        if (postalCodeField.getText().trim().isEmpty()) {
            errors.append("- Veuillez entrer un code postal\n");
        } else if (postalCodeField.getText().length() != 4) {
            errors.append("- Le code postal doit contenir 4 chiffres\n");
        }
        
        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText(null);
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    public void setOnAddressValidated(Runnable callback) {
        this.onAddressValidated = callback;
    }
    
    public String getFormattedAddress() {
        return previewArea.getText();
    }
    
    public double getSelectedLatitude() {
        return selectedLat;
    }
    
    public double getSelectedLongitude() {
        return selectedLng;
    }
} 