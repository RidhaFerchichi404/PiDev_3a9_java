package utils;

public class LocalCaptcha {
    private String captchaText; // Stores the generated CAPTCHA text

    // Generate a random CAPTCHA text
    private String generateCaptchaText() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            captcha.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return captcha.toString();
    }

    // Generate a new CAPTCHA text
    public String getNewCaptchaText() {
        captchaText = generateCaptchaText();
        return captchaText;
    }

    // Validate the user's input against the CAPTCHA text
    public boolean validateCaptcha(String userInput) {
        return userInput.equalsIgnoreCase(captchaText);
    }
}