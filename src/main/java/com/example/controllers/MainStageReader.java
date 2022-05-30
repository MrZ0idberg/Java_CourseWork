package com.example.controllers;

import com.example.FXApp;
import com.example.database.DatabaseHandler;

import javafx.fxml.FXML;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;





public class MainStageReader {

    @FXML
    private StackPane rootPane;


    private FXApp m;
    private DatabaseHandler handler;




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
