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
    private TableColumn<Movie, Float> allMoviesPersonalRatingColumn;
    @FXML
    private TableColumn<Movie, Float> moviesPersonalRatingColumn;
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
        moviesPersonalRatingColumn = new TableColumn();

        allMoviesTable = new TableView();
        allMoviesNameColumn = new TableColumn();
        allMoviesRatingColumn = new TableColumn();
        allMoviesPersonalRatingColumn = new TableColumn();
        allMoviesLastViewCol = new TableColumn();

        categoryListView = new ListView();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allMoviesNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        allMoviesRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("Rating"));
        allMoviesPersonalRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("personalRating"));
        allMoviesLastViewCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));

        moviesNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        moviesRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("Rating"));
        moviesPersonalRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("personalRating"));
        moviesLastViewCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));



        try {
            allMoviesTable.setItems(movieModel.getObservableMovies());
            categoryListView.setItems(categoryModel.getObservableCategories());
        } catch (IOException e) {
            e.printStackTrace();
        }

        radioButtonTitle.setSelected(true);
        filterType = "movieFilter";
        filterInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue,filterType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try {
            oldMoviesWarning();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoden handleCreateCategory styrer knappen "Create" under Category, som åbner createCategory.fxml
     * @param actionEvent javaFX event klasse
     * @throws IOException
     */

    public void handleCreateCategory(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/CreateCategory.fxml"))); // The FXML path
        Scene scene = new Scene(parent); // Scenen som skal vises
        Stage createCategoryStage = new Stage();
        createCategoryStage.setResizable(false);
        createCategoryStage.setScene(scene); // Sets the new scene
        createCategoryStage.showAndWait(); // This shows the new scene
        categoryListView.getItems().clear();
        categoryListView.setItems(categoryModel.getObservableCategories());
    }

    /**
     * Metoden handleEditCategory styrer knappen "Edit" under Category, som åbner EditCategory.fxml
     * @param actionEvent javaFX event klasse
     * @throws IOException
     */

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
            editCategoryStage.setResizable(false);
            EditCategoryController editCategoryController = root.getController();
            editCategoryController.setCategory(selectedCategory);
            editCategoryStage.showAndWait();
            categoryListView.getItems().clear();
            categoryListView.setItems(categoryModel.getObservableCategories());
        }
    }

    /**
     * handleDeleteCategory styrer knappen "delete" under Category listView. Metoden sletter et specifikt valgt category objekt
     * @param actionEvent javaFX event klasse
     */

    public void handleDeleteCategory(ActionEvent actionEvent) {
        if (categoryListView.getSelectionModel().getSelectedItem() == null) {
            error("Please choose a category to delete");
        } else {
            categoryModel.deleteCategory(categoryListView.getSelectionModel().getSelectedItem());
            categoryListView.getItems().remove(categoryListView.getSelectionModel().getSelectedItem());
        }
    }


    /**
     * handleCreateMovieButton styrer knappen "Create" under allMovies tableview, som åbner "gui/view/CreateMovie.fxml"
     * @param actionEvent javaFX event klasse
     * @throws IOException
     */

    public void handleCreateMovieButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/view/CreateMovie.fxml"));
        Stage stage = new Stage();
        stage.setTitle("CreateMovie");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
        allMoviesTable.getItems().clear();
        allMoviesTable.setItems(movieModel.getObservableMovies());
    }

    /**
     * handleEditMovieButton styrer knappen "Edit" under allMovies tableview, som åbner "gui/view/EditMovie.fxml"
     * @param actionEvent javaFX event klasse
     * @throws IOException
     */

    public void handleEditMovieButton(ActionEvent actionEvent) throws IOException {
        Movie selectedMovie = allMoviesTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/EditMovie.fxml"));
            Scene mainWindowScene = new Scene(root.load());

            Stage editMovieStage = new Stage();
            editMovieStage.setScene(mainWindowScene);
            EditMovieController editMovieController = root.getController();
            editMovieController.setMovie(selectedMovie);
            editMovieStage.setResizable(false);
            editMovieStage.showAndWait();
            allMoviesTable.getItems().clear();
            allMoviesTable.setItems(movieModel.getObservableMovies());
        }
    }

    /**
     * handleDeleteMovieButton styrer knappen "delete" under Movie TableView. Metoden sletter et specifikt valgt Movie objekt
     * @param actionEvent javaFX event klasse
     */

    public void handleDeleteMovieButton(ActionEvent actionEvent) {
        if (allMoviesTable.getSelectionModel().getSelectedItem() == null) {
            error("Please choose a movie to delete");
        } else {
            movieModel.deleteMovie(allMoviesTable.getSelectionModel().getSelectedItem());
            allMoviesTable.getItems().remove(allMoviesTable.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Error message
     * @param text
     */

    private void error(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * addMovieToCategoryBtn Styrer knappen "Add", som tilføjer et Movie objekt til en Category objekt
     * @param actionEvent javaFX event klasse
     */
    public void addMovieToCategoryBtn(ActionEvent actionEvent) {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        Movie selectedMovie = allMoviesTable.getSelectionModel().getSelectedItem();
        categoryModel.addMovieToCategory(selectedCategory, selectedMovie);

        moviesTable.getItems().clear();
        moviesTable.setItems(categoryModel.getAllMoviesFromCategoriesObservable(selectedCategory));
    }


    public void handleRadioButton(ActionEvent actionEvent) {
        if (radioButtonTitle.isSelected()){
            filterType = "movieFilter";
        } else if(radioButtonRating.isSelected()){
            filterType = "ratingFilter";
        }else if (radioButtonCategory.isSelected()){
            filterType = "categoryFilter";
        }
    }

    /**
     * handleButtonPlay Styrer Play/Pause stop knappen, som enten starter eller stopper en film i at blive afspillet.
     * @param actionEvent javaFX event klasse
     * @throws IOException
     * @throws SQLServerException
     */

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

        moviesTable.getItems().clear();
        moviesTable.setItems(categoryModel.getAllMoviesFromCategoriesObservable(selectedCategory));
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

    private void oldMoviesWarning() throws IOException {
        if (!movieModel.checkForOldMovies().isEmpty()){
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/OldMoviesView.fxml"));
            Scene mainWindowScene = new Scene(root.load());

            Stage oldMovieStage = new Stage();
            oldMovieStage.setScene(mainWindowScene);
            oldMovieStage.setResizable(false);
            oldMovieStage.showAndWait();
        }
    }
}
