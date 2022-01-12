package bll;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.CategoryDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoryManager  {
    private CategoryDAO categoryDAO;

    public CategoryManager() throws IOException {
        categoryDAO = new CategoryDAO();
    }

    public List getAllObjects() throws IOException {
       return categoryDAO.getAllCategories();
    }

    public void createCategory(String name ) throws SQLServerException {
        categoryDAO.createCategory(name);
    }

    public void delete(Category category) {
        categoryDAO.deleteCategory(category);
    }

    public void update(Category categoryUpdate) throws SQLException {
        categoryDAO.updateCategory(categoryUpdate);
    }

    public void addMovieToCategory(Category category, Movie movie){
        categoryDAO.addMovieToCategory(category, movie);
    }
    public List<Movie> getALlMoviesFromCategories(Category category){
      return categoryDAO.getAllMoviesFromCategory(category);
    }

    public void deleteMovieFromCategory(Category category, Movie movie){
        categoryDAO.deleteMovieFromCategory(category, movie);
    }

}
