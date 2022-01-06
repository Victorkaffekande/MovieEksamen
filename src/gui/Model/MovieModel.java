package gui.Model;

import be.Movie;
import bll.IMovieManager;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MovieModel {
    private IMovieManager iMovieManager;

    private void getAllMovies() throws IOException {
        iMovieManager.getAllObjects();
    }
    public void createMovie(String name, float rating, String fileLink, Timestamp lastView) throws SQLException {
        iMovieManager.create(name, rating, fileLink, lastView);
    }
    public void removeMovie(Movie movie){
        iMovieManager.remove(movie);
    }
}
