package be;

import java.util.Date;

public class Movie {

    private int id;
    private String name;
    private float rating;
    private String fileLink;
    private java.sql.Date lastView;

    public Movie(int id, String name, float rating, String fileLink, java.sql.Date lastView) {
        this.id = id;
        this.name = name;
        this.rating = rating;
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

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public java.sql.Date getLastView() {
        return lastView;
    }

    public void setLastView(java.sql.Date lastView) {
        this.lastView = lastView;
    }
}
