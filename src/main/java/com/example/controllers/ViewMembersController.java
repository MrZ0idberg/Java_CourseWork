package com.example.controllers;

import com.example.database.DatabaseHandler;
import com.example.models.Members;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewMembersController implements Initializable{

    @FXML
    private TableView<Members> TableView;
    @FXML
    private TableColumn<Members, String> idCol;
    @FXML
    private TableColumn<Members, String> loginCol;
    @FXML
    private TableColumn<Members, String> nameCol;
    @FXML
    private TableColumn<Members, String> surnameCol;
    @FXML
    private TableColumn<Members, String> phoneCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();

    }

    /**
     * Ініціалізація колонок, співставлення колонок до класу
     */
    private void initCol() {

        idCol.setCellValueFactory(cellData -> cellData.getValue().idReadingTicketProperty());
        loginCol.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        surnameCol.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
    }

    /**
     * Завантаження даних з БД і їх завантаження у табличку у вигляді списку, котрий був сформаваний
     * У базі даних
     */
    private void loadData() {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        TableView.getItems().setAll(databaseHandler.getMembers());
    }

}
