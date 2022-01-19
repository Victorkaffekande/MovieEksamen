package bll.util;

import be.Category;
import be.Movie;
import dal.CategoryDAO;
import dal.DatabaseConnector;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Filter {
    private DatabaseConnector databaseConnector;
    private CategoryDAO categoryDAO;

    public Filter() throws IOException {
        databaseConnector = new DatabaseConnector();
        categoryDAO = new CategoryDAO();
    }

    /**
     * Sammenligner navn, rating eller kategori på hver
     * film i den givne filmliste med det indtastede query
     * @param movieList listen af film objekter der skal sammenlignes
     * @param query søge inputtet
     * @param filterType hvad der skal sammen lignes. "categoryFilter", "movieFilter", "ratingFilter"
     * @return a list of songs containing the String
     */
    public List<Movie> search(List<Movie> movieList, String query, String filterType) throws SQLException, IOException {
        List<Movie> result = new ArrayList<>();
        int[] categoryIds = getCategoryIds(query);

        for (Movie movie : movieList) {
            switch (filterType) {
                case "categoryFilter" -> {
                    if (compareCategory(movie, categoryIds)) {
                        result.add(movie);
                    }
                }

                case "movieFilter" -> {
                    if (compareToTitle(movie, query)) {
                        result.add(movie);
                    }
                }

                case "ratingFilter" -> {
                    if (Objects.equals(query, "")) {
                        query = "0";
                    }
                    if (compareRating(movie, query)) {
                        result.add(movie);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Sammenligner titlen med inputtet
     * @param movie Filmen der undersøges om den fylder kriterierne
     * @param query inputtet fra søgefeltet som der ledes efter
     * @return True hvis filmens titel indeholder søge inputtet, else false
     */
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
            float rating = Float.parseFloat(stringBuilder.toString());
            return movie.getRating() >= rating;
        }
        return false;
    }

    /**
     * Sammenligner kategoi id'et af den givne film med med id'erne fra de søgte kategorier
     * @param movie den film der sammenlignes
     * @param categoryIds id'erne fra de søgte kategorier
     * @return true hvis filmen optræder i den søgte kategori
     * @throws SQLException
     */
    private boolean compareCategory(Movie movie, int[] categoryIds) throws SQLException {
        if (categoryIds[0] == 0) {
            return true;
        }

        try (Connection connection = databaseConnector.getConnection()) {
            for (int cId : categoryIds) {
                String sql = "SELECT * FROM CatMovie WHERE CategoryId =? ";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, cId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (movie.getId() == rs.getInt("MovieId")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finder ud af om det givne input er en kategori
     * @param category søge input
     * @return  Et array af kategori id'er
     * @throws IOException
     */
    private int[] getCategoryIds(String category) throws IOException {
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
}