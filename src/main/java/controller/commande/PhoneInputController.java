package controller.commande;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import java.util.*;

public class PhoneInputController {
    @FXML private ComboBox<String> countryCodeCombo;
    @FXML private TextField phoneNumberField;
    
    private Map<String, String> countryCodes;
    
    @FXML
    public void initialize() {
        setupCountryCodes();
        setupValidation();
    }
    
    private void setupCountryCodes() {
        countryCodes = new LinkedHashMap<>(); // Keep insertion order
        
        // Add Tunisia first (default)
        countryCodes.put("Tunisie (+216)", "216");
        
        // Add other North African countries
        countryCodes.put("Algérie (+213)", "213");
        countryCodes.put("Maroc (+212)", "212");
        countryCodes.put("Libye (+218)", "218");
        countryCodes.put("Égypte (+20)", "20");
        
        // Add some European countries
        countryCodes.put("France (+33)", "33");
        countryCodes.put("Italie (+39)", "39");
        countryCodes.put("Espagne (+34)", "34");
        countryCodes.put("Allemagne (+49)", "49");
        
        // Set items and default value
        countryCodeCombo.setItems(FXCollections.observableArrayList(countryCodes.keySet()));
        countryCodeCombo.setValue("Tunisie (+216)"); // Set Tunisia as default
    }
    
    private void setupValidation() {
        // Only allow numbers in the phone field
        phoneNumberField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                phoneNumberField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            
            // Limit length based on country code
            String countryCode = getSelectedCountryCode();
            int maxLength = "216".equals(countryCode) ? 8 : 15; // 8 digits for Tunisia, 15 for others
            
            if (newVal.length() > maxLength) {
                phoneNumberField.setText(oldVal);
            }
        });
    }
    
    private String getSelectedCountryCode() {
        String selected = countryCodeCombo.getValue();
        return countryCodes.get(selected);
    }
    
    public String getFullPhoneNumber() {
        String countryCode = getSelectedCountryCode();
        String number = phoneNumberField.getText().trim();
        return countryCode + number;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return;
        }
        
        // Try to match country code
        String foundCountryCode = null;
        String numberPart = phoneNumber;
        
        for (String code : countryCodes.values()) {
            if (phoneNumber.startsWith(code)) {
                foundCountryCode = code;
                numberPart = phoneNumber.substring(code.length());
                break;
            }
        }
        
        // Set country code if found, otherwise default to Tunisia
        if (foundCountryCode != null) {
            for (Map.Entry<String, String> entry : countryCodes.entrySet()) {
                if (entry.getValue().equals(foundCountryCode)) {
                    countryCodeCombo.setValue(entry.getKey());
                    break;
                }
            }
        }
        
        // Set the number part
        phoneNumberField.setText(numberPart);
    }
    
    public boolean isValid() {
        String number = phoneNumberField.getText().trim();
        String countryCode = getSelectedCountryCode();
        
        if (number.isEmpty()) {
            return false;
        }
        
        // Specific validation for Tunisia
        if ("216".equals(countryCode)) {
            return number.length() == 8;
        }
        
        // General validation for other countries
        return number.length() >= 4 && number.length() <= 15;
    }
} 