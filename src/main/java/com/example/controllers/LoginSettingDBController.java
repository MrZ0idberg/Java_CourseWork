package com.example.controllers;

import com.example.DatabaseConnection;
import com.example.FXApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class LoginSettingDBController implements Initializable{

    @FXML
    public TextField loginDBTextField;
    @FXML
    public PasswordField passField;
    @FXML
    public TextField nameDBTextField;
    @FXML
    public TextField addressDBTextField;
    @FXML
    public Label informLabel;

    /**
     * Ініціалізація вікна для входу
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        String login;
        String pass;
        String address;
        String name;

        /*
         * Початкове завантаження значень з налаштувань в текстові поля програми
         */
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("settings.properties");
            properties.load(in);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Отримання налаштувань з файлу
        login = properties.getProperty("db.login");
        pass = properties.getProperty("db.password");
        address = properties.getProperty("db.address");
        name = properties.getProperty("db.name");

        //Встановлення тексту в комірках
        loginDBTextField.setText(login);
        passField.setText(pass);
        nameDBTextField.setText(name);
        addressDBTextField.setText(address);
    }

    /**
     * Обробка події натискання на кнопку збереження
     * Збеження данних в файлі конфігурації
     */
    public void saveButtonOnAction(ActionEvent event){
        String login;
        String pass;
        String address;
        String name;

        Properties properties = new Properties();

        try {
            FileInputStream in = new FileInputStream("settings.properties");
            properties.load(in);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Отримання значень з текстових полів
        login = loginDBTextField.getText();
        pass = passField.getText();
        address = addressDBTextField.getText();
        name = nameDBTextField.getText();

        //Запис значень, в об'єкт конфігурації
        properties.setProperty("db.login",login);
        properties.setProperty("db.password",pass);
        properties.setProperty("db.address",address);
        properties.setProperty("db.name",name);

        //Запис значень в файл
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("settings.properties");
            properties.store(out, "Update");
            informLabel.setText("Інформацію успішно збережено");
        } catch (IOException e) {
            e.printStackTrace();
            informLabel.setText("Виникла помилка, зламайте руки тому, хто це робив");
        }

    }

    /**
     * Перевірка з'єднання з БД
     */
    public void checkButtonOnAction(ActionEvent event){
        DatabaseConnection conn = new DatabaseConnection();
        if(conn.checkConnection()){
            informLabel.setText("Зв'язок з БД успішно встановлений");
        } else {
            informLabel.setText("Зв'язок з БД не встановлено, перевірте уведені дані");
        }

    }

    /**
     * Повернення на початковий екран
     */
    public void backButtonOnAction(ActionEvent event) throws IOException {
        FXApp m = new FXApp();
        m.changeScene("/makets/Login.fxml");
    }

}
