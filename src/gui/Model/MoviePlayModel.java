package gui.Model;

import be.Movie;

/**
 * Model der sætter MovieUrl til anvendelse i controllerne
 */
public class MoviePlayModel {
    public static String movieUrl = "Movies/";
    public static Movie movie;

    public  String getMovieUrl() {
        return movieUrl;
    }

    public static Movie getMovie() {
        return movie;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }
}
