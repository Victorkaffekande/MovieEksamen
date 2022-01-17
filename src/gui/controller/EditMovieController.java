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
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class EditMovieController implements Initializable {

    @FXML
    private TextField txtMoviePersonalRatingEdit;
    @FXML
    private TextField txtMovieId;
    @FXML
    private TextField txtMovieTitleEdit;
    @FXML
    private TextField txtMovieRatingEdit;
    @FXML
    private TextField txtMovieFilepathEdit;
    @FXML
    private Button newMovieCancelEdit;
    @FXML
    private Button newMovieAcceptEdit;

    private int id;
    private Timestamp lastView;
    private final int MAXRATING = 10;

    private MovieModel moviemodel;

    public EditMovieController() throws IOException {
        moviemodel = new MovieModel();
    }

    public void setMovie(Movie movie) {
    id = movie.getId();
    lastView = movie.getLastView();
    txtMovieTitleEdit.setText(movie.getName());
    txtMovieRatingEdit.setText(Float.toString(movie.getRating()));
    txtMovieFilepathEdit.setText(movie.getFileLink());
    txtMoviePersonalRatingEdit.setText(Float.toString(movie.getPersonalRating()));
    }


    /**
     * Cancel-Knappen, som lukker "gui/view/EditMovie.fxml"
     * @param actionEvent javaFX event klasse
     */
    public void handleNewMovieCancelEdit(ActionEvent actionEvent) {
        Stage stage = (Stage) newMovieCancelEdit.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtMovieRatingEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}([\\.]\\d{0,1})?")) {
                    txtMovieRatingEdit.setText(oldValue);
                }
            }
        });

        txtMoviePersonalRatingEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,1}([\\.]\\d{0,1})?")) {
                    txtMoviePersonalRatingEdit.setText(oldValue);
                }
            }
        });
    }

    /**
     * handleNewMovieAcceptEdit er "Accept-knappen", som overwriter de gamle values tilknyttet et movie objekt med de
     * nye values
     * @param actionEvent javaFX event klasse
     * @throws SQLException
     */
    public void handleNewMovieAcceptEdit(ActionEvent actionEvent) throws SQLException {
        String movieTitle="";
        String filePath="";
        float rating=-1;
        float personalRating=-1;

        if (!txtMovieTitleEdit.getText().isEmpty()){
            movieTitle = txtMovieTitleEdit.getText();
        }
        else{
            errorWindow("Please name the movie");
        }

        if (!txtMovieRatingEdit.getText().isEmpty() && Float.parseFloat(txtMovieRatingEdit.getText()) <= MAXRATING){
            rating= Float.parseFloat(txtMovieRatingEdit.getText());
        }else{
            errorWindow("Please input an imdb rating between 0 and 10 for your movie");
        }

        if (!txtMoviePersonalRatingEdit.getText().isEmpty() && Float.parseFloat(txtMoviePersonalRatingEdit.getText()) <= MAXRATING){
            personalRating = Float.parseFloat(txtMoviePersonalRatingEdit.getText());

        }else{
            errorWindow("Please input a personal rating");
        }

        if (!txtMovieFilepathEdit.getText().isEmpty()){
            filePath = txtMovieFilepathEdit.getText();
        }else{
            errorWindow("Please select a movie file");
        }


        if (!movieTitle.isEmpty() && rating >= 0 && personalRating >= 0 && !filePath.isEmpty()) {

            Movie updateMovie = new Movie(id, movieTitle,rating,filePath,lastView,personalRating);

            moviemodel.updateMovie(updateMovie);

            Stage stage = (Stage) newMovieAcceptEdit.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * handleChooseFilePath er Choose knappen, som man benytter, når man skal vælge/finde Filepath. Da filepath er en
     * mp4 fil, da man ønsker at finde en film.
     * @param actionEvent javaFX event klasse
     */
    public void handleChooseFilepath(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Movies", "*.mp4", "*.mpeg4"));
        fileChooser.setInitialDirectory(new File("Movies/" ));
        File file = fileChooser.showOpenDialog(null);


        if (file != null) {
            txtMovieFilepathEdit.setText( file.getName());
        }
    }

    private void errorWindow(String errorTxt){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(errorTxt);
        alert.showAndWait();
    }

}

