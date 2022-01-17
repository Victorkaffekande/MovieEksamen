package gui.controller;

import be.Movie;
import gui.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OldMoviesViewController implements Initializable {
    @FXML
    private TableColumn<Movie, String> oldMoviesNameColumn;
    @FXML
    private TableColumn<Movie, Float> oldMoviesRatingColumn;
    @FXML
    private TableColumn<Movie, Float> oldMoviesPersonalRatingColumn;
    @FXML
    private TableColumn<Movie, String> oldMoviesLastViewedColumn;
    @FXML
    private TableView<Movie> oldMoviesTableView;

    private MovieModel movieModel;
    private ObservableList<Movie> badMoviesList;

    public OldMoviesViewController() throws IOException {
        movieModel = new MovieModel();
        badMoviesList = FXCollections.observableArrayList();
        badMoviesList.addAll(movieModel.checkForOldMovies());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        oldMoviesNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        oldMoviesRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("Rating"));
        oldMoviesPersonalRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("personalRating"));
        oldMoviesLastViewedColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));
        if (!badMoviesList.isEmpty()) {
            oldMoviesTableView.getItems().addAll(badMoviesList);
        }
    }


    public void handleOKButton(ActionEvent actionEvent) {
        Stage stage = (Stage) oldMoviesTableView.getScene().getWindow();
        stage.close();
    }
}