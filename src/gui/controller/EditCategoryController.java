package gui.controller;

import be.Category;
import gui.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.sql.SQLException;

public class EditCategoryController extends CategoryController {
    @FXML
    private Button acceptCategoryBtn;
    @FXML
    private TextField enterCategoryName;
    @FXML
    private Button newCategoryCancelEdit;
    private int id;

    private CategoryModel categoryModel;

    public EditCategoryController() throws IOException {
        categoryModel = new CategoryModel();
    }

    /**
     * handleAcceptBtn "Accept-knappen", som overwriter det gamle navn/titel tilknyttet Category objektet med det
     * nye navn/titel
     *
     * @param actionEvent javaFX event klasse
     * @throws SQLException
     */

    public void handleAcceptBtn(ActionEvent actionEvent) throws SQLException {
        if (!getName(enterCategoryName).isEmpty()) {
            Category categoryUpdate = new Category(id, getName(enterCategoryName));
            categoryModel.updateCategory(categoryUpdate);
            closeWindow(acceptCategoryBtn);
        } else {
            error("please input a name");
        }
    }

    /**
     * Sætter start teksten i navne inputtet til det nuværende navn
     * @param category den nuværende kategori
     */
    public void setCategory(Category category) {
        id = category.getId();
        enterCategoryName.setText(category.getName());
    }

    /**
     * handleCancelBtn Cancel-Knappen, som lukker "gui/view/EditCategory.fxml"
     *
     * @param actionEvent javaFX event klasse
     */
    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(newCategoryCancelEdit);
    }
}

