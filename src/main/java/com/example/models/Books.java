package com.example.models;

import javafx.beans.property.SimpleStringProperty;

/**
 * Клас-модель, для представлення книжок
 */
public class Books {

    private  SimpleStringProperty id;
    private  SimpleStringProperty name;
    private  SimpleStringProperty author;
    private  SimpleStringProperty genre;
    private  SimpleStringProperty department;
    private  SimpleStringProperty available;

    public Books(){}

    public Books(String id,String name,String author,String genre,String department,Boolean available){

        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.department = new SimpleStringProperty(department);

        if (available) {
            this.available = new SimpleStringProperty("В наявності");
        } else {
            this.available = new SimpleStringProperty("Відсутня");
        }
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public String getDepartment() {
        return department.get();
    }

    public String isAvailable() {
        return available.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public SimpleStringProperty departmentProperty() {
        return department;
    }

    public SimpleStringProperty availableProperty() {
        return available;
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public void setAuthor(String author) {
        this.author = new SimpleStringProperty(author);
    }

    public void setAvailable(Boolean available) {
        if (available) {
            this.available = new SimpleStringProperty("В наявності");
        } else {
            this.available = new SimpleStringProperty("Відсутня");
        }
    }
}
