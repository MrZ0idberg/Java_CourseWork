package com.example.controllers;

import com.example.FXApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainStageWorkersController implements Initializable {
    FXApp m = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        m = new FXApp();
    }

    @FXML
    public void addBooksButton(ActionEvent event){
        m.showModalWindow("/makets/AddBook.fxml", event);
    }

    @FXML
    public void showBooksButton(ActionEvent event){
        m.showModalWindow("/makets/ViewBooks.fxml", event);
    }

    @FXML
    public void addMemberButton(ActionEvent event){
        m.showModalWindow("/makets/AddMembers.fxml", event);
    }

    @FXML
    public void showMembersButton(ActionEvent event){
        m.showModalWindow("/makets/ViewMembers.fxml", event);
    }


}
