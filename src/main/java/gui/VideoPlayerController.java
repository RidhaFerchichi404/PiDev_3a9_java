package gui;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;

public class VideoPlayerController {

    @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;

    public void setVideoUrl(String videoUrl) {
        Media media = new Media(videoUrl);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Répéter la vidéo en boucle
    }

    @FXML
    private void handlePlayButtonClick() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    private void handlePauseButtonClick() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void handleStopButtonClick() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}