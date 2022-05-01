package com.example.controllers;

import com.example.FXApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainStageWorkersController {


    @FXML
    public void addBooksButton(ActionEvent event){
        FXApp m = new FXApp();
        m.showModalWindow("/makets/LoginSettingDB.fxml", event);//TODO Лол, зробити посилання на фхмл файлик, котрий рево робить
    }

    @FXML
    public void showBooksButton(ActionEvent event){
        FXApp m = new FXApp();
        m.showModalWindow("makets/ViewBooks.fxml", event);
    }

}
