package com.example.OlioHT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DataHandler {
    /* Applications data brains
    * Mainly used as a way to store and update data during the applications usage
    * Uses the Data-class entries
    * Contains ONLY one users data at a time / SQLite contains EVERYONE'S data.
    * Singleton */

    private static DataHandler handler = new DataHandler();
    private ArrayList<Data> userData;

    private DataHandler() {
        // default value for userdata
        userData = null;
    }

    public static DataHandler getInstance() {
        return handler;
    }

    public Data getLatestData() {
        // returns the latest data entry found
        if (userData != null) {
            return userData.get(userData.size()-1);
        } else {
            return null;
        }
    }

    public Data getFirstData() {
        // returns the first data entry given for the application from the user
        if (userData != null) {
            return userData.get(0);
        } else {
            return null;
        }
    }

    public Data getPrevData() {
        // returns the previous data entry to the latest entry found.
        if ((userData != null) && (userData.size() > 1)) {
            return userData.get(userData.size()-2);
        } else {
            return null;
        }
    }

    public Data getPrevFood() {
        // returns the previous (as stated in getPrevData()) non zero CO2 consumption entry
        if ((userData != null) && (userData.size() > 1)) {
            for (int i = userData.size() - 2; i >= 0; i--) {
                if (userData.get(i).getApi() != 0) {
                    return userData.get(i);
                }
            }
        }
        return null;
    }

    public Data getFirstFood() {
        // returns the first non zero CO2 consumption entry given by user.
        if ((userData != null)) {
            for (int i = 0; i < userData.size(); i++) {
                if (userData.get(i).getApi() != 0) {
                    return userData.get(i);
                }
            }
        }
        return null;
    }

    public ArrayList<Data> getData() {
        // if all the data is required somewhere (History fragment for example)
        return userData;
    }

    public void updateData(User user, String date, String name, int height, double weight) {
        /* Method for updating latest data entry */
        Data latest = getLatestData(); // checks if we have latest entry
        double bmi = 1; // initialize a variable for BMI
        if (latest != null) { // if there is an entry
            // and the date of the entry is the current day
            if (latest.getDate().equals(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()))) {
                // change the values in the entry to be the same as entered values.
                latest.setHeight(height);
                latest.setWeight(weight);
                latest.setName(name);
                // if height somehow equals 0
                if (height != 0) {
                    bmi = 10000 * weight / (height * height);
                } else {
                    bmi = 1;
                }
                latest.setBmi(bmi);
                // removes the old entry
                userData.remove(userData.size()-1);
                // adds the updated entry
                userData.add(latest);
                // updates the database to match the current data
                SqlManager.getInstance().updateUserData(latest);
                return;
            }
        }
        /* if we have no entry for the user
        * we use the above way to add a new entry to the table.
        * This should only occur when new user is created*/
        if (height != 0) {
            bmi = 10000 * weight / (height * height);
        } else {
            bmi = 1;
        }
        Data newEntry = new Data(date, name, weight, height, bmi, 0, user.getUserID());
        if (userData == null) {
            userData = new ArrayList<>();
        }
        userData.add(newEntry);
        SqlManager.getInstance().addData(newEntry);
        return;
    }

    public void getUserData(User user) {
        // when a user logs in -> gets the user's data from the database for the class to use.
        userData = SqlManager.getInstance().getDataByUser(user);
        return;
    }

    public void clearUserData() {
        // when user logs out -> clear the data variable for the new user
        userData = null;
        return;
    }

    public void addDailyData(User user) {
        /* when a user logs in for the first time in a day, and has previous data entries
        * add a new similar entry suggesting that no values have changed */
        if ((userData != null) && (getLatestData() != null)) {
            Data latest = getLatestData();
            updateData(user, new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()), latest.getName(), latest.getHeight(), latest.getWeight());
        }
        return;
    }

    public boolean updateLatestAPIEntry(double apiEntry) {
        /* Function for updating the latest API entry
        * As this value is given from a different location in the app we only want to change this
        * one value to reduce errors */
        Data latest = getLatestData();
        if (latest != null) {
            // So we fetch the latest entry and only change the one value
            latest.setApi(apiEntry);
            // then update it
            SqlManager.getInstance().updateUserData(latest);
            return true;
        } else {
            return false;
        }
    }
}
