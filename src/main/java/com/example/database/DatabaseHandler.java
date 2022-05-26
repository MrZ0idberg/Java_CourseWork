package com.example.database;

import com.example.models.Books;
import com.example.models.Members;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.*;

import java.time.LocalDate;
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

        String hashPassword = DigestUtils.sha1Hex(password);

        String verifyLoginWorkers = "SELECT COUNT(1) FROM workers\n" +
                "WHERE login = '" + login + "' AND password = '" + hashPassword + "';";
        String verifyLoginReaders = "SELECT COUNT(1) FROM library_reader\n" +
                "WHERE login = '" + login + "' AND password = '" + hashPassword + "';";

        boolean checkWorkers = false;
        boolean checkReaders = false;

        ResultSet queryResult;

        try{
             queryResult = execQuery(verifyLoginWorkers);

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

        /*
         * Зберігання даних про те, хто авторизувався у системі
         */
        Properties properties = new Properties();

        try {
            FileInputStream in = new FileInputStream("settings.properties");
            properties.load(in);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String id = "Not Set";
        String post = "Not Set";
        String query;

        try {
            if (checkWorkers) {
                query = "SELECT id_workers, post_id FROM workers\n" +
                        "WHERE login = '" + login + "';";
                queryResult = execQuery(query);
                while (queryResult.next()) {
                    id = queryResult.getString("id_workers");
                    post = queryResult.getString("post_id");
                }
            } else {
                query = "SELECT id_reading_ticket FROM library_reader\n" +
                        "WHERE login = '" + login + "';";
                queryResult = execQuery(query);
                while (queryResult.next()) {
                    id = queryResult.getString("id_reading_ticket");
                    post = "Reader";
                }
            }

            //Запис значень, в об'єкт конфігурації
            properties.setProperty("system.userLogin",login);
            properties.setProperty("system.userID",id);
            properties.setProperty("system.userPost",post);

        } catch (SQLException e){
            e.printStackTrace();
        }

        //Запис значень в файл
        try {
            FileOutputStream out = new FileOutputStream("settings.properties");
            properties.store(out, "Update");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return checkWorkers || checkReaders;
    }

    /**
     * Перевірка наявності користувача в системі
     * @param login логін користувача
     * @return true - дані є в БД,
     * false - дані відсутні в БД
     */
    public boolean checkAvailableUser(String login){

        String verifyLoginWorkers = "SELECT COUNT(1) FROM workers\n" +
                "WHERE login = '" + login + "';";
        String verifyLoginReaders = "SELECT COUNT(1) FROM library_reader\n" +
                "WHERE login = '" + login + "';";
        boolean checkWorkers = false;
        boolean checkReaders = false;

        ResultSet queryResult;

        try{
            queryResult = execQuery(verifyLoginWorkers);

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
     * @param idGenre ID жанру книжки
     * @param idDepartment ID відділу зберігання книги
     * @return true - запит виконано успішно;
     * false - запит не виконано
     */
    public boolean addBook (String name, String author, String idGenre, String idDepartment){
        String query = "INSERT INTO books(name_book, writer, genre_id, department_id)\n" +
                "VALUES (\""  + name +  "\", \"" + author + "\", \"" + idGenre + "\", \""  + idDepartment +"\");";
        return execAction(query);
    }

    /**
     * Отримання списку жанрів
     */
    public ObservableList<String> getListGenre (){

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "SELECT name_genre FROM book_genre;";

        ResultSet queryResult = execQuery(query);
        try {
            while (queryResult.next()){

                String nameGenre = queryResult.getString("name_genre");
                list.add(nameGenre);
            }
        } catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

        return list;
    }

    /**
     * Отримання списку жанрів
     */
    public ObservableList<String> getListDepartment (){

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "SELECT name_department FROM books_department;";

        ResultSet queryResult = execQuery(query);
        try {
            while (queryResult.next()){

                String nameGenre = queryResult.getString("name_department");
                list.add(nameGenre);
            }
        } catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

        return list;
    }

    /**
     * Пошук ID відділу, по його назві
     * @param departmentName назва відділу
     * @return ID відділу
     */
    public String searchIdDepartment(String departmentName){

        String id = null;

        String query = "SELECT id_department FROM books_department\n" +
                "WHERE name_department = '" + departmentName + "';";

        ResultSet queryResult = execQuery(query);
        try {
            while (queryResult.next()){
                 id = queryResult.getString("id_department");
            }
        } catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

        return id;
    }

    /**
     * Пошук ID жанру, по його назві
     * @param genreName назва жанру
     * @return ID відділу
     */
    public String searchIdGenre(String genreName){

        String id = null;

        String query = "SELECT id_genre FROM book_genre\n" +
                "WHERE name_genre = '" + genreName + "';";

        ResultSet queryResult = execQuery(query);
        try {
            while (queryResult.next()){
                id = queryResult.getString("id_genre");
            }
        } catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

        return id;
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

        String hashPassword = DigestUtils.sha1Hex(password);

        String query = "INSERT INTO library_reader(login, password, name, last_name, phone_num)\n" +
                "VALUES ('" + login + "', '" + hashPassword + "', '" + name + "', '" + surname + "', '" + phone_number + "')\n;";
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

    /**
     * Пошук інформації про книжку по ID, якщо ID відсутнє, виводить повідомлення про це
     * @param id ID книги
     * @return об'єкт з назвою, автором і наявністю
     */
    public Books searchBookInfo(String id){

        Books books = new Books();

        try {
            String query = "SELECT count(1) FROM books\n" +
                    "WHERE id_books = " + id +";";
            ResultSet queryResult = execQuery(query);

            boolean available = false;

            while (queryResult.next()){
                available = queryResult.getBoolean(1);
            }

            if (available){
                query = "SELECT name_book, writer, isAvailable FROM books\n" +
                        "WHERE id_books = " + id + "\n;";

                queryResult = execQuery(query);

                while (queryResult.next()) {
                    books.setName(queryResult.getString("name_book"));
                    books.setAuthor(queryResult.getString("writer"));
                    books.setAvailable(queryResult.getBoolean("isAvailable"));
                }
            }else {
                books.setName("Книжку не знайдено");
                books.setAuthor("Перевірте ID");
                books.setAvailable(false);
            }
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        return books;
    }

    /**
     * Пошук інформації про читача по ID, якщо ID відсутнє, виводить повідомлення про це
     * @param id ID читача
     * @return об'єкт з потрібною інформацією
     */
    public Members searchMemberInfo(String id){

        Members members = new Members();

        try {

            String query = "SELECT count(1) FROM library_reader\n" +
                    "WHERE id_reading_ticket = " + id + ";";
            ResultSet queryResult = execQuery(query);

            boolean available = false;

            while (queryResult.next()){
                available = queryResult.getBoolean(1);
            }

            if (available){
                query = "SELECT name, last_name, phone_num FROM library_reader\n" +
                        "WHERE id_reading_ticket = " + id + ";";

                queryResult = execQuery(query);

                while (queryResult.next()) {
                    members.setName(queryResult.getString("name"));
                    members.setSurname(queryResult.getString("last_name"));
                    members.setPhoneNumber(queryResult.getString("phone_num"));
                }
            }else {
                members.setName("Читача не ");
                members.setSurname("знайдено");
                members.setPhoneNumber("Перевірте ID");
            }

        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

        return members;
    }

    /**
     * Видача книги, запис в БД
     * ІД книги та читача передаються в параметрах, ІД працівника з файлу параметрів зчитується, а дата береться поточна
     * @param bookID ІД книги
     * @param memberID ІД читача
     * @return true - виконання успішне,
     * false - помилка при виконанні
     */
    public boolean issueBook(String bookID, String memberID){

        //Зчитуємо ID користувача з файлу налаштувань
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("settings.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String workerID = properties.getProperty("system.userID");

        //Отримання поточної дати
        LocalDate date = LocalDate.now();
        String dateOut = date.toString();

        //Виконання запиту до БД
        String insert = "INSERT INTO accounting_books(books_id, library_reader_id, book_was_taken, date_out, gave_worker_id)\n" +
                "VALUES ('" + bookID + "', '" + memberID + "', '" + "1" + "', '" + dateOut + "', '" + workerID + "');";
        String update = "UPDATE books\n" +
                "SET isAvailable = 0\n" +
                "WHERE id_books = " + bookID +";";

        return execAction(insert) && execAction(update);
    }

    /**
     * Пошук інформації про книгу, котра пов'язана з обліком
     * @param bookID ID книги
     * @return Лист з інформацією для виводу
     */
    public ObservableList<String> loadBookAndMemberInfo(String bookID){

        ObservableList<String> info = FXCollections.observableArrayList();

        String query;
        ResultSet queryResult;

        String recordID = null;
        String dateOut = null;
        String memberName = null;
        String memberLastName = null;
        String memberPhone = null;
        String workerName = null;
        String workerLastName = null;

        try {

            recordID = searchRecordID(bookID);

            /*
             * Пошук інформації по запису ім'я і прізвище читача, коли видано, ким видано
             */
            query = "SELECT accounting_books.date_out, library_reader.name, library_reader.last_name,\n" +
                    "library_reader.phone_num, workers.name, workers.last_name \n" +
                    "FROM accounting_books\n" +
                    "INNER JOIN library_reader\n" +
                    "\tON accounting_books.library_reader_id = library_reader.id_reading_ticket\n" +
                    "INNER JOIN workers\n" +
                    "\tON accounting_books.gave_worker_id = workers.id_workers\n" +
                    "WHERE id_accounting_books = " + recordID + ";";
            queryResult = execQuery(query);
            while (queryResult.next()) {
                dateOut = queryResult.getString("accounting_books.date_out");
                memberName = queryResult.getString("library_reader.name");
                memberLastName = queryResult.getString("library_reader.last_name");
                memberPhone = queryResult.getString("library_reader.phone_num");
                workerName = queryResult.getString("workers.name");
                workerLastName = queryResult.getString("workers.last_name");
            }
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

        //Пошук інформації про книгу
        Books book = searchBookInfo(bookID);

        /*
         * Запис даних в список та його повернення
         */
        info.add("Номер запису: " + recordID);
        info.add("Дата видачі: " + dateOut);
        info.add("ID книги: " + bookID);
        info.add("Автор книги: " + book.getAuthor());
        info.add("Назва книги: " + book.getName());
        info.add("Ім'я та прізвище читача: " + memberName + " " + memberLastName);
        info.add("Номер телефону читача: " + memberPhone);
        info.add("Ім'я та прізвище працівника, котрий видав книгу: " + workerName + " " + workerLastName);

        return info;
    }

    /**
     * Пошук ID запису, якщо книга видана, і вона ще не повернена, то це наш клієнт
     */
    public String searchRecordID(String bookID) {

        String recordID = null;

        try {

            String query = "SELECT id_accounting_books FROM accounting_books\n" +
                    "WHERE books_id = " + bookID + " \n" +
                    "AND accounting_books.return = 0;";
            ResultSet queryResult = execQuery(query);

            while (queryResult.next()) {
                recordID = queryResult.getString("id_accounting_books");
            }
        }catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        return recordID;
    }

    /**
     *
     * @param bookID ID книги
     * @return true - якщо операція успішна,
     * false - якщо ні
     */
    public boolean returnBook(String bookID){

        //Зчитуємо ID користувача з файлу налаштувань
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("settings.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String workerID = properties.getProperty("system.userID");

        String queryUpdateBook;
        String queryUpdateRecord;

        String recordID = searchRecordID(bookID);

        //Отримання поточної дати
        LocalDate date = LocalDate.now();
        String dateReceive = date.toString();

        queryUpdateBook = "UPDATE books\n" +
                "SET isAvailable = 1\n" +
                "WHERE id_books = " + bookID + ";";
        queryUpdateRecord = "UPDATE accounting_books\n" +
                "SET return_date = '" + dateReceive + "',\n" +
                "accounting_books.return = 1,\n" +
                "receive_worker_id = "+ workerID + "\n" +
                "WHERE id_accounting_books = " + recordID + ";";

        return execAction(queryUpdateBook) && execAction(queryUpdateRecord);
    }



}