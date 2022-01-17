package gui.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import java.io.IOException;

public class CreateCategoryController extends CategoryController {

    @FXML
    private TextField nameInput;
    private CategoryModel categoryModel;

    public CreateCategoryController() throws IOException {
        categoryModel = new CategoryModel();
    }

    /**
     * handleAcceptButton opretter det nye Category objekt
     * @param actionEvent javaFX event klasse
     * @throws SQLServerException
     */

    public void handleAcceptButton(ActionEvent actionEvent) throws SQLServerException {
        if (!getName(nameInput).isEmpty()){
            categoryModel.createCategory(getName(nameInput));
            closeWindow(nameInput);
        }else{
            error("Input name");
        }
    }

    /**
     * handleCancelButton Cancel-Knappen, som lukker "gui/view/CreateCategory.fxml"
     * @param actionEvent javaFX event klasse
     */

    public void handleCancelButton(ActionEvent actionEvent) {
        closeWindow(nameInput);
    }


}
