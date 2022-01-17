package gui.controller;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model.CategoryModel;
import gui.Model.MovieModel;
import gui.Model.MoviePlayModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
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
    public MoviePlayModel moviePlayModel;

    private String moviePath = "Movies/";
    private String filterType;

    public MainViewController() throws IOException {
        movieModel = new MovieModel();
        categoryModel = new CategoryModel();
        moviePlayModel = new MoviePlayModel();

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

        //radio button start position
        radioButtonTitle.setSelected(true);
        filterType = "movieFilter";

        try {
            oldMoviesWarning();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoden handleCreateCategory styrer knappen "Create" under Category, som åbner createCategory.fxml
     *
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
     *
     * @param actionEvent javaFX event klasse
     * @throws IOException
     */
    public void handleEditCategory(ActionEvent actionEvent) throws IOException {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/EditCategory.fxml"));
            Scene mainWindowScene = new Scene(root.load());

            Stage editCategoryStage = new Stage();
            editCategoryStage.setScene(mainWindowScene);
            editCategoryStage.setResizable(false);
            EditCategoryController editCategoryController = root.getController();
            editCategoryController.setCategory(selectedCategory);
            editCategoryStage.showAndWait();
            categoryListView.getItems().clear();
            categoryListView.setItems(categoryModel.getObservableCategories());
        } else {
            error("Select a category and try again");
        }
    }

    /**
     * handleDeleteCategory styrer knappen "delete" under Category listView. Metoden sletter et specifikt valgt category objekt
     *
     * @param actionEvent javaFX event klasse
     */
    public void handleDeleteCategory(ActionEvent actionEvent) {
        if (categoryListView.getSelectionModel().getSelectedItem() == null) {
            error("Please choose a category to delete");
        } else {
            Category category = categoryListView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + category.getName(), ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                categoryModel.deleteCategory(categoryListView.getSelectionModel().getSelectedItem());
                categoryListView.getItems().remove(categoryListView.getSelectionModel().getSelectedItem());
                moviesTable.getItems().clear();
            }
        }
    }

    /**
     * handleCreateMovieButton styrer knappen "Create" under allMovies tableview, som åbner "gui/view/CreateMovie.fxml"
     *
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
     *
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
        } else {
            error("Select a movie and try again");
        }
    }

    /**
     * handleDeleteMovieButton styrer knappen "delete" under Movie TableView. Metoden sletter et specifikt valgt Movie objekt hvis der vælges yes i alertboxen
     *
     * @param actionEvent javaFX event klasse
     */
    public void handleDeleteMovieButton(ActionEvent actionEvent) {
        if (allMoviesTable.getSelectionModel().getSelectedItem() == null) {
            error("Please choose a movie to delete");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                Movie movie = allMoviesTable.getSelectionModel().getSelectedItem();
                movieModel.deleteMovie(movie);
                allMoviesTable.getItems().remove(movie);
            }
        }
    }

    /**
     * Error message
     *
     * @param text
     */
    private void error(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * addMovieToCategoryBtn Styrer knappen "Add", som tilføjer et Movie objekt til en Category objekt
     *
     * @param actionEvent javaFX event klasse
     */
    public void addMovieToCategoryBtn(ActionEvent actionEvent) {
        boolean alReadyInCategory = false;
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        Movie selectedMovie = allMoviesTable.getSelectionModel().getSelectedItem();

        if (selectedMovie != null && selectedCategory != null) {
            ObservableList<Movie> moviesInCategory = categoryModel.getAllMoviesFromCategoriesObservable(selectedCategory);
            for (Movie movie : moviesInCategory) {
                if (movie.getId() == selectedMovie.getId()) {
                    alReadyInCategory = true;
                    break;
                }
            }

            if (!alReadyInCategory) {
                categoryModel.addMovieToCategory(selectedCategory, selectedMovie);
                moviesInCategory.add(selectedMovie);
                moviesTable.getItems().clear();
                moviesTable.setItems(moviesInCategory);
            } else {
                error(selectedMovie.getName() + " already exists in " + selectedCategory.getName());
            }
        } else {
            error("Select a movie and which category you wish to add it to");
        }

    }

    public void handleRadioButton(ActionEvent actionEvent) {
        if (radioButtonTitle.isSelected()) {
            filterType = "movieFilter";
        } else if (radioButtonRating.isSelected()) {
            filterType = "ratingFilter";
        } else if (radioButtonCategory.isSelected()) {
            filterType = "categoryFilter";
        }
    }

    /**
     * handleButtonPlay Styrer Play/Pause stop knappen, som enten starter eller stopper en film i at blive afspillet.
     *
     * @param actionEvent javaFX event klasse
     * @throws IOException
     * @throws SQLServerException
     */
    public void handleButtonPlay(ActionEvent actionEvent) throws IOException, SQLServerException {
        ///TODO UPDATE LAST VIEW NÅR MOVIE AFSPILLES FRA CATEGORY, IKKE KUN FRA ALLMOVIES TABLE
        if (allMoviesTable.getSelectionModel().getSelectedItem() != null) {
            Movie movie = allMoviesTable.getSelectionModel().getSelectedItem();
            movieModel.updateMovieTime(movie);
            Desktop.getDesktop().open(new File(moviePlayModel.getMovieUrl() + movie.getFileLink()));
            allMoviesTable.getItems().clear();
            allMoviesTable.setItems(movieModel.getObservableMovies());
        } else if (moviesTable.getSelectionModel().getSelectedItem() != null) {
            Movie movie2 = moviesTable.getSelectionModel().getSelectedItem();
            movieModel.updateMovieTime(movie2);
            Desktop.getDesktop().open(new File(moviePlayModel.getMovieUrl() + movie2.getFileLink()));
            moviesTable.refresh();
        } else
            error("Please select a movie to play");
    }

    public void lookAtCategoryMovies() {
        if (categoryListView.getSelectionModel().getSelectedItem() != null) {
            Category category = categoryListView.getSelectionModel().getSelectedItem();
            ObservableList<Movie> observableList = categoryModel.getAllMoviesFromCategoriesObservable(category);
            moviesTable.setItems(observableList);
        }
    }

    public void deleteMovieFromCategory(ActionEvent actionEvent) {

        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        Movie selectedMovie = moviesTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null && selectedMovie != null) {
            categoryModel.deleteMovieFromCategory(selectedCategory, selectedMovie);

            moviesTable.getItems().clear();
            moviesTable.setItems(categoryModel.getAllMoviesFromCategoriesObservable(selectedCategory));
        } else {
            error("Select a category and what movie you wish to delete");
        }
    }

    public void handleMovieTableClicked(MouseEvent mouseEvent) {
        allMoviesTable.getSelectionModel().clearSelection();
    }

    public void allMoviesTableClicked(MouseEvent mouseEvent) {
        moviesTable.getSelectionModel().clearSelection();
    }

    public void playMovieMediaView(ActionEvent actionEvent) throws IOException {
        moviePlayModel.setMovieUrl(moviePlayModel.getMovieUrl() + allMoviesTable.getSelectionModel().getSelectedItem().getFileLink());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/view/MoviePlay.fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        }


    private void oldMoviesWarning() throws IOException {
        if (!movieModel.checkForOldMovies().isEmpty()) {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/view/OldMoviesView.fxml"));
            Scene mainWindowScene = new Scene(root.load());

            Stage oldMovieStage = new Stage();
            oldMovieStage.setScene(mainWindowScene);
            oldMovieStage.setResizable(false);
            oldMovieStage.showAndWait();
        }
    }

    public void handleSearchButton(ActionEvent actionEvent) throws SQLException, IOException {
        movieModel.searchMovie(filterInput.getText(), filterType);
    }
}
