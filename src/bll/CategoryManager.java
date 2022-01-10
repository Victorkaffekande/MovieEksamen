package bll;

import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.CategoryDAO;

import java.io.IOException;
import java.util.List;

public class CategoryManager  {
    private CategoryDAO categoryDAO;

    public CategoryManager() throws IOException {
        categoryDAO = new CategoryDAO();
    }

    public List getAllObjects() throws IOException {
       return categoryDAO.getAllCategories();
    }

    public void create(String name ) throws SQLServerException {
        categoryDAO.createCategory(name);
    }

    public void delete(Category category) {
        categoryDAO.deleteCategory(category);
    }

    public void update() {

    }
}
