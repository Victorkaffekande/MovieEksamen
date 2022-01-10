package bll;

import be.Category;
import be.Movie;
import bll.util.Filter;
import dal.MovieDAO;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class MovieManager {

    private MovieDAO movieDAO;
    private Filter filter;
    public MovieManager() throws IOException {
        movieDAO = new MovieDAO();
        filter = new Filter();
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

    public void update(Movie movie) throws SQLException {
        movieDAO.updateMovie(movie);
    }

    public List<Movie> getCategoryMovies(int categoryId) throws SQLException {
      return movieDAO.getCategoryMovie(categoryId);
    }

    public List<Movie> getSearchedMovies(List<Movie> movieList, String query) throws SQLException {
        return filter.search(movieList, query);
    }
}
