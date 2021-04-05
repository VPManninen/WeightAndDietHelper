package com.example.week11;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsVM extends ViewModel {
    // Settings variables
    private MutableLiveData<Integer> fontSize = new MutableLiveData<>();
    private MutableLiveData<Integer> lines = new MutableLiveData<>();
    private MutableLiveData<Boolean> editToggle = new MutableLiveData<>();
    private MutableLiveData<Boolean> editBold = new MutableLiveData<>();
    private MutableLiveData<String> textField = new MutableLiveData<>();
    private MutableLiveData<String> displayText = new MutableLiveData<>();
    private MutableLiveData<Integer> color = new MutableLiveData<>();
    private MutableLiveData<Integer> colorPos = new MutableLiveData<>();

    // default settings
    public void initSettings() {
        fontSize.setValue(16);
        lines.setValue(2);
        editToggle.setValue(true);
        editBold.setValue(false);
        textField.setValue("Type here: ");
        displayText.setValue("Display text");
        color.setValue(Color.BLACK);
        colorPos.setValue(0);
    }

    // change saved settings
    public void changeFontSize(@NonNull Integer givenSize) {
        this.fontSize.setValue(givenSize);
    }

    public void changeColor(@NonNull Integer colorCode) {
        this.color.setValue(colorCode);
    }

    public void changeColorPos(@NonNull Integer colorP) {
        this.colorPos.setValue(colorP);
    }

    public void changeLines(@NonNull Integer givenSize) {
        this.lines.setValue(givenSize);
    }

    public void changeLockState(@NonNull Boolean givenState) {
        this.editToggle.setValue(givenState);
    }

    public void changeBoldState(@NonNull Boolean givenState) {
        this.editBold.setValue(givenState);
    }

    public void changeString(@NonNull String givenString) {
        this.textField.setValue(givenString);
    }

    public void changeDisplay(@NonNull String givenString) {
        this.displayText.setValue(givenString);
    }

    // get saved settings
    public LiveData<String> getString() { return textField; }

    public LiveData<Integer> getFontSize() {
        return fontSize;
    }

    public LiveData<Integer> getLines() {
        return lines;
    }

    public LiveData<Integer> getColor() {
        return color;
    }

    public LiveData<Integer> getColorPos() {
        return colorPos;
    }

    public LiveData<Boolean> getLockState() {
        return editToggle;
    }

    public LiveData<Boolean> getBoldState() {
        return editBold;
    }

    public LiveData<String> getDisplay() {
        return displayText;
    }
}
