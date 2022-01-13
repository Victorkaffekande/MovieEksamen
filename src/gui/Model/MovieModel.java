package gui.Model;

import be.Movie;
import bll.MovieManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
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

    public void createMovie(String name, float rating, String fileLink, float personalRating) throws SQLException {
        movieManager.create(name, rating, fileLink, personalRating);
    }
    public void deleteMovie(Movie movie){
        movieManager.delete(movie);
    }

    public ObservableList<Movie> getObservableMovies() throws IOException {
        movieList.clear();
        movieList.addAll(getAllMovies());
        return movieList;
    }

    public void searchMovie(String query,String type) throws IOException, SQLException {
        List<Movie> allMovies = getAllMovies();
        List<Movie> result = movieManager.getSearchedMovies(allMovies,query,type);
        movieList.clear();
        movieList.addAll(result);
    }

    public void updateMovie(Movie movieUpdate) throws SQLException {
        movieManager.update(movieUpdate);
    }
    public void updateMovieTime(Movie movie) throws SQLServerException {
        movieManager.updateMovieTime(movie);
    }

}
