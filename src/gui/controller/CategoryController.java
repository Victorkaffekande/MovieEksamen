package gui.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public abstract class CategoryController {
    /**
     * finder texten i det givne tekstfelt
     * @param nameInput navnet på det ønskede tekstfelt
     * @return Stringen fra tekstfeltet trimmet. hvis der kun er
     * mellemrum i tekstfeltet, return null
     */
    public String getName(TextField nameInput){
        if (!nameInput.getText().trim().isEmpty()){
            return nameInput.getText().trim();
        }
        return null;
    }

    /**
     * lukker vinduet
     * @param anyButton en knap der befinder sig i det vindue der skal lukkes
     */
    public void closeWindow(Button anyButton){
        Stage stage = (Stage) anyButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Lukker vinduet
     * @param anyTextField et tekstefelt der befinder sig i det vindue der skal lukkes
     */
    public void closeWindow(TextField anyTextField){
        Stage stage = (Stage) anyTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * åbner et error vindue
     * @param errorString Error teksten der skal vises i alert boksen
     */
    public void error(String errorString){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(errorString);
        alert.showAndWait();
    }
}

