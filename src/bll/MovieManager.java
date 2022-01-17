package bll;


import be.Movie;
import bll.util.Filter;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.MovieDAO;

import java.io.IOException;
import java.sql.SQLException;

import java.util.List;

/**
 * Klassen Moviemanager indeholder alle metoderne, som anvendes i MovieModel
 */

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

    public Movie create(String name, float rating, String fileLink, float personalRating) throws SQLException {
        return movieDAO.createMovie(name, rating, fileLink, personalRating);
    }

    public void delete(Movie movie) {
        movieDAO.deleteMovie(movie);
    }

    public void update(Movie movie) throws SQLException {
        movieDAO.updateMovie(movie);
    }

    public List<Movie> getCategoryMovies(int categoryId) throws SQLException {
      return movieDAO.getCategoryMovie(categoryId);
    }

    public List<Movie> getSearchedMovies(List<Movie> movieList, String query, String filterType) throws SQLException, IOException {
        return filter.search(movieList, query, filterType);
    }
    public void updateMovieTime(Movie movie) throws SQLServerException {
        movieDAO.updateMovieTime(movie);
    }
}
