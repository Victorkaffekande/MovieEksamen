package bll.util;

import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.DatabaseConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

public class Filter {
    private DatabaseConnector databaseConnector;

    public Filter() throws IOException {
        databaseConnector = new DatabaseConnector();
    }

    /**
     * compares songList with a String from query
     * @param movieList
     * @param query
     * @return a list of songs containing the String
     */
    public List<Movie> search(List<Movie> movieList, String query) throws SQLException {
        List<Movie> result = new ArrayList<>();

        for (Movie movie : movieList)
        {
            if (compareToTitle(movie, query))
            {
                result.add(movie);
            }
        }
        return result;
    }

    private boolean compareToTitle(Movie movie, String query)
    {
        return movie.getName().toLowerCase().contains(query.toLowerCase());
    }

    private boolean containedInCategory(String query) throws SQLException {
        int[] categoryIds = getCategoryIds(query);
        if (categoryIds != null){
            try(Connection connection = databaseConnector.getConnection()){
                StringBuilder stringBuilder = new StringBuilder("SELECT * FROM CatMovie WHERE CategoryId =");
                for(int i =0; i<categoryIds.length ;i++){
                    stringBuilder.append(categoryIds[i]);
                    if (i != categoryIds.length-1){
                        stringBuilder.append("OR");
                    }
                }


                String sql = stringBuilder.toString();
                Statement ps = connection.createStatement();
                ResultSet rs = ps.executeQuery(sql);
                while (rs.next()){

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return false;
    }


    private int[] getCategoryIds(String category) throws SQLException {
        // if query == category -> find category id
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Category WHERE Name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                rs.last();
                int[] categoryIds = new int[rs.getRow() - 1]; //?? why -1
                rs.first();
                for (int i = 0; i < categoryIds.length; i++) {
                    categoryIds[i] = rs.getInt("Id");
                    rs.next();
                }
                return categoryIds;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}