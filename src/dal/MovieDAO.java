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

    /**
     * i getAllMovies metoden får vi data fra databasen, som bruges til
     * at lave et movie objekt, som derefter bliver sat ind i en Arrayliste
     * i metoden, arraylisten er allMovies.
     * @return allMovies som indeholder alle film objekter fra databasen
     * @throws IOException
     */


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
                    float personalRating = resultSet.getFloat("personalRating");
                    String fileLink = resultSet.getString("fileLink");
                    java.sql.Timestamp lastView = resultSet.getTimestamp("lastView");

                    Movie movie = new Movie(id, name, rating, fileLink, lastView, personalRating);// Laver et movie objekt
                    allMovies.add(movie);// indsætter movie objekt i Arraylisten


                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return allMovies;
    }

    /**
     *
     * @param name navnet / titlen af filmen (String)
     * @param rating Bedømmelsen, som brugeren giver filmen (float)
     * @param filelink filelinket (String)
     *
     *        createMovie metoden laver et film objekt i vores database.
     *        id bliver incremented af vores database og sendt tilbage til
     *        applikationen. Id'et bliver forøget med 1 hver gang et nyt objekt
     *        bliver tilføjet.
     *
     * @return Det nye film objekt
     * @throws SQLException
     */

    public Movie createMovie(String name, float rating, String filelink, float personalRating) throws SQLException {
        int Id = -1;
        java.sql.Timestamp lastview = new java.sql.Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO Movie (Name, Rating, filelink, lastview, personalRating) VALUES (?,?,?,?,?)";
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setFloat(2, rating);
            ps.setString(3, filelink);
            ps.setTimestamp(4, lastview);
            ps.setFloat(5, personalRating);
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
        return new Movie(Id, name, rating, filelink, lastview, personalRating);
    }

    /**
     *
     * @param movieDelete Denne metode sletter først det valgte movie objekt fra alle categorier og bagefter selve movien fra databasen.
     */
    public void deleteMovie(Movie movieDelete) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "Delete from CatMovie WHERE MovieId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,movieDelete.getId());
            ps.execute();

            String sql2 = "DELETE from Movie WHERE Id = ?";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.setInt(1, movieDelete.getId());
            ps2.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    /**
     *
     * @param categoryId category Id
     *
     *                   innerjoiner Movie og Category columns i databasen ved at
     *                   innerjoine dem i en tredje column, som er CatMovie.
     *
     * @return returnerer allMovies Arraylisten
     * @throws SQLException
     */
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
                    float personalRating = resultSet.getFloat("personalRating");
                    int id = resultSet.getInt("id");
                    Movie movie = new Movie(id, name, rating, filelink, lastview, personalRating);
                    allMovies.add(movie);
                }
            }

        }
            catch (SQLException throwables) {
            throwables.printStackTrace();
            }
        return allMovies;
    }

    /**
     *
     * @param movieUpdate Metoden her bruges, når man vil redigere
     *                    et film objekt ved brug af SQL UPDATE statement.
     *                    Overskriver det allerede eksisterende film objekt.
     * @throws SQLException
     */
    public void updateMovie(Movie movieUpdate) throws SQLException{
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Movie SET Name=?, Rating=?, filelink=?, lastview=?, personalRating=? WHERE Id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, movieUpdate.getName());
            ps.setFloat(2, movieUpdate.getRating());
            ps.setString(3, movieUpdate.getFileLink());
            ps.setTimestamp(4, movieUpdate.getLastView());
            ps.setFloat(5, movieUpdate.getPersonalRating());
            ps.setInt(6, movieUpdate.getId());
            if (ps.executeUpdate() != 1) {
                throw new Exception("Could not update Movie");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param movie film objektet
     *        updateMovieTime opdaterer, hvornår et film objekt sidst
     *        er blevet set under lastview.
     * @throws SQLServerException
     */

    public void updateMovieTime(Movie movie) throws SQLServerException {
        java.sql.Timestamp lastview = new java.sql.Timestamp(System.currentTimeMillis());
        try(Connection connection = databaseConnector.getConnection()){
            String sql = "UPDATE MOVIE SET lastview=? WHERE Id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, lastview);
            ps.setInt(2, movie.getId());
            if(ps.executeUpdate() != 1){
                throw new Exception("Could not update movie time");
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
}
