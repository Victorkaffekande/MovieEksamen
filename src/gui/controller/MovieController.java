package gui.controller;

import gui.Model.MovieModel;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public abstract  class MovieController {
    private final MovieModel movieModel;
    private final int  MAXRATING = 10;

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

    public String getMovieTitle(TextField titleField){
        if (!titleField.getText().trim().isEmpty()){
            return titleField.getText().trim();
        }
        else{
            errorWindow("Please name the movie");
        }
        return null;
    }

    public String getFilePath(TextField fileField){
        if (!fileField.getText().trim().isEmpty()){
            return fileField.getText().trim();
        }else{
            errorWindow("Please select a movie file");
        }
        return null;
    }

    public float getRating(TextField ratingField){
        float rating = -1;
        if (!ratingField.getText().trim().isEmpty() && Float.parseFloat(ratingField.getText().trim()) <= MAXRATING){
            rating = Float.parseFloat(ratingField.getText().trim());
        }else{
            errorWindow("Please input an imdb rating between 0 and 10 for your movie");
        }
        return rating;
    }

    public float getPersonalRating(TextField personalRatingField){
        float personalRating = -1;
        if (!personalRatingField.getText().trim().isEmpty() && Float.parseFloat(personalRatingField.getText().trim()) <= MAXRATING){
            personalRating = Float.parseFloat(personalRatingField.getText());
        }else{
            errorWindow("Please input a personal rating between 0 and 10");
        }
        return personalRating;
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
    public void closeWindow(TextField anyTextField){
        Stage stage = (Stage) anyTextField.getScene().getWindow();
        stage.close();
    }
}
