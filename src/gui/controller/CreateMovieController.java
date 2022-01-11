package gui.controller;

import gui.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CreateMovieController {
    public Button cancelBtn;
    @FXML
    private TextField movieTitleTxt;
    @FXML
    private TextField movieRatingTxt;
    @FXML
    private TextField movieFilePathTxt;
    private MainViewController mainViewController;
    private MovieModel movieModel;

    public CreateMovieController() throws IOException {
        mainViewController = new MainViewController();
        movieModel = new MovieModel();
    }
    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void saveMovieBtn(ActionEvent actionEvent) throws SQLException {
        movieModel.createMovie(movieTitleTxt.getText(), Float.parseFloat(movieRatingTxt.getText()),  movieFilePathTxt.getText());
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void chooseMovieFileBtn(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Movies", "*.mp4", "*.mpeg4"));
        fileChooser.setInitialDirectory(new File("Movies/" ));
        File file = fileChooser.showOpenDialog(null);


        if (file != null) {
            movieFilePathTxt.setText( file.getName());
        }
    }
}
