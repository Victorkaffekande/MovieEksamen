package gui.controller;

import be.Movie;
import bll.MovieManager;
import gui.Model.MovieModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
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
        badMoviesList.addAll(moviesToDelete());


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        oldMoviesNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        oldMoviesRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("Rating"));
        oldMoviesPersonalRatingColumn.setCellValueFactory(new PropertyValueFactory<Movie, Float>("personalRating"));
        oldMoviesLastViewedColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));

        oldMoviesTableView.setItems(badMoviesList);

        if (badMoviesList.size() == 0){
            Platform.exit();
            try {
                startMainView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public List<Movie> moviesToDelete() throws IOException {
        MovieManager movieManager = new MovieManager();
        movieManager.getAllObjects();
        int twoYearsInDays = 730;
        Date currentDate = new Date();
        long time = currentDate.getTime();
        Timestamp currentTime=new Timestamp(time);

        List<Movie> tempAllMovies = movieModel.getObservableMovies();
        List<Movie> badMovies = new ArrayList<>();
        for (Movie movie:tempAllMovies) {
            long timeDiff = (currentTime.getTime() - movie.getLastView().getTime()) / (1000*60*60*24);
            if (timeDiff > twoYearsInDays){
                badMovies.add(movie);
            }
        }
        return badMovies;
    }


    public void closeOldMoviesWindow(){
        Stage stage = (Stage) oldMoviesTableView.getScene().getWindow();
        stage.close();
    }

    public void startMainView() throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("gui/view/MainViewController.fxml")));
        Scene scene = new Scene(parent);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Private movie collection");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void handleYesButton(ActionEvent actionEvent) {
    }

    public void handleNoButton(ActionEvent actionEvent) {
    }


}
