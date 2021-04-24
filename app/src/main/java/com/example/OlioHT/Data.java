package com.example.OlioHT;

public class Data {
    /* DATA class
    * Constructed to store data entries and contains all information SQLite Database requires
    * This class is mainly used to store data entries during the application's use */

    // all variables
    private int id;
    private String date;
    private String name;
    private double weight;
    private int height;
    private double bmi;
    private double api;
    private int userId;

    public Data(int id, String date, String name, double weight, int height, double bmi, double api, int userId) {
        // initializer used when fetching data from the database
        this.id = id;
        this.date = date;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.api = api;
        this.userId = userId;
    }

    public Data(String date, String name, double weight, int height, double bmi, double api, int userId) {
        // initializer used when creating new data entries
        this.date = date;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.api = api;
        this.userId = userId;
    }

    // getters for the application to use
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public double getBmi() {
        return bmi;
    }

    public double getApi() {
        return api;
    }

    public int getUserId() {
        return userId;
    }

    // setters for data entry changes
    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public void setApi(double api) {
        this.api = api;
    }
}
