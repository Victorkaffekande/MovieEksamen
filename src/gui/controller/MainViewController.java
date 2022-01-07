package gui.controller;

import be.Movie;
import gui.Model.MovieModel;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    public TableView allMoviesTable;

    public TableColumn allMoviesNameColumn;

    public TableColumn allMoviesRatingColumn;

    public TableView moviesTable;

    public TableColumn moviesNameColumn;

    public TableColumn moviesRatingColumn;


    private MovieModel movieModel = new MovieModel();

    public MainViewController() throws IOException {

        moviesTable = new TableView();
        moviesNameColumn = new TableColumn();
        moviesRatingColumn = new TableColumn();

        allMoviesTable= new TableView();
        allMoviesNameColumn = new TableColumn();
        allMoviesRatingColumn = new TableColumn();


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allMoviesNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        allMoviesRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("Rating"));

        try {
            allMoviesTable.setItems(movieModel.getObservableMovies());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
