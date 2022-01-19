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
    private final int  MAXRATING = 10;

    protected MovieController() throws IOException {
        MovieModel movieModel = new MovieModel();
    }

    /**
     * Giver en listener til et tekstfelt der sikre at
     * tekstfeltet kun kan modtage tal og at der ikke er
     * flere end to tal før kommaet og kun et tal efter
     * @param movieRatingTxt
     */
    public void addNumbersOnlyListener(TextField movieRatingTxt) {
        movieRatingTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}([\\.]\\d{0,1})?")) {
                movieRatingTxt.setText(oldValue);
            }
        });
    }

    /**
     * Finder en string fra det givne tekstfelt
     * @param titleField teksfelt til filmnavn
     * @return en trimmet string hvis feltet indeholder andet end mellemrum
     */
    public String getMovieTitle(TextField titleField){
        if (!titleField.getText().trim().isEmpty()){
            return titleField.getText().trim();
        }
        else{
            errorWindow("Please name the movie");
        }
        return null;
    }

    /**
     * Finder en string fra det givne tekstfelt
     * @param fileField teksfelt til filmnavn
     * @return en trimmet string hvis feltet indeholder andet end mellemrum
     */
    public String getFilePath(TextField fileField){
        if (!fileField.getText().trim().isEmpty()){
            return fileField.getText().trim();
        }else{
            errorWindow("Please select a movie file");
        }
        return null;
    }

    /**
     * Finder ratingen i et tekstfelt
     * @param ratingField tekstfeltet hvor ratingen befinder sig
     * @return en rating float hvis feltet ikke er tomt.
     */
    public float getRating(TextField ratingField){
        float rating = -1;
        if (!ratingField.getText().trim().isEmpty() && Float.parseFloat(ratingField.getText().trim()) <= MAXRATING){
            rating = Float.parseFloat(ratingField.getText().trim());
        }else{
            errorWindow("Please input an imdb rating between 0 and 10 for your movie");
        }
        return rating;
    }

    /**
     * Finder den personlige rating i et tekstfelt
     * @param personalRatingField tekstfeltet hvor ratingen befinder sig
     * @return en rating float hvis feltet ikke er tomt.
     */
    public float getPersonalRating(TextField personalRatingField){
        float personalRating = -1;
        if (!personalRatingField.getText().trim().isEmpty() && Float.parseFloat(personalRatingField.getText().trim()) <= MAXRATING){
            personalRating = Float.parseFloat(personalRatingField.getText());
        }else{
            errorWindow("Please input a personal rating between 0 and 10");
        }
        return personalRating;
    }

    /**
     * åbner et errorvindue
     * @param errorTxt den tekst vinduet skal indeholde
     */
    private void errorWindow(String errorTxt){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(errorTxt);
        alert.showAndWait();
    }

    /**
     * finder filepathen fra det valgte tekstfelt
     * @param fileField tekstfeltet hvor fil stien befinder sig
     */
    public void chooseFilePath(TextField fileField){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Movies", "*.mp4", "*.mpeg4"));
        fileChooser.setInitialDirectory(new File("Movies/" ));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            fileField.setText( file.getName());
        }
    }

    /**
     * lukker et vindue
     * @param anyButton en knap der befinder sig i det vindue der skal lukkes
     */
    public void closeWindow(Button anyButton){
        Stage stage = (Stage) anyButton.getScene().getWindow();
        stage.close();
    }

}
