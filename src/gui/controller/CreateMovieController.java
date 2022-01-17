package gui.controller;

import be.Movie;
import gui.Model.MovieModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateMovieController implements Initializable {
    @FXML
    private TextField moviePersonalRatingTxt;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField movieTitleTxt;
    @FXML
    private TextField movieRatingTxt;
    @FXML
    private TextField movieFilePathTxt;
    private MovieModel movieModel;

    public CreateMovieController() throws IOException {
        movieModel = new MovieModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movieRatingTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}([\\.]\\d{0,1})?")) {
                movieRatingTxt.setText(oldValue);
            }
        });

        moviePersonalRatingTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}([\\.]\\d{0,1})?")) {
                moviePersonalRatingTxt.setText(oldValue);
            }
        });
    }

    /**
     *  saveMovieBtn opretter det nye movie objekt
     * @param actionEvent javaFX event klasse
     * @throws SQLException
     */
    public void saveMovieBtn(ActionEvent actionEvent) throws SQLException {
        String movieTitle="";
        String filePath="";
        float rating=-1;
        float personalRating=-1;
        int maxRating = 10;

        if (!movieTitleTxt.getText().isEmpty()){
            movieTitle = movieTitleTxt.getText();
        }
        else{
            errorWindow("Please name the movie");
        }

        if (!movieRatingTxt.getText().isEmpty() && Float.parseFloat(movieRatingTxt.getText()) <= maxRating){
            rating= Float.parseFloat(movieRatingTxt.getText());
        }else{
            errorWindow("Please input an imdb rating between 0 and 10 for your movie");
        }

        if (!moviePersonalRatingTxt.getText().isEmpty() && Float.parseFloat(moviePersonalRatingTxt.getText()) <= maxRating){
            personalRating = Float.parseFloat(moviePersonalRatingTxt.getText());

        }else{
            errorWindow("Please input a personal rating");
        }

        if (!movieFilePathTxt.getText().isEmpty()){
            filePath = movieFilePathTxt.getText();
        }else{
            errorWindow("Please select a movie file");
        }

        if (!movieTitle.isEmpty() && rating >= 0 && personalRating >= 0 && !filePath.isEmpty()) {
            movieModel.createMovie(movieTitle, rating,filePath,personalRating);

            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * chooseMovieFileBtn er Choose knappen, som man benytter, når man skal vælge/finde Filepath. Da filepath er en
     * mp4 fil, da man ønsker at finde en film.
     * @param actionEvent javaFX event klasse
     */
    public void chooseMovieFileBtn(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Movies", "*.mp4", "*.mpeg4"));
        fileChooser.setInitialDirectory(new File("Movies/" ));
        File file = fileChooser.showOpenDialog(null);


        if (file != null) {
            movieFilePathTxt.setText( file.getName());
        }
    }

    /**
     * handleCancelBtn Cancel-Knappen, som lukker "gui/view/CreateMovie.fxml"
     * @param actionEvent javaFX event klasse
     */

    public void handleCancelBtn(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    private void errorWindow(String errorTxt){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(errorTxt);
        alert.showAndWait();
    }


}
