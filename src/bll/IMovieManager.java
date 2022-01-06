package bll;

import be.Movie;
import dal.MovieDAO;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public abstract class IMovieManager implements IManager {

    private MovieDAO movieDAO;
    public IMovieManager() throws IOException {
        movieDAO = new MovieDAO();
    }

    @Override
    public void getAllObjects() {

    }

    public void create(String name, float rating, String fileLink, Timestamp lastView) {

    }

    public void remove(Movie movie) {
    }

    public void update(Movie movie) {

    }
}
