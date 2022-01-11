package gui.controller;

import be.Category;
import be.Movie;
import gui.Model.CategoryModel;
import gui.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
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

    public void handleCreateCategory(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/CreateCategory.fxml"))); // The FXML path
        Scene scene = new Scene(parent); // Scene supposed to be viewed
        Stage createCategoryStage = new Stage();
        createCategoryStage.setScene(scene); // Sets the new scene
        createCategoryStage.showAndWait(); // This shows the new scene
        categoryListView.getItems().clear();
        categoryListView.setItems(categoryModel.getObservableCategories());
    }

    public void handleEditCategory(ActionEvent actionEvent) {
    }

    public void handleDeleteCategory(ActionEvent actionEvent) {
        if (categoryListView.getSelectionModel().getSelectedItem() == null){
            error("Please choose a category to delete");
        }
        else {
            categoryModel.deleteCategory(categoryListView.getSelectionModel().getSelectedItem());
            categoryListView.getItems().remove(categoryListView.getSelectionModel().getSelectedItem());
        }
    }

    public void handleCreateMovieButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/view/CreateMovie.fxml"));
        Stage stage = new Stage();
        stage.setTitle("CreateMovie");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        moviesTable.getItems().clear();
        moviesTable.setItems(movieModel.getObservableMovies());
    }

    public void handleEditMovieButton(ActionEvent actionEvent) {
    }

    public void handleDeleteMovieButton(ActionEvent actionEvent) {
        if (allMoviesTable.getSelectionModel().getSelectedItem() == null){
            error("Please choose a movie to delete");
        }
        else {
            movieModel.deleteMovie(allMoviesTable.getSelectionModel().getSelectedItem());
            allMoviesTable.getItems().remove(allMoviesTable.getSelectionModel().getSelectedItem());

        }
    }

    public void handleButtonPlay(ActionEvent actionEvent) {
    }

    private void error(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
    }

}
