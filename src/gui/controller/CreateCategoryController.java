package gui.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateCategoryController {

    public TextField nameInput;
    CategoryModel categoryModel;

    public CreateCategoryController() throws IOException {
        categoryModel = new CategoryModel();
    }

    public void handleAcceptButton(ActionEvent actionEvent) throws SQLServerException {
        categoryModel.createCategory(nameInput.getText());
        closeWindow();
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        closeWindow();
    }

    /**
     * closes the window
     */
    private void closeWindow() {
        Stage stage = (Stage) nameInput.getScene().getWindow();
        stage.close();
    }
}
