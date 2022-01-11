package gui.controller;

import be.Movie;
import gui.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class EditMovieController {

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
}

    public void handleChooseEdit(ActionEvent actionEvent) {

    }


    public void handleNewMovieCancelEdit(ActionEvent actionEvent) {
        Stage stage = (Stage) newMovieCancelEdit.getScene().getWindow();
        stage.close();
    }

    public void handleNewMovieAcceptEdit(ActionEvent actionEvent) throws SQLException {
        if (!txtMovieTitleEdit.getText().isEmpty() && !txtMovieRatingEdit.getText().isEmpty() && !txtMovieFilepathEdit.getText().isEmpty()) {

            Movie updateMovie = new Movie(id, txtMovieTitleEdit.getText(),Float.parseFloat(txtMovieRatingEdit.getText()), txtMovieFilepathEdit.getText(), lastView);

            moviemodel.updateMovie(updateMovie);

            Stage stage = (Stage) newMovieAcceptEdit.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("fill out all textfields");
            alert.showAndWait();        }
    }
}
