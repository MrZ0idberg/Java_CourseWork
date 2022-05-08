package com.example.controllers;

import com.example.FXApp;
import com.example.database.DatabaseHandler;
import com.example.models.Books;
import com.example.models.Members;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MainStageWorkersController implements Initializable {

    @FXML
    private Text nameBook;
    @FXML
    private Text authorName;
    @FXML
    private Text memberInfo;
    @FXML
    private Text phoneNum;
    @FXML
    private Text bookAvailable;

    @FXML
    private TextField bookIdInfo;
    @FXML
    private TextField memberIdInfo;

    FXApp m;
    DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        m = new FXApp();
        handler = DatabaseHandler.getInstance();
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

    /**
     * Завантаження даних про книгу з БД по ID
     */
    @FXML
    public void loadBookInfo(ActionEvent event){

        String id = bookIdInfo.getText();
        Books books = handler.searchBookInfo(id);

        nameBook.setText(books.getName());
        authorName.setText(books.getAuthor());
        bookAvailable.setText(books.isAvailable());
    }

    /**
     * Завантаження даних про читача з БД по ID
     */
    @FXML
    public void loadMemberInfo(ActionEvent event){

        String memberId = memberIdInfo.getText();
        Members members = handler.searchMemberInfo(memberId);

        memberInfo.setText(members.getName() + " " + members.getSurname());
        phoneNum.setText(members.getPhoneNumber());
    }

}
