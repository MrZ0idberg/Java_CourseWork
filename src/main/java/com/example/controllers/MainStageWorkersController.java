package com.example.controllers;

import com.example.FXApp;
import com.example.database.DatabaseHandler;
import com.example.models.Books;
import com.example.models.Members;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;
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

    private FXApp m;
    private DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        m = new FXApp();
        handler = DatabaseHandler.getInstance();
    }

    /*
     * Обробка подій натиснення на кнопки та відкриття модальних вікон
     */
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
    public void loadBookInfo(){

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
    public void loadMemberInfo(){

        String memberId = memberIdInfo.getText();
        Members members = handler.searchMemberInfo(memberId);

        memberInfo.setText(members.getName() + " " + members.getSurname());
        phoneNum.setText(members.getPhoneNumber());
    }

    /**
     * Видача книжки
     */
    public void issueBook(){

        String idBook = bookIdInfo.getText();
        String idMember = memberIdInfo.getText();
        String bookName = nameBook.getText();
        String memberName = memberInfo.getText();

        Alert alert;

        if(!bookAvailable.getText().equals("В наявності")){ //Якщо книги немає в наявності

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText("Вибраної книжки немає в бібліотеці");
            alert.showAndWait();
        } else if (phoneNum.getText().equals("Перевірте ID")){ //Якщо користувач невірний

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Помилка");
            alert.setHeaderText("Не коректний користувач");
            alert.showAndWait();
        } else { //Інакше, виконуємо спробу видати книгу

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Підтвердження операції видачі");
            alert.setContentText("Ви впевнені, що хочете видати книжку " + bookName + "\n читачу " +memberName);
            alert.setHeaderText(null);

            Optional<ButtonType> response = alert.showAndWait();

            if(response.get()==ButtonType.OK) { //Якщо користувач дав відповідь "ОК"
                if (handler.issueBook(idBook, idMember)) { //Якщо запит до БД успішний

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Книжка видана успішно");
                    alert.setContentText(null);
                } else { //Якщо запит до БД з помилкою

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Помилка");
                    alert.setHeaderText("Помилка при передачі запиту в БД");
                }
                alert.showAndWait();

            } else { //Якщо користувач відмовився

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Скасування");
                alert.setHeaderText("Видача книжки скасована");
                alert.showAndWait();
            }
        }
    }

}
