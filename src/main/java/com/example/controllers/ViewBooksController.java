package com.example.controllers;

import com.example.database.DatabaseHandler;
import com.example.models.Books;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Клас-контролер для вікна перегляду книжок
 */
public class ViewBooksController implements Initializable {

    @FXML
    private TableView<Books> TableView;
    @FXML
    private TableColumn<Books, String> idCol;
    @FXML
    private TableColumn<Books, String> nameCol;
    @FXML
    private TableColumn<Books, String> authorCol;
    @FXML
    private TableColumn<Books, String> genreCol;
    @FXML
    private TableColumn<Books, String> departmentCol;
    @FXML
    private TableColumn<Books, String> availableCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    /**
     * Ініціалізація колонок, зіставлення колонок з класом
     */
    private void initCol() {

        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        genreCol.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        departmentCol.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        availableCol.setCellValueFactory(cellData -> cellData.getValue().availableProperty());
    }

    /**
     * Завантаження даних з БД і їх завантаження у табличку у вигляді списку, котрий був сформаваний
     * У базі даних
     */
    private void loadData() {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        TableView.getItems().setAll(databaseHandler.getBooks());
    }
}
