package com.example.database;

import javafx.scene.control.Alert;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class DatabaseHandler {

    private static  DatabaseHandler handler;

    private static Connection conn = null;
    private static Statement stmt = null;

    private boolean checkWork;

    public DatabaseHandler(){
        getConnection();
    }

    /**
     * Считування налаштування та під'єднання до бази даних
     */
    private void getConnection(){

        Properties properties = new Properties();

        try {
            FileInputStream in = new FileInputStream("settings.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String databaseName = properties.getProperty("db.name");
        String databaseUser = properties.getProperty("db.login");
        String databasePassword = properties.getProperty("db.password");
        String databaseAddress = properties.getProperty("db.address");
        String url = "jdbc:mysql://" + databaseAddress + "/" + databaseName;
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            checkWork = true;
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
            checkWork = false;
        }
    }

    /**
     * Перевірка з'єднання з БД
     * @return true - зв'язок є; false - зв'язок відсутній
     */
    public boolean checkConnection(){
        getConnection();
        return checkWork;
    }

    /**
     * Функція виконує запит SQL та повертає результат запиту
     * @param query SQL запит
     * @return результат запиту
     */
    public ResultSet execQuery(String query){
        ResultSet result = null;
        try{
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        return result;
    }

    /**
     * Функція виконує запит SQL та повертає выдомості про завершення запиту
     * @param query SQL запит
     * @return true - запит виконано успішно;
     * false - запит не виконано
     */
    public boolean execAction(String query){
        try{
            stmt = conn.createStatement();
            stmt.execute(query);
            return true;
        }catch (SQLException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Occured");
            alert.setContentText("Error:" + ex.getMessage());
            alert.showAndWait();

            ex.printStackTrace();
            ex.getCause();
            return false;
        }
    }

    /**
     * Перевіряє, чи є данний користувач в системі
     * @param login Логін користувача
     * @param password Пароль користувача
     * @return true - дані є в БД,
     * false - дані відсутні в БД
     */
    public boolean validateLoginDB(String login, String password){

        String verifyLoginWorkers = "SELECT COUNT(1) FROM workers\n" +
                "WHERE login = '" + login + "' AND password = '" + password + "';";
        String verifyLoginReaders = "SELECT COUNT(1) FROM library_reader\n" +
                "WHERE login = '" + login + "' AND password = '" + password + "';";

        boolean checkWorkers = false;
        boolean checkReaders = false;

        try{
            ResultSet queryResult = execQuery(verifyLoginWorkers);

            while (queryResult.next()){
                checkWorkers = queryResult.getBoolean(1);
            }

            queryResult = execQuery(verifyLoginReaders);
            while (queryResult.next()){
                checkReaders = queryResult.getBoolean(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        return checkWorkers || checkReaders;
    }

    /**
     * Внесення книжки в базу даних
     * @param name назва книжки
     * @param author автор книжки
     * @param genre жанр книжки
     * @param department відділ зберігання книги
     * @return true - запит виконано успішно;
     * false - запит не виконано
     */
    public boolean addBook (String name, String author, String genre, String department){
        String query = "INSERT INTO books(name_book, writer, genre_id, department_id)\n" +
                "VALUES (\""  + name +  "\", \"" + author + "\", \"" + genre + "\", \""  + department +"\");";
        return execAction(query);
    }


}
