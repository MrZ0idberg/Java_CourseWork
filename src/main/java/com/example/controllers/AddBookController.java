package com.example.controllers;

import com.example.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Клас-контроллер для вікна додавання книги
 */
public class AddBookController implements Initializable{

    @FXML
    private TextField id;
    @FXML
    private TextField author;
    @FXML
    private TextField name;
    @FXML
    private TextField genre;
    @FXML
    private TextField department;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane rootPane;

    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = new DatabaseHandler();
    }

    /**
     *Додавання книги в БД
     */
    @FXML
    public void addBook(ActionEvent event){
        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookName = name.getText();
        String bookGenre = genre.getText();
        String bookDepartment = department.getText();

        boolean isEmptyTextField = bookID.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty()
                || bookGenre.isEmpty() || bookDepartment.isEmpty();

        if(!isEmptyTextField){
            if(databaseHandler.addBook(bookAuthor, bookName, bookGenre, bookDepartment)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Книжку успішно додано до БД");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Помилка при передачі запиту до БД");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Заповніть всі поля");
            alert.showAndWait();
        }
    }

    /**
     * Закриття вікна
     */
    @FXML
    public void cancel(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}
