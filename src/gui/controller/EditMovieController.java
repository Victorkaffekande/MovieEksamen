package gui.controller;

import be.Movie;
import gui.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditMovieController extends MovieController implements Initializable {

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
    private java.sql.Timestamp lastView;
    private MovieModel movieModel;

    public EditMovieController() throws IOException {
        movieModel = new MovieModel();
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
     *
     * @param actionEvent javaFX event klasse
     */
    public void handleNewMovieCancelEdit(ActionEvent actionEvent) {
        closeWindow(newMovieCancelEdit);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addNumbersOnlyListener(txtMovieRatingEdit);
        addNumbersOnlyListener(txtMoviePersonalRatingEdit);
    }

    /**
     * handleNewMovieAcceptEdit er "Accept-knappen", som overwriter de gamle values tilknyttet et movie objekt med de
     * nye values
     *
     * @param actionEvent javaFX event klasse
     * @throws SQLException
     */
    public void handleNewMovieAcceptEdit(ActionEvent actionEvent) throws SQLException {
        String title = getMovieTitle(txtMovieTitleEdit);
        String filePath = getFilePath(txtMovieFilepathEdit);
        float rating = getRating(txtMovieRatingEdit);
        float personalRating = getPersonalRating(txtMoviePersonalRatingEdit);

        Movie movie = new Movie(id, title, rating, filePath, lastView, personalRating);
        movieModel.updateMovie(movie);

        closeWindow(newMovieAcceptEdit);
    }

    /**
     * handleChooseFilePath er Choose knappen, som man benytter, når man skal vælge/finde Filepath. Da filepath er en
     * mp4 fil, da man ønsker at finde en film.
     *
     * @param actionEvent javaFX event klasse
     */
    public void handleChooseFilepath(ActionEvent actionEvent) {
        chooseFilePath(txtMovieFilepathEdit);
    }


}

