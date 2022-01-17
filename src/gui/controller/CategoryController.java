package gui.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public abstract class CategoryController {

    public String getName(TextField nameInput){
        if (!nameInput.getText().trim().isEmpty()){
            return nameInput.getText().trim();
        }
        return null;
    }

    public void closeWindow(Button anyButton){
        Stage stage = (Stage) anyButton.getScene().getWindow();
        stage.close();
    }
    public void closeWindow(TextField anyTextField){
        Stage stage = (Stage) anyTextField.getScene().getWindow();
        stage.close();
    }

    public void error(String errorString){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(errorString);
        alert.showAndWait();
    }
}

