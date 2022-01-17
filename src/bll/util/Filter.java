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
        int[] categoryIds  = getCategoryIds(query);

        for (Movie movie : movieList) {
            if (Objects.equals(filterType,"categoryFilter")){
                if (compareCategory(movie,categoryIds)){
                    result.add(movie);
                }
            }
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


    private boolean compareCategory(Movie movie, int[] categoryIds) throws SQLException {
        if (categoryIds[0] == 0){
            return false;
        }

        try (Connection connection = databaseConnector.getConnection()) {
            for (int cId : categoryIds) {
                String sql = "SELECT * FROM CatMovie WHERE CategoryId =? ";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, cId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    if (movie.getId() == rs.getInt("MovieId")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int[] getCategoryIds(String category) throws SQLException, IOException {
        String[] catArray = category.toLowerCase(Locale.ROOT).trim().split(" ");

        List<Category> allCategories = categoryDAO.getAllCategories();
        List<String> allCatNames = new ArrayList<>();
        for (Category c : allCategories) {
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