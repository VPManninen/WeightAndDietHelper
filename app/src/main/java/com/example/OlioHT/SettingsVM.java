package com.example.OlioHT;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SettingsVM extends ViewModel {
    /* Some common variables for the application to use.
    * Most of the could be relocated to a better place but they are used by most of the fragments. */
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<String> date = new MutableLiveData<>();

    // default settings
    public void initSettings() {
        // current user
        currentUser.setValue(null);
        // current date
        date.setValue(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));
    }

    // change saved settings
    public void changeCurrentUser(@NonNull User newUser) {
        this.currentUser.setValue(newUser);
    }

    // get saved settings
    public User getUser() { return currentUser.getValue(); }
    public String getDate() {return date.getValue(); }
}
