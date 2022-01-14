package gui.Model;

import be.Category;
import be.Movie;
import bll.CategoryManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoryModel {
    private CategoryManager categoryManager;
    private ObservableList <Category> categoryList;

    /**
     * Vi sætter vores observable list movieList til vores getAllObjects list
     * @throws IOException
     */

    public CategoryModel() throws IOException {
        categoryManager = new CategoryManager();
        categoryList = FXCollections.observableArrayList();
        categoryList.addAll(categoryManager.getAllObjects());
    }

    private List<Category> getAllCategories() throws IOException {
        return categoryManager.getAllObjects();
    }

    public ObservableList<Category> getObservableCategories() throws IOException {
        categoryList.clear();
        categoryList.addAll(getAllCategories());
        return categoryList;
    }

    public void createCategory(String name) throws SQLServerException {
        categoryManager.createCategory(name);
    }

    public void deleteCategory(Category category){
        categoryManager.delete(category);
    }

    public void addMovieToCategory(Category category, Movie movie){
        categoryManager.addMovieToCategory(category, movie);
    }

    public void updateCategory(Category categoryUpdate) throws SQLException {
        categoryManager.update(categoryUpdate);
    }
    public ObservableList<Movie> getAllMoviesFromCategoriesObservable(Category category){
        List<Movie> tempMovieList;
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        tempMovieList = this.categoryManager.getALlMoviesFromCategories(category);
        movies.addAll(tempMovieList);
    return movies;
    }
    public void deleteMovieFromCategory(Category category, Movie movie){
        categoryManager.deleteMovieFromCategory(category, movie);
    }
}
