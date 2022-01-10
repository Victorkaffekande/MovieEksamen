package gui.Model;

import be.Category;
import bll.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class CategoryModel {
    private CategoryManager categoryManager;
    private ObservableList <Category> categoryList;

    public CategoryModel() throws IOException {
        categoryManager = new CategoryManager();
        categoryList = FXCollections.observableArrayList();
        categoryList.addAll(categoryManager.getAllObjects());
    }

    private List<Category> getAllCategories() throws IOException {
        return categoryManager.getAllObjects();
    }

    public ObservableList<Category> getObservableCategories() throws IOException {
        categoryList.clear();
        categoryList.addAll(getAllCategories());
        return categoryList;
    }

}