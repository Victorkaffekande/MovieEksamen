package bll;

import be.Movie;
import dal.MovieDAO;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class MovieManager {

    private MovieDAO movieDAO;
    public MovieManager() throws IOException {
        movieDAO = new MovieDAO();
    }

    public List<Movie> getAllObjects() throws IOException {
        return movieDAO.getAllMovies();
    }

    public void create(String name, float rating, String fileLink, Timestamp lastView) throws SQLException {
        movieDAO.createMovie(name, rating, fileLink, lastView);
    }

    public void remove(Movie movie) {
        movieDAO.deleteMovie(movie);
    }

    public void update(Movie movie) {

    }
}
