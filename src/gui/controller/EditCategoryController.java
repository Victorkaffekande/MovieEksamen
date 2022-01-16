package gui.controller;

import be.Category;
import bll.CategoryManager;
import gui.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EditCategoryController {
    @FXML
    private Button acceptCategoryBtn;
    @FXML
    private TextField enterCategoryName;
    @FXML
    private Button newCategoryCancelEdit;
    private int id;

   private CategoryModel categoryModel;

    private CategoryManager categoryManager;

    public EditCategoryController() throws IOException {
        categoryModel = new CategoryModel();
    }

    /**
     * handleAcceptBtn "Accept-knappen", som overwriter det gamle navn/titel tilknyttet Category objektet med det
     * nye navn/titel
     * @param actionEvent javaFX event klasse
     * @throws SQLException
     */

    public void handleAcceptBtn(ActionEvent actionEvent) throws SQLException {
        if (!enterCategoryName.getText().isEmpty()) {
            Category categoryUpdate = new Category(id, enterCategoryName.getText());

            categoryModel.updateCategory(categoryUpdate);


            Stage stage = (Stage) acceptCategoryBtn.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("fill out all textfields");
            alert.showAndWait();
        }
    }

        public void setCategory (Category category)
        {
            id = category.getId();
            enterCategoryName.setText(category.getName());
        }


    /**
     * handleCancelBtn Cancel-Knappen, som lukker "gui/view/EditCategory.fxml"
     * @param actionEvent javaFX event klasse
     */

    public void handleCancelBtn (ActionEvent actionEvent){
            Stage stage = (Stage) newCategoryCancelEdit.getScene().getWindow();
            stage.close();
        }
    }

