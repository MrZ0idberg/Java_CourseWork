package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FXApp extends Application   {

    private static Stage stg;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        CheckSettingsFile();

        stg = primaryStage;
        stg.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/makets/Login.fxml"));
        stg.setTitle("Library Friend");
        stg.setScene(new Scene(root));
        File iconFile = new File("src/main/resources/icons/ProgramIcon.png");
        Image icon = new Image(iconFile.toURI().toString());
        stg.getIcons().add(icon);
        stg.show();
    }

    /**
     * Зміна головної сцени на ту, що в fxml файлі
     */
    public void changeScene(String fxml) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(root);
        stg.sizeToScene();
    }

    /**
     * Метод, для створення файлу налаштувань, та задання значень по замовченню
     */
    public static void CheckSettingsFile() throws IOException {
        File file = new File("settings.properties");
        if(!file.exists()){
            file.createNewFile();

            Properties properties = new Properties();
            properties.setProperty("db.login","not set");
            properties.setProperty("db.password","not set");
            properties.setProperty("db.address","not set");
            properties.setProperty("db.name","not set");

            FileOutputStream out = new FileOutputStream("settings.properties");
            properties.store(out, "Create file");
        }


    }


}
