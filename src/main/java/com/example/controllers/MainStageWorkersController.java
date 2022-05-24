package com.example.controllers;

import com.example.FXApp;
import com.example.database.DatabaseHandler;
import com.example.models.Books;
import com.example.models.Members;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
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
    @FXML
    private TextField bookIdSecondTAB;

    @FXML
    private ListView<String> listBookAndMember;

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
    public void addBooksButton(ActionEvent event){
        m.showModalWindow("/makets/AddBook.fxml", event);
    }

    public void showBooksButton(ActionEvent event){
        m.showModalWindow("/makets/ViewBooks.fxml", event);
    }

    public void addMemberButton(ActionEvent event){
        m.showModalWindow("/makets/AddMembers.fxml", event);
    }

    public void showMembersButton(ActionEvent event){
        m.showModalWindow("/makets/ViewMembers.fxml", event);
    }

    /**
     * Завантаження даних про книгу з БД по ID
     */
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

            } else { //Якщо користувач відмовився

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Скасування");
                alert.setHeaderText("Видача книжки скасована");
            }
            alert.showAndWait();
        }
    }

    /**
     * Завантаження даних про книгу і про користувача, у кого вона
     */
    public void loadBookInfoAndMember(){

        String bookID = bookIdSecondTAB.getText();

        ObservableList<String> info = handler.loadBookAndMemberInfo(bookID);

        listBookAndMember.getItems().setAll(info);
    }

    /**
     * Операція повернення книги
     */
    public void returnBookButton(){

        String bookID = bookIdSecondTAB.getText();

        Alert alert;

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Підтвердження операції видачі");
        alert.setContentText("Ви впевнені, що хочете повернути книжку? ");
        alert.setHeaderText(null);

        Optional<ButtonType> response = alert.showAndWait();

        if(response.get()==ButtonType.OK) {//Якщо користувач дав відповідь "ОК"
            if (handler.searchRecordID(bookID) != null) {//Якщо є запис в БД (книга видана і не повернена)
                if (handler.returnBook(bookID)) {

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Книжка повернена успішно");
                    alert.setContentText(null);
                } else {//Якщо запит до БД з помилкою

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Помилка");
                    alert.setHeaderText("Помилка при передачі запиту в БД");
                }
            } else {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Помилка");
                alert.setHeaderText("Помилка при передачі запиту в БД");
            }
        } else {//Якщо користувач відмовився

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Скасування");
            alert.setHeaderText("Повернення книги скасовано");
        }

        alert.showAndWait();
    }



}
