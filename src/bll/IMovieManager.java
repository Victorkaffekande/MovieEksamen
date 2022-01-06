package bll;

import be.Movie;
import dal.MovieDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public abstract class IMovieManager implements IManager {

    private MovieDAO movieDAO;
    public IMovieManager() throws IOException {
        movieDAO = new MovieDAO();
    }

    @Override
    public void getAllObjects() throws IOException {
        movieDAO.getAllMovies();
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
