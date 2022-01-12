package gui.controller;

import be.Movie;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MoviePlayController {

    @FXML
    private MediaView mediaView;
    private String media_URL = "Movies/";
    private Media media;
    private MediaPlayer mediaPlayer;

    private Movie movie;
    public MoviePlayController(){
        media = new Media(media_URL + setMovieUrl());
    }

    public String setMovieUrl(){
        return movie.getFileLink();
    }
}
