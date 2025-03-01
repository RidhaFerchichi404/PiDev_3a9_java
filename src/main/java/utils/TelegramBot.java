package utils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;
    private final String chatId;

    public TelegramBot(String botToken, String botUsername, String chatId) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.chatId = chatId;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("You said: " + messageText);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendImage() {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Check out this image: " + "https://static.vecteezy.com/system/resources/thumbnails/002/367/440/small/new-post-neon-signs-style-text-free-vector.jpg");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
