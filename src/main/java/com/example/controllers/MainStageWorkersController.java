package com.example.controllers;

import com.example.FXApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainStageWorkersController {

    @FXML
    public void addBooksButton(ActionEvent event){
        FXApp m = new FXApp();
        m.showModalWindow("/makets/AddBook.fxml", event);
    }

    @FXML
    public void showBooksButton(ActionEvent event){
        FXApp m = new FXApp();
        m.showModalWindow("/makets/ViewBooks.fxml", event);
    }

    @FXML
    public void addMemberButton(ActionEvent event){
        FXApp m = new FXApp();
        m.showModalWindow("/makets/AddMembers.fxml", event);
    }

    @FXML
    public void showMembersButton(ActionEvent event){
        FXApp m = new FXApp();
        m.showModalWindow("/makets/ViewMembers.fxml", event);
    }

}
