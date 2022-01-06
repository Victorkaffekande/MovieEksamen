package gui.Model;

import be.Movie;
import bll.MovieManager;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MovieModel {
    private MovieManager movieManager;

    private void getAllMovies() throws IOException {
        movieManager.getAllObjects();
    }
    public void createMovie(String name, float rating, String fileLink, Timestamp lastView) throws SQLException {
        movieManager.create(name, rating, fileLink, lastView);
    }
    public void removeMovie(Movie movie){
        movieManager.remove(movie);
    }
}
