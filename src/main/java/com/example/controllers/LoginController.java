package com.example.controllers;

import com.example.DatabaseConnection;
import com.example.FXApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMassageLabel;
    @FXML
    private ImageView logoImg;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;

    /*
     * Ініціалізація вікна для входу
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        //Считування зображення з файлу і запис його у ImageView
        File imgFile = new File("src/main/resources/icons/logoImg.jpg");
        Image img = new Image(imgFile.toURI().toString());
        logoImg.setImage(img);

    }

    /*
     * Обробка події натискання на кнопку cancel і закриття програми
     */
    public void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /*
     * Обробка події настискання на кнопку login і перевірка введених даних
     */
    public void loginButtonOnAction(ActionEvent event) throws IOException {

        if(!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()){
            validateLogin();
        }else{
            loginMassageLabel.setText("Введіть логін/пароль");
        }
    }

    /*
     * Перевірка комбінації логіну/паролю
     */
    public void validateLogin() throws IOException {
        FXApp m = new FXApp();

        DatabaseConnection connDB = new DatabaseConnection();

        String login = usernameTextField.getText();
        String password = passwordTextField.getText();

        if(!connDB.checkConnection()) {
            loginMassageLabel.setText("Відсутній зв'язок з ДП, перевірте налаштування");
        }else if (connDB.validateLoginDB(login, password)){
            m.changeScene("/makets/MainStage.fxml");
        }else{
            loginMassageLabel.setText("Невірка комбінація логіну/паролю");
        }
    }

    /*
     * Обробка події настискання на кнопку налаштувань БД
     */
    public void settingDBButtonOnAction(ActionEvent event) throws IOException {
        FXApp m = new FXApp();
        m.changeScene("/makets/LoginSettingDB.fxml");
    }



}
