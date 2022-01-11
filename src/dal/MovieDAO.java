package dal;

import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import java.util.List;

public class MovieDAO {
    private DatabaseConnector databaseConnector;

    public MovieDAO() throws IOException {
        this.databaseConnector = new DatabaseConnector();
    }

    public List<Movie> getAllMovies() throws IOException {
        ArrayList<Movie> allMovies = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection()) {
            String sqlStatement = "SELECT * FROM Movie";
            Statement statement = connection.createStatement();
            if (statement.execute(sqlStatement)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    float rating = resultSet.getFloat("rating");
                    String fileLink = resultSet.getString("fileLink");
                    java.sql.Timestamp lastView = resultSet.getTimestamp("lastView");
                    Movie movie = new Movie(id, name, rating, fileLink, lastView);
                    allMovies.add(movie);
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return allMovies;
    }

    public Movie createMovie(String name, float rating, String filelink) throws SQLException {
        int Id = -1;
        java.sql.Timestamp lastview = new java.sql.Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO Movie (Name, Rating, filelink, lastview) VALUES (?,?,?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setFloat(2, rating);
            ps.setString(3, filelink);
            ps.setTimestamp(4, lastview);
            ps.addBatch();
            ps.executeBatch();

            sql = "SELECT TOP(1) * FROM Movie ORDER by Id desc";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Id = resultSet.getInt("id");
            }

            ps.executeBatch();
        }
        return new Movie(Id, name, rating, filelink, lastview);
    }

    public void deleteMovie(Movie movieDelete) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE from Movie WHERE Id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, Integer.toString(movieDelete.getId()));
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }



    public List<Movie> getCategoryMovie(int categoryId) throws SQLException {
        ArrayList<Movie> allMovies = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Movie m " +
            "       innter join CatMovie cm on cm.MovieId = m.id" +
            "       where cm.categoryID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);

            if (preparedStatement.execute())
            {
                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    float rating = resultSet.getFloat("rating");
                    String filelink = resultSet.getString("filelink");
                    Timestamp lastview = resultSet.getTimestamp("lastview");
                    int id = resultSet.getInt("id");
                    Movie movie = new Movie(id, name, rating, filelink, lastview);
                    allMovies.add(movie);
                }
            }

        }
            catch (SQLException throwables) {
            throwables.printStackTrace();
            }
        return allMovies;
    }

    public void updateMovie(Movie movieUpdate) throws SQLException{
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Movie SET Name=?, Rating=?, filelink=?, lastview=? WHERE Id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, movieUpdate.getName());
            ps.setFloat(2, movieUpdate.getRating());
            ps.setString(3, movieUpdate.getFileLink());
            ps.setTimestamp(4, movieUpdate.getLastView());
            ps.setInt(5, movieUpdate.getId());
            if (ps.executeUpdate() != 1) {
                throw new Exception("Could not update Movie");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        MovieDAO movieDAO = new MovieDAO();
    }

}
