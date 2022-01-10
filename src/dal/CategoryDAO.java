
package dal;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private DatabaseConnector databaseConnector;

    public CategoryDAO() throws IOException {
         databaseConnector = new DatabaseConnector();
    }

    public List<Category> getAllCategories() throws IOException {
        List<Category> allCategories = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sqlStatement = "SELECT * FROM Category";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                String name = rs.getString("Name");
                int id = rs.getInt("Id");
                Category category = new Category(id, name);
                allCategories.add(category);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return allCategories;
    }

    public Category createCategory(String categoryName) throws SQLServerException {
        int newID = -1;
        String sql = "INSERT INTO Category(Name) VALUES (?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement prepstatement = connection.prepareStatement(sql);
            prepstatement.setString(1, categoryName);
            prepstatement.addBatch();
            prepstatement.executeBatch();

            sql = "SELECT TOP(1) * FROM Category ORDER by Id desc";
            prepstatement = connection.prepareStatement(sql);
            ResultSet rs = prepstatement.executeQuery();
            while (rs.next()) {
                newID = rs.getInt("id");
            }
            prepstatement.executeBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Category(newID, categoryName);
    }


    public void deleteCategory(Category categoryDelete) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sqLang = "DELETE from CatMovie WHERE CategoryId = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(sqLang);
            preparedStmt.setString(1, Integer.toString(categoryDelete.getId()));
            preparedStmt.execute();

            String sql = "DELETE from Category WHERE Id = ?";
            preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString(1, Integer.toString(categoryDelete.getId()));
            preparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void deleteMovieFromCategory(Category category, Movie movie) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE FROM CatMovie WHERE categoryId = ? AND movieId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, category.getId());
            ps.setInt(2, movie.getId());
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Movie> getAllMoviesFromCategory(Category category) {
        List<Movie> moviesInCategory = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM CatMovie WHERE CategoryId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, category.getId());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int movieId = rs.getInt("MovieId");
                String movieSql = "SELECT top(1) * FROM Movie WHERE ID = ?";
                PreparedStatement psMovie = connection.prepareStatement(movieSql);
                psMovie.setInt(1, movieId);
                ResultSet rSet = psMovie.executeQuery();

                rSet.next();
                int id = rSet.getInt("ID");
                String name = rSet.getString("Name");
                float rating = rSet.getFloat("Rating");
                String filelink = rSet.getString("filelink");
                java.sql.Timestamp timestamp = rSet.getTimestamp("lastview");

                Movie movie = new Movie(id, name, rating, filelink, timestamp);

                moviesInCategory.add(movie);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return moviesInCategory;
    }

    public void addMovieToCategory(Category category, Movie movie){
        String sql = "INSERT INTO CatMovie(CategoryId, MovieId) VALUES (?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, category.getId());
            preparedStatement.setInt(2, movie.getId());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) throws IOException, SQLServerException {
        CategoryDAO categoryDAO = new CategoryDAO();

    }
}