package gui.controller;

import gui.Model.MoviePlayModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MoviePlayController implements Initializable {
    @FXML
    private Button closeButton;
    @FXML
    private MediaView mediaView;
    private Media media;
    private MediaPlayer mediaPlayer;
    private File file;
    private MoviePlayModel moviePlayModel;
    private String moviePath = "Movies/";
    public MoviePlayController(){
        moviePlayModel = new MoviePlayModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        file = new File(MoviePlayModel.movieUrl);
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }

    public void playMovie(ActionEvent actionEvent) {
        mediaPlayer.play();

    }

    public void pauseMovie(ActionEvent actionEvent) {
        mediaPlayer.pause();
    }

    public void stopMovie(){
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }

    public void closeMovieView(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stopMovie();
        moviePlayModel.setMovieUrl(moviePath);
        stage.close();
    }
}
