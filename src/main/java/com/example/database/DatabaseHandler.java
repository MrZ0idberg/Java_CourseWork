package com.example.database;

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
     * Метод для під'єднання до бази даних
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

    public boolean checkConnection(){
        getConnection();
        return checkWork;
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
            Statement statement = conn.createStatement();

            ResultSet queryResult = statement.executeQuery(verifyLoginWorkers);
            while (queryResult.next()){
                checkWorkers = queryResult.getBoolean(1);
            }

            queryResult = statement.executeQuery(verifyLoginReaders);
            while (queryResult.next()){
                checkReaders = queryResult.getBoolean(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        return checkWorkers || checkReaders;
    }



}
