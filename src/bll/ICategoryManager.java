package bll;

import be.Category;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.CategoryDAO;

import java.io.IOException;

public abstract class ICategoryManager implements IManager {
    private CategoryDAO categoryDAO;

    public ICategoryManager() throws IOException {
        categoryDAO = new CategoryDAO();
    }
    @Override
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
