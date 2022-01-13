package bll.util;

import be.Category;
import be.Movie;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.CategoryDAO;
import dal.DatabaseConnector;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.function.ToDoubleBiFunction;

public class Filter {
    private DatabaseConnector databaseConnector;
    private CategoryDAO categoryDAO;

    public Filter() throws IOException {
        databaseConnector = new DatabaseConnector();
        categoryDAO = new CategoryDAO();
    }

    /**
     * compares songList with a String from query
     *
     * @param movieList
     * @param query
     * @return a list of songs containing the String
     */
    public List<Movie> search(List<Movie> movieList, String query, String filterType) throws SQLException, IOException {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movieList) {
            if (Objects.equals(filterType, "movieFilter")) {
                if (compareToTitle(movie, query)) {
                    result.add(movie);
                }
            }
            if (Objects.equals(filterType, "ratingFilter")) {
                if (Objects.equals(query, "")) {
                    query = "0";
                }
                if (compareRating(movie, query)) {
                    result.add(movie);
                }
            }
        }
        if (Objects.equals(filterType, "categoryFilter")) {
            if (Objects.equals(query, "")){
                result.addAll(movieList);
            }
            result.addAll(containedInCategory(query));
        }
        return result;
    }


    private boolean compareToTitle(Movie movie, String query) {
        return movie.getName().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareRating(Movie movie, String query) {
        //finds all the numbers in the input string
        char[] chars = query.toLowerCase(Locale.ROOT).toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : chars) {
            if (Character.isDigit(c) || c == '.') {
                stringBuilder.append(c);
            }
        }
        //compares movie rating to the input
        if (!stringBuilder.isEmpty()) {
            double rating = Double.parseDouble(stringBuilder.toString());
            return movie.getRating() >= rating;
        }
        return false;
    }

    ///TODO fix category search
    private List<Movie> containedInCategory(String query) throws SQLException, IOException {
        List<Movie> moviesInCategory = new ArrayList<>();
        int[] categoryIds = getCategoryIds(query);
        if (categoryIds[0] != 0) {
            for (int i : categoryIds) {
                try (Connection connection = databaseConnector.getConnection()) {
                    String sql = "SELECT * FROM CatMovie WHERE CategoryId =? ";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, i);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String sql2 = "SELECT top(1) * from movie where Id = ?";
                        PreparedStatement ps2 = connection.prepareStatement(sql2);
                        ps2.setInt(1, rs.getInt("MovieId"));
                        ResultSet rs2 = ps2.executeQuery();
                        while (rs2.next()) {
                            int id = rs2.getInt("ID");
                            String name = rs2.getString("Name");
                            float rating = rs2.getFloat("Rating");
                            String filelink = rs2.getString("filelink");
                            java.sql.Timestamp timestamp = rs2.getTimestamp("lastview");
                            float personalRating = rs2.getFloat("personalRating");

                            Movie movie = new Movie(id, name, rating, filelink, timestamp,personalRating);
                                moviesInCategory.add(movie);
                        }
                    }
                }
            }

        }
        return moviesInCategory;
    }

    private int[] getCategoryIds(String category) throws SQLException, IOException {
        String[] catArray = category.toLowerCase(Locale.ROOT).trim().split(" ");

        List<Category> allCats = categoryDAO.getAllCategories();
        List<String> allCatNames = new ArrayList<>();
        for (Category c : allCats) {
            allCatNames.add(c.getName().toLowerCase());
        }
        int[] categoryIds = new int[catArray.length];
        for (int i = 0; i < catArray.length; i++) {
            if (allCatNames.contains(catArray[i])) {
                try (Connection connection = databaseConnector.getConnection()) {
                    String sql = "SELECT * FROM Category WHERE Name = ?";
                    PreparedStatement ps = connection.prepareStatement(sql);

                    ps.setString(1, catArray[i]);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    categoryIds[i] = rs.getInt("Id");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return categoryIds;
    }

    public static void main(String[] args) {
        System.out.println("'test'");
    }
}