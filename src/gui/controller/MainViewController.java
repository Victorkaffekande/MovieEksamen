package gui.controller;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model.CategoryModel;
import gui.Model.MovieModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private TableColumn<Movie, String> moviesLastViewCol;
    @FXML
    private TableColumn<Movie, String> allMoviesLastViewCol;
    @FXML
    private RadioButton radioButtonTitle;
    @FXML
    private RadioButton radioButtonCategory;
    @FXML
    private RadioButton radioButtonRating;
    @FXML
    private TableView<Movie> allMoviesTable;
    @FXML
    private TableColumn<Movie, String> allMoviesNameColumn;
    @FXML
    private TableColumn<Movie, Float> allMoviesRatingColumn;
    @FXML
    private TableView<Movie> moviesTable;
    @FXML
    private TableColumn<Movie, String> moviesNameColumn;
    @FXML
    private TableColumn<Movie, Float> moviesRatingColumn;
    @FXML
    private TextField filterInput;
    @FXML
    private ListView<Category> categoryListView;


    private MovieModel movieModel;
    private CategoryModel categoryModel;

    private String moviePath = "Movies/";
    private String filterType;

    public MainViewController() throws IOException {
        movieModel = new MovieModel();
        categoryModel = new CategoryModel();


        moviesTable = new TableView();
        moviesNameColumn = new TableColumn();
        moviesRatingColumn = new TableColumn();

        allMoviesTable = new TableView();
        allMoviesNameColumn = new TableColumn();
        allMoviesRatingColumn = new TableColumn();
        allMoviesLastViewCol = new TableColumn();

        categoryListView = new ListView();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allMoviesNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        allMoviesRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("Rating"));
        allMoviesLastViewCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));

        moviesNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        moviesRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("Rating"));
        moviesLastViewCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));


        //Sets items in the tableView and the listView
        try {
            allMoviesTable.setItems(movieModel.getObservableMovies());
            categoryListView.setItems(categoryModel.getObservableCategories());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //movie search
        radioButtonTitle.setSelected(true);
        filterType = "movieFilter";
        filterInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue,filterType);
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

    public void handleEditCategory(ActionEvent actionEvent) throws IOException {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/EditCategory.fxml"));
            Scene mainWindowScene = null;

            try {
                mainWindowScene = new Scene(root.load());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Stage editCategoryStage = new Stage();
            editCategoryStage.setScene(mainWindowScene);
            EditCategoryController editCategoryController = root.getController();
            editCategoryController.setCategory(selectedCategory);
            editCategoryStage.showAndWait();
            categoryListView.getItems().clear();
            categoryListView.setItems(categoryModel.getObservableCategories());
        }
    }

    public void handleDeleteCategory(ActionEvent actionEvent) {
        if (categoryListView.getSelectionModel().getSelectedItem() == null) {
            error("Please choose a category to delete");
        } else {
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
        allMoviesTable.getItems().clear();
        allMoviesTable.setItems(movieModel.getObservableMovies());
    }

    public void handleEditMovieButton(ActionEvent actionEvent) throws IOException {
        Movie selectedMovie = allMoviesTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/EditMovie.fxml"));
            Scene mainWindowScene = null;

            try {
                mainWindowScene = new Scene(root.load());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Stage editMovieStage = new Stage();
            editMovieStage.setScene(mainWindowScene);
            EditMovieController editMovieController = root.getController();
            editMovieController.setMovie(selectedMovie);
            editMovieStage.showAndWait();
            allMoviesTable.getItems().clear();
            allMoviesTable.setItems(movieModel.getObservableMovies());
        }
    }


    public void handleDeleteMovieButton(ActionEvent actionEvent) {
        if (allMoviesTable.getSelectionModel().getSelectedItem() == null) {
            error("Please choose a movie to delete");
        } else {
            movieModel.deleteMovie(allMoviesTable.getSelectionModel().getSelectedItem());
            allMoviesTable.getItems().remove(allMoviesTable.getSelectionModel().getSelectedItem());

        }
    }


    private void error(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
    }

    public void addMovieToCategoryBtn(ActionEvent actionEvent) {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        Movie selectedMovie = allMoviesTable.getSelectionModel().getSelectedItem();
        categoryModel.addMovieToCategory(selectedCategory, selectedMovie);
    }

    public void handleRadioButton(ActionEvent actionEvent) {
        if (radioButtonTitle.isSelected()){
            filterType = "movieFilter";
        } else if(radioButtonRating.isSelected()){
            filterType = "ratingFilter";
        }else if (radioButtonCategory.isSelected()){

        }
    }

    public void handleButtonPlay(ActionEvent actionEvent) throws IOException, SQLServerException {
        if (allMoviesTable.getSelectionModel().getSelectedItem() != null) {
            Movie movie = allMoviesTable.getSelectionModel().getSelectedItem();
            movieModel.updateMovieTime(movie);
            Desktop.getDesktop().open(new File(moviePath + movie.getFileLink()));
            allMoviesTable.getItems().clear();
            allMoviesTable.setItems(movieModel.getObservableMovies());
        } else if (moviesTable.getSelectionModel().getSelectedItem() != null) {
            Movie movie2 = moviesTable.getSelectionModel().getSelectedItem();
            movieModel.updateMovieTime(movie2);
            Desktop.getDesktop().open(new File(moviePath + movie2.getFileLink()));
            moviesTable.refresh();
        } else
            error("Please select a movie");

    }

    public void lookAtCategoryMovies() {
        Category category = categoryListView.getSelectionModel().getSelectedItem();
        ObservableList<Movie> observableList = categoryModel.getAllMoviesFromCategoriesObservable(category);
        moviesTable.setItems(observableList);
    }

    public void deleteMovieFromCategory(ActionEvent actionEvent) {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        Movie selectedMovie = moviesTable.getSelectionModel().getSelectedItem();
        categoryModel.deleteMovieFromCategory(selectedCategory, selectedMovie);
    }

    public void handleMovieTableClicked(MouseEvent mouseEvent) {
        allMoviesTable.getSelectionModel().clearSelection();
    }

    public void playMovieMediaView(ActionEvent actionEvent) throws IOException {
        Movie selectedMovie = allMoviesTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/MoviePlay.fxml"));
            Scene mainWindowScene = null;

            try {
                mainWindowScene = new Scene(root.load());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Stage musicPlaystage = new Stage();
            musicPlaystage.setScene(mainWindowScene);
            MoviePlayController moviePlayController = root.getController();
            moviePlayController.setMovieUrl();
            musicPlaystage.setTitle("MoviePlay");
            musicPlaystage.show();
        }
    }

    public void allMoviesTableClicked(MouseEvent mouseEvent) {
    }
}
