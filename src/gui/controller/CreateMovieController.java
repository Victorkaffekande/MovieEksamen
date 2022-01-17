package gui.controller;

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

public class CreateMovieController extends MovieController implements Initializable {
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
        addNumbersOnlyListener(movieRatingTxt);
        addNumbersOnlyListener(moviePersonalRatingTxt);
    }

    /**
     * saveMovieBtn opretter det nye movie objekt
     *
     * @param actionEvent javaFX event klasse
     * @throws SQLException
     */
    public void saveMovieBtn(ActionEvent actionEvent) throws SQLException {
        String title = getMovieTitle(movieTitleTxt);
        String filePath = getFilePath(movieFilePathTxt);
        float rating = getRating(movieRatingTxt);
        float personalRating = getPersonalRating(moviePersonalRatingTxt);
        movieModel.createMovie(title, rating, filePath, personalRating);
        closeWindow(cancelBtn);
    }

    /**
     * chooseMovieFileBtn er Choose knappen, som man benytter, når man skal vælge/finde Filepath. Da filepath er en
     * mp4 fil, da man ønsker at finde en film.
     *
     * @param actionEvent javaFX event klasse
     */
    public void chooseMovieFileBtn(ActionEvent actionEvent) {
        chooseFilePath(movieFilePathTxt);
    }

    /**
     * handleCancelBtn Cancel-Knappen, som lukker "gui/view/CreateMovie.fxml"
     *
     * @param actionEvent javaFX event klasse
     */
    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }

}
