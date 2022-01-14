
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

    /**
     *      i getAllCategories metoden får vi data fra databasen, som bruges til
     *      at lave et category objekt, som derefter bliver sat ind i en liste
     *      i metoden, listen er allCategories.
     * @return allCategories listen
     * @throws IOException
     */

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

    /**
     *
     * @param categoryName navnet / titlen af kategorien (String)
     *              createCategory metoden laver et category objekt i vores database.
     *              id bliver incremented af vores database og sendt tilbage til
     *              applikationen. Id'et bliver forøget med 1 hver gang et nyt objekt
     *              bliver tilføjet.
     * @return det nye kategori objekt
     * @throws SQLServerException
     */

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

    /**
     *
     * @param categoryUpdate Metoden her bruges, når man vil redigere
     *                       et kategori objekt ved brug af SQL UPDATE statement.
     *                       Overskriver det allerede eksisterende kategori objekt.
     * @throws SQLException
     */

    public void updateCategory(Category categoryUpdate) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()){
            String sql = "UPDATE Category SET Name=? WHERE Id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, categoryUpdate.getName());
            preparedStatement.setInt(2, categoryUpdate.getId());
            if (preparedStatement.executeUpdate() != 1) {
                throw new Exception("Could not update Category");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param categoryDelete denne metode sletter valgte kategori objekt
     *                       fra databasen.
     */

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


    /**
     * Denne metode sletter en film fra en kategori
     * @param category Kategori objektet film objektet bliver slettet fra
     * @param movie Det valgte film objekt
     */

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


    /**
     * Denne metode viser alle Film i en specifik kategori ved at
     * vise et ResultSet, som viser alle MovieId's på i den valgte kategori.
     * For hver eneste linje i ResultSet vil et nye movie objekt blive dannet
     * ud fra movieId og blive tilføjet til listen, som bliver returneret.
     * @param category Specifikke kategori objekt
     * @return en liste kaldt moviesInCategory, som indeholder alle film i den valgte kategori.
     * som er "assigned" til den valgte kategori.
     */

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
                float personalRating = rSet.getFloat("personalRating");

                Movie movie = new Movie(id, name, rating, filelink, timestamp, personalRating);

                moviesInCategory.add(movie);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return moviesInCategory;
    }

    /**
     * addMovieToCategory tilføjer den valgte film til den valgte kategori
     * ved at koble den valgte films MovieId til den valgte kategoros CategoryId.
     * @param category Den valgte kategori.
     * @param movie Den valgte film.
     */

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
}