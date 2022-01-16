package gui.controller;

import be.Movie;
import gui.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class EditMovieController {

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

    /**
     * handleNewMovieAcceptEdit er "Accept-knappen", som overwriter de gamle values tilknyttet et movie objekt med de
     * nye values
     * @param actionEvent javaFX event klasse
     * @throws SQLException
     */

    public void handleNewMovieAcceptEdit(ActionEvent actionEvent) throws SQLException {
        if (!txtMovieTitleEdit.getText().isEmpty() && !txtMovieRatingEdit.getText().isEmpty() && !txtMovieFilepathEdit.getText().isEmpty()) {

            Movie updateMovie = new Movie(id, txtMovieTitleEdit.getText(),Float.parseFloat(txtMovieRatingEdit.getText()), txtMovieFilepathEdit.getText(), lastView, Float.parseFloat(txtMoviePersonalRatingEdit.getText()));

            moviemodel.updateMovie(updateMovie);

            Stage stage = (Stage) newMovieAcceptEdit.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("fill out all textfields");
            alert.showAndWait();        }
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
}
