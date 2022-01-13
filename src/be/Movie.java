package be;

import java.util.Date;

public class Movie {

    private int id;
    private String name;
    private float rating;
    private float personalRating;
    private String fileLink;
    private java.sql.Timestamp lastView;

    public Movie(int id, String name, float rating, String fileLink, java.sql.Timestamp lastView, float personalRating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.personalRating = personalRating;
        this.fileLink = fileLink;
        this.lastView = lastView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public float getPersonalRating() {
        return personalRating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPersonalRating(float personalRating) {
        this.personalRating = personalRating;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public java.sql.Timestamp getLastView() {
        return lastView;
    }

    public void setLastView(java.sql.Timestamp lastView) {
        this.lastView = lastView;
    }

    @Override
    public String toString() {
        return name + rating;
    }
}
