package gui.Model;

import be.Movie;
import bll.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class MovieModel {
    private MovieManager movieManager;
    private ObservableList<Movie> movieList;

    public MovieModel() throws IOException {
       movieManager = new MovieManager();
       movieList = FXCollections.observableArrayList();
       movieList.addAll(movieManager.getAllObjects());
    }

    private List<Movie> getAllMovies() throws IOException {
        return movieManager.getAllObjects();

    }

    public void createMovie(String name, float rating, String fileLink, Timestamp lastView) throws SQLException {
        movieManager.create(name, rating, fileLink, lastView);
    }
    public void removeMovie(Movie movie){
        movieManager.remove(movie);
    }

    public ObservableList<Movie> getObservableMovies() throws IOException {
        movieList.clear();
        movieList.addAll(getAllMovies());

        return movieList;
    }
}
