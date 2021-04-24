package com.example.OlioHT;

public class User {
    /* User class
    * Contains all variables required to store an individual user
    * Used by : SQLite database and the application while running */
    // required variables
    private int userID;
    private String username;
    private String password;
    private String dateOfBirth;

    // Existing user
    public User(int userID, String username, String password, String dateOfBirth) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    // New user
    public User(String username, String password, String dateOfBirth) {
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    // getters for variables
    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
}
