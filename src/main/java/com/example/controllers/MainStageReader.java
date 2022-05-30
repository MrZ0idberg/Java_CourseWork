package com.example.controllers;

import com.example.FXApp;
import com.example.database.DatabaseHandler;

import com.example.models.OrderView;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class MainStageReader implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<OrderView> tableView;
    @FXML
    private TableColumn<OrderView, String> idCol;
    @FXML
    private TableColumn<OrderView, String> nameCol;
    @FXML
    private TableColumn<OrderView, String> authorCol;
    @FXML
    private TableColumn<OrderView, String> dateOutCol;
    @FXML
    private TableColumn<OrderView, String> dateReturnCol;

    private FXApp m;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        m = new FXApp();

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
        dateOutCol.setCellValueFactory(cellData -> cellData.getValue().dateOutProperty());
        dateReturnCol.setCellValueFactory(cellData -> cellData.getValue().dateReturnProperty());

    }

    /**
     * Завантаження даних з БД і їх завантаження у табличку у вигляді списку, котрий був сформаваний
     * У базі даних
     */
    private void loadData() {

        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("settings.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String idMember = properties.getProperty("system.userID");

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        tableView.getItems().setAll(databaseHandler.getOrder(idMember));
    }

    /**
     * Обробка події натискання на кнопку cancel і закриття програми
     */
    public void cancelButtonOnAction(){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Обробка події натискання на кнопку cancel і закриття програми
     */
    public void logoutButton(){
        m.changeScene("/makets/Login.fxml");
    }

}
