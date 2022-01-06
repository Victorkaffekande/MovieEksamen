package dal;

import be.Movie;

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
                    Date lastView = resultSet.getDate("lastView");

                    Movie movie = new Movie(id, name, rating, fileLink, lastView);
                    allMovies.add(movie);
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return allMovies;
    }

    public Movie createMovie(String name, float rating, String filelink, Date lastview) throws SQLException {
        int Id = -1;
        String sql = "INSERT INTO Movie (Name, Rating, filelink, lastview) VALUES (?,?,?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setFloat(2, rating);
            ps.setString(3, filelink);
            ps.setDate(4, lastview);
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



    public List<Movie> getCategoryMovie() throws IOException {
        throw new UnsupportedOperationException();
    }

    public Movie updateMovie() {
        throw new UnsupportedOperationException();
    }



}
