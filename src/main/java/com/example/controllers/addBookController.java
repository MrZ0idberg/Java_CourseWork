package com.example.controllers;

import com.example.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class addBookController implements Initializable{

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

    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void addBook(ActionEvent event){

    }

    @FXML
    public void cancel(ActionEvent event){

    }


}
