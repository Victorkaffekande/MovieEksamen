package gui.Model;

import be.Movie;
import bll.MovieManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Vi sætter vores observable list movieList til vores getAllObjects list
 */

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

    public Movie createMovie(String name, float rating, String fileLink, float personalRating) throws SQLException {
        return movieManager.create(name, rating, fileLink, personalRating);
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

    /**
     * Denne metode tjekker om en Movie ikke er blevet afspillet de sidste 2 år og har en personlig rating på 6 eller under
     * @return Liste af film der ikke opfylder kriterierne
     * @throws IOException
     */
    public List<Movie> checkForOldMovies() throws IOException {
        MovieManager movieManager = new MovieManager();
        movieManager.getAllObjects();
        int twoYearsInDays = 730;
        int personalRatingCriteria = 6;
        Date currentDate = new Date();
        long time = currentDate.getTime();
        Timestamp currentTime=new Timestamp(time);
        int millisecondsToDays = 1000*60*60*24;
        List<Movie> tempAllMovies = getObservableMovies();
        List<Movie> badMovies = new ArrayList<>();
        for (Movie movie:tempAllMovies) {
            long timeDiff = (currentTime.getTime() - movie.getLastView().getTime()) / (millisecondsToDays);
            if (timeDiff > twoYearsInDays && movie.getPersonalRating() <= personalRatingCriteria){
                badMovies.add(movie);
            }
        }
        return badMovies;
    }
}
