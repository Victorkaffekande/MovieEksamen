package gui.Model;

import bll.CategoryManager;

import java.io.IOException;

public class CategoryModel {
    private CategoryManager categoryManager;

    public CategoryModel() throws IOException {
        categoryManager = new CategoryManager();
    }

}
