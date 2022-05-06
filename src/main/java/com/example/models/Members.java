package com.example.models;

import javafx.beans.property.SimpleStringProperty;

/**
 * Клас-модель для роботи з читачами
 */
public class Members extends Person{

    private final SimpleStringProperty idReadingTicket;

    public Members(String idReadingTicket, String login, String name, String surname, String phone_number ) {
        super(login, name, surname, phone_number);
        this.idReadingTicket = new SimpleStringProperty(idReadingTicket);
    }

    public String getIdReadingTicket() {
        return idReadingTicket.get();
    }

    public SimpleStringProperty idReadingTicketProperty() {
        return idReadingTicket;
    }
}
