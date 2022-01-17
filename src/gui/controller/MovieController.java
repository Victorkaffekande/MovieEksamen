package gui.controller;

import be.Movie;
import gui.Model.MovieModel;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public abstract  class MovieController {
    MovieModel movieModel;

    protected MovieController() throws IOException {
        movieModel = new MovieModel();
    }

    public void addNumbersOnlyListener(TextField movieRatingTxt) {
        movieRatingTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}([\\.]\\d{0,1})?")) {
                movieRatingTxt.setText(oldValue);
            }
        });
    }

    public void acceptButton(TextField titleField, TextField fileField, TextField ratingField, TextField personalRatingField) throws SQLException {
        String movieTitle="";
        String filePath="";
        float rating=-1;
        float personalRating=-1;
        int maxRating = 10;

        if (!titleField.getText().isEmpty()){
            movieTitle = titleField.getText();
        }
        else{
            errorWindow("Please name the movie");
        }

        if (!ratingField.getText().isEmpty() && Float.parseFloat(ratingField.getText()) <= maxRating){
            rating= Float.parseFloat(ratingField.getText());
        }else{
            errorWindow("Please input an imdb rating between 0 and 10 for your movie");
        }

        if (!personalRatingField.getText().isEmpty() && Float.parseFloat(personalRatingField.getText()) <= maxRating){
            personalRating = Float.parseFloat(personalRatingField.getText());

        }else{
            errorWindow("Please input a personal rating between 0 and 10");
        }

        if (!fileField.getText().isEmpty()){
            filePath = fileField.getText();
        }else{
            errorWindow("Please select a movie file");
        }

        if (!movieTitle.isEmpty() && rating >= 0 && personalRating >= 0 && !filePath.isEmpty()) {
            movieModel.createMovie(movieTitle, rating,filePath,personalRating);

            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.close();
        }
    }
    private void errorWindow(String errorTxt){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(errorTxt);
        alert.showAndWait();
    }

    public void chooseFilePath(TextField fileField){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Movies", "*.mp4", "*.mpeg4"));
        fileChooser.setInitialDirectory(new File("Movies/" ));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            fileField.setText( file.getName());
        }
    }

    public void closeWindow(Button anyButton){
        Stage stage = (Stage) anyButton.getScene().getWindow();
        stage.close();
    }
}
