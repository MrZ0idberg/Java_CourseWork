package com.example.database;

import com.example.models.Books;
import com.example.models.Members;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Клас для обробки всіх звернень до бази даних
 */
public class DatabaseHandler {

    private static  DatabaseHandler handler = null;

    private static Connection conn = null;
    private static Statement stmt = null;

    private boolean checkWork;

    private DatabaseHandler(){
        getConnection();
    }

    public static  DatabaseHandler getInstance(){
        if(handler == null){
            handler = new DatabaseHandler();
        }
        return handler;
    }

    /**
     * Зчитування налаштування та під'єднання до бази даних
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
            alert.setHeaderText("Error Occurred");
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

    /**
     * Внесення читача в базу даних
     * @param login логін читача
     * @param password пароль читача
     * @param name ім'я читача
     * @param surname прізвище читача
     * @param phone_number номер читача
     * @return true - запит виконано успішно;
     * false - запит не виконано
     */
    public boolean addMember (String login, String password, String name, String surname, String phone_number){
        String query = "INSERT INTO library_reader(login, password, name, last_name, phone_num)\n" +
                "VALUES ('" + login + "', '" + password + "', '" + name + "', '" + surname + "', '" + phone_number + "')\n;";
        return execAction(query);
    }

    /**
     * Виконання запиту до БД і заповнення списку інформацією про книжки
     * @return список з інформацією, готовою до завантаження в таблицю
     */
    public ObservableList<Books> getBooks (){
        String id, name, writer, genre, department;
        boolean available;

        ObservableList<Books> books = FXCollections.observableArrayList();

        try {
            String query = "SELECT id_books, name_book, writer, name_genre, name_department, isAvailable FROM books\n" +
                    "INNER JOIN book_genre\n" +
                    "\tON books.genre_id = book_genre.id_genre\n" +
                    "INNER JOIN books_department\n" +
                    "\tON books.department_id = books_department.id_department\n" +
                    ";";

            ResultSet queryResult = execQuery(query);

            while (queryResult.next()){
                id = queryResult.getString("id_books");
                name = queryResult.getString("name_book");
                writer = queryResult.getString("writer");
                genre = queryResult.getString("name_genre");
                department = queryResult.getString("name_department");
                available = queryResult.getBoolean("isAvailable");

                books.add(new Books(id,name, writer, genre, department, available));
            }
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        return books;
    }

    /**
     * Виконання запиту до БД і заповнення списку інформацією про читачів
     * @return список з інформацією, готовою до завантаження в таблицю
     */
    public ObservableList<Members> getMembers (){
        String id, login, name, surname, phone_number;

        ObservableList<Members> members = FXCollections.observableArrayList();

        try {
            String query = "SELECT id_reading_ticket, login, name, last_name, phone_num FROM library_reader;";

            ResultSet queryResult = execQuery(query);

            while (queryResult.next()){
                id = queryResult.getString("id_reading_ticket");
                login = queryResult.getString("login");
                name = queryResult.getString("name");
                surname = queryResult.getString("last_name");
                phone_number = queryResult.getString("phone_num");


                members.add(new Members(id, login, name, surname, phone_number));
            }
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        return members;
    }

}