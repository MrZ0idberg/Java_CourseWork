package com.example.models;

import javafx.beans.property.SimpleStringProperty;

/**
 * Клас-модель для роботи з робітниками
 */
public class Workers extends Person{

    private final SimpleStringProperty idWorkers;
    private final SimpleStringProperty post;


    public Workers(String login, String name, String surname, String phone_number, String idWorkers, String post) {
        super(login, name, surname, phone_number);
        this.idWorkers = new SimpleStringProperty(idWorkers);
        this.post = new SimpleStringProperty(post);
    }

    public String getIdWorkers() {
        return idWorkers.get();
    }

    public SimpleStringProperty idWorkersProperty() {
        return idWorkers;
    }

    public String getPost() {
        return post.get();
    }

    public SimpleStringProperty postProperty() {
        return post;
    }
}
