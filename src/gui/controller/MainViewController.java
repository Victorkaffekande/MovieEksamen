package gui.controller;

import be.Category;
import be.Movie;
import gui.Model.CategoryModel;
import gui.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    public TableView<Movie> allMoviesTable;

    public TableColumn<Movie, String> allMoviesNameColumn;

    public TableColumn<Movie, Float> allMoviesRatingColumn;

    public TableView<Movie> moviesTable;

    public TableColumn<Movie, String> moviesNameColumn;

    public TableColumn<Movie, Float> moviesRatingColumn;

    public TextField filterInput;
    public ListView<Category> categoryListView;


    private MovieModel movieModel;
    private CategoryModel categoryModel;

    public MainViewController() throws IOException {
        movieModel = new MovieModel();
        categoryModel = new CategoryModel();


        moviesTable = new TableView();
        moviesNameColumn = new TableColumn();
        moviesRatingColumn = new TableColumn();

        allMoviesTable= new TableView();
        allMoviesNameColumn = new TableColumn();
        allMoviesRatingColumn = new TableColumn();

        categoryListView = new ListView();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allMoviesNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        allMoviesRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("Rating"));

        //Sets items in the tableView and the listView
        try {
            allMoviesTable.setItems(movieModel.getObservableMovies());
            categoryListView.setItems(categoryModel.getObservableCategories());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //movie search
        filterInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void handleCreateCategory(ActionEvent actionEvent) {
    }

    public void handleEditCategory(ActionEvent actionEvent) {
    }

    public void handleDeleteCategory(ActionEvent actionEvent) {
    }

    public void handleCreateMovieButton(ActionEvent actionEvent) {
    }

    public void handleEditMovieButton(ActionEvent actionEvent) {
    }

    public void handleDeleteMovieButton(ActionEvent actionEvent) {
    }

    public void handleButtonPlay(ActionEvent actionEvent) {
    }
}
