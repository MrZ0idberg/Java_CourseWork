package com.example.models;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class OrderView {

    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty author;
    private final SimpleStringProperty dateOut;
    private final SimpleStringProperty dateReturn;




    public OrderView(String id,String name,String author,String dateOut,String dateReturn){

        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author);
        this.dateOut = new SimpleStringProperty(dateOut);
        this.dateReturn = new SimpleStringProperty(Objects.requireNonNullElse(dateReturn, "Книга не повернута"));

    }


    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public String getDateOut() {
        return dateOut.get();
    }

    public SimpleStringProperty dateOutProperty() {
        return dateOut;
    }

    public String getDateReturn() {
        return dateReturn.get();
    }

    public SimpleStringProperty dateReturnProperty() {
        return dateReturn;
    }
}
