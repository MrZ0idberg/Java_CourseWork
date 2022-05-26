package com.example.controllers;

import com.example.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Клас-контролер для вікна додавання книги
 */
public class AddBookController implements Initializable{


    @FXML
    private TextField author;
    @FXML
    private TextField name;

    @FXML
    private ComboBox<String> genre;
    @FXML
    private ComboBox<String> department;

    @FXML
    private AnchorPane rootPane;

    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        databaseHandler = DatabaseHandler.getInstance();
        genre.setItems(databaseHandler.getListGenre());
        department.setItems(databaseHandler.getListDepartment());
    }

    /**
     *Додавання книги в БД
     */
    @FXML
    public void addBook(){

        String bookAuthor = author.getText();
        String bookName = name.getText();
        String bookGenre = genre.getValue();
        String bookDepartment = department.getValue();

        boolean isEmptyTextField = bookAuthor.isEmpty() || bookName.isEmpty()
                || bookGenre.isEmpty() || bookDepartment.isEmpty();

        if(!isEmptyTextField){

            //Пошук ID того, що було отримано зі списку, що випадає
            String idGenre = databaseHandler.searchIdGenre(bookGenre);
            String idDepartment = databaseHandler.searchIdDepartment(bookDepartment);

            if(databaseHandler.addBook(bookName, bookAuthor, idGenre, idDepartment)){
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
    public void cancel(){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}
