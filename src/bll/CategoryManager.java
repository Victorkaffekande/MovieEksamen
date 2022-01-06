package bll;

import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.CategoryDAO;

import java.io.IOException;

public class CategoryManager  {
    private CategoryDAO categoryDAO;

    public CategoryManager() throws IOException {
        categoryDAO = new CategoryDAO();
    }

    public void getAllObjects() throws IOException {
        categoryDAO.getAllCategories();
    }

    public void create(String name ) throws SQLServerException {
        categoryDAO.createCategory(name);
    }

    public void remove(Category category) {
        categoryDAO.deleteCategory(category);
    }

    public void update() {

    }
}
