package com.example.controllers;

import com.example.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddMembersController implements Initializable {

    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField phone_number;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane rootPane;

    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();
    }

    /**
     *Додавання читача в БД
     */
    @FXML
    public void addMember(ActionEvent event){
        String memberLogin = login.getText();
        String memberPassword = password.getText();
        String memberName = name.getText();
        String memberSurname = surname.getText();
        String memberPhone_number = phone_number.getText();

        boolean isEmptyTextField = memberLogin.isEmpty() || memberPassword.isEmpty() || memberName.isEmpty()
                || memberSurname.isEmpty() || memberPhone_number.isEmpty();

        //Якщо поля не пусті, то продовжуємо, інакше повідомляємо про це користувача
        if(!isEmptyTextField){
            //Якщо користувача немає в системі, продовжуємо, інакше повідомляємо про це
            if(!databaseHandler.checkAvailableUser(memberLogin)) {
                //Якщо додавання успішне, повідомляємо, інакше повідомляємо про помилку
                if (databaseHandler.addMember(memberLogin, memberPassword, memberName, memberSurname, memberPhone_number)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Читача успішно додано до БД");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Помилка при передачі запиту до БД");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Користувач з таким логіном присутній у системі");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Заповніть всі поля");
            alert.showAndWait();
        }
    }

    /**
     * Закриття вікна
     */
    @FXML
    public void cancel(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}
