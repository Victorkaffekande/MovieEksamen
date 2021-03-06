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
     * Metoden handleCreateCategory styrer knappen "Create" under Category, som ??bner createCategory.fxml
     * @throws IOException
     */
    public void handleCreateCategory() throws IOException {
        openScene("/gui/view/CreateCategory.fxml", false, true, "Create category ", false);
        categoryListView.getItems().clear();
        categoryListView.setItems(categoryModel.getObservableCategories());
    }

    /**
     * Metoden handleEditCategory styrer knappen "Edit" under Category, som ??bner EditCategory.fxml
     * @throws IOException
     */
    public void handleEditCategory() throws IOException {
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
     */
    public void handleDeleteCategory() {
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
     * handleCreateMovieButton styrer knappen "Create" under allMovies tableview, som ??bner "gui/view/CreateMovie.fxml"
     * @throws IOException
     */
    public void handleCreateMovieButton() throws IOException {
        openScene("/gui/view/CreateMovie.fxml", false, true, "Create movie", false);
        allMoviesTable.getItems().clear();
        allMoviesTable.setItems(movieModel.getObservableMovies());
    }

    /**
     * handleEditMovieButton styrer knappen "Edit" under allMovies tableview, som ??bner "gui/view/EditMovie.fxml"
     * @throws IOException
     */
    public void handleEditMovieButton() throws IOException {
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
     * handleDeleteMovieButton styrer knappen "delete" under Movie TableView. Metoden sletter et specifikt valgt Movie objekt hvis der v??lges yes i alertboxen
     */
    public void handleDeleteMovieButton() {
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
     * addMovieToCategoryBtn Styrer knappen "Add", som tilf??jer et Movie objekt til en Category objekt
     */
    public void addMovieToCategoryBtn() {
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

    public void handleRadioButton() {
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
     *
     * @throws IOException
     * @throws SQLServerException
     */
    public void handleButtonPlay() throws IOException, SQLServerException {
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

    public void deleteMovieFromCategory() {

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

    /**
     *
     * Afspiller filmen ved anvendelse af mediaView klassen i et nyt vindue
     * Opdaterer tiden p?? filmene n??r de bliver afspillet
     * @throws IOException
     * @throws SQLServerException
     */
    public void playMovieMediaView() throws IOException, SQLServerException {
        Movie selectedMovieAllMoviesTable = allMoviesTable.getSelectionModel().getSelectedItem();
        Movie selectedMovieMoviesTable = moviesTable.getSelectionModel().getSelectedItem();
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if(selectedMovieAllMoviesTable != null){
            movieModel.updateMovieTime(selectedMovieAllMoviesTable);
            moviePlayModel.setMovieUrl(moviePlayModel.getMovieUrl() + selectedMovieAllMoviesTable.getFileLink());
            allMoviesTable.getItems().clear();
            allMoviesTable.setItems(movieModel.getObservableMovies());
            openScene("/gui/view/MoviePlay.fxml", true, false, "MoviePlayer", false);
        }
        else if(selectedMovieMoviesTable != null){
            movieModel.updateMovieTime(selectedMovieMoviesTable);
            moviePlayModel.setMovieUrl(moviePlayModel.getMovieUrl() + selectedMovieMoviesTable.getFileLink());
            moviesTable.getItems().clear();
            moviesTable.setItems(categoryModel.getAllMoviesFromCategoriesObservable(selectedCategory));
            openScene("/gui/view/MoviePlay.fxml", true, false, "MoviePlayer", false);
        }
        else{
            error("Please select a movie to play");
        }




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

    public void handleSearchButton() throws SQLException, IOException {
        movieModel.searchMovie(filterInput.getText(), filterType);
    }

    /**
     * Metode anvendt til at ??bne de forskellige vinduer i programmet
     * @param pathToFXML - Stien til FXML vinduet
     * @param undecorated - et boolean parameter, der bestemmer hverenten der skal v??re dekorationer i et FXML vindue (dekorationer = forst??rrrelse knappen, minimer knappen og exit krydset)
     * @param showAndWait - et boolean parameter der bestemmer om der skal anvendes showAndWait() eller show() metoden
     * @param title - titlen p?? FXML vinduet
     * @param resizable - et boolean parameter der bestemmer om et vindue kan redigeres i st??rrelsen
     * @throws IOException
     */
    public void openScene(String pathToFXML, boolean undecorated, boolean showAndWait, String title, boolean resizable) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(pathToFXML));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        if(undecorated){
            stage.initStyle(StageStyle.UNDECORATED);
        }
        if(!undecorated){
            stage.initStyle(StageStyle.DECORATED);
        }

        stage.setTitle(title);
        stage.setResizable(resizable);

        stage.setScene(scene);
        if(showAndWait){
            stage.showAndWait();
        }

        if(!showAndWait){
            stage.show();
        }
    }
}
