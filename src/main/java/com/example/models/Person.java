package com.example.models;

import javafx.beans.property.SimpleStringProperty;

/**
 * Абстракний клас-модель для визначення сутності персони
 * Об'єднує у собі поля та методи для роботи з логіном, ім'ям, прізвищем, та номером
 */
public abstract class Person {

    private SimpleStringProperty login;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty phoneNumber;

    protected Person (){}

    protected Person (String login, String name, String surname, String phone_number){

        this.login = new SimpleStringProperty(login);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.phoneNumber = new SimpleStringProperty(phone_number);
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);;
    }

    public void setSurname(String surname) {
        this.surname = new SimpleStringProperty(surname);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }
}