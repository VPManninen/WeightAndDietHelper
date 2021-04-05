package com.example.week11;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class settingsFragment extends Fragment {

    View view;
    private SettingsVM settingsVM;
    private EditText fontSizeInput;
    private Switch editToggler;
    private EditText displayText;
    private Spinner langSpinner;
    private EditText linesInput;
    private Spinner colorSpinner;
    private Switch boldToggle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    // initializations
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);
        fontSizeInput = view.findViewById(R.id.fontSize);
        editToggler = view.findViewById(R.id.editSwitch);
        displayText = view.findViewById(R.id.displayText);
        langSpinner = view.findViewById(R.id.lang);
        colorSpinner = view.findViewById(R.id.colors);
        linesInput = view.findViewById(R.id.nOfLines);
        boldToggle = view.findViewById(R.id.boldToggle);

        displayText.setText(settingsVM.getDisplay().getValue());

        fontSizeInput.setText(settingsVM.getFontSize().getValue().toString());
        linesInput.setText(settingsVM.getLines().getValue().toString());

        // checks for changes on the font size field
        fontSizeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                if (fontSizeInput.getText().toString().isEmpty() == false) {
                    if ((50 > Integer.parseInt(fontSizeInput.getText().toString())) && (Integer.parseInt(fontSizeInput.getText().toString()) > 0)) {
                        settingsVM.changeFontSize(Integer.parseInt(fontSizeInput.getText().toString()));
                    }
                }
            }
        });

        // checks for changes on the line amount field
        linesInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                if (linesInput.getText().toString().isEmpty() == false) {
                    if ((20 > Integer.parseInt(linesInput.getText().toString())) && (Integer.parseInt(linesInput.getText().toString()) > 0)) {
                        settingsVM.changeLines(Integer.parseInt(linesInput.getText().toString()));
                    }
                }
            }
        });

        // checks if the edit field state is changed
        editToggler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsVM.changeLockState(false);
                } else {
                    settingsVM.changeLockState(true);
                }
            }
        });

        // Checks if the Bold-setting is toggled
        boldToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsVM.changeBoldState(true);
                } else {
                    settingsVM.changeBoldState(false);
                }
            }
        });

        // Checks if the display text field changes
        displayText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                settingsVM.changeDisplay(displayText.getText().toString());
            }
        });


        // Checks if the selected colour is changed
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (colorSpinner.getSelectedItem().toString()) {
                    case ("Black"):
                        settingsVM.changeColorPos(colorSpinner.getSelectedItemPosition());
                        settingsVM.changeColor(Color.BLACK);
                        break;
                    case ("Red"):
                        settingsVM.changeColor(Color.RED);
                        settingsVM.changeColorPos(colorSpinner.getSelectedItemPosition());
                        break;
                    case ("Blue"):
                        settingsVM.changeColor(Color.BLUE);
                        settingsVM.changeColorPos(colorSpinner.getSelectedItemPosition());
                        break;
                    case ("Green"):
                        settingsVM.changeColor(Color.GREEN);
                        settingsVM.changeColorPos(colorSpinner.getSelectedItemPosition());
                        break;
                    case ("Yellow"):
                        settingsVM.changeColor(Color.YELLOW);
                        settingsVM.changeColorPos(colorSpinner.getSelectedItemPosition());
                        break;
                    case ("White"):
                        settingsVM.changeColor(Color.WHITE);
                        settingsVM.changeColorPos(colorSpinner.getSelectedItemPosition());
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // spinner for language options
        ArrayList<String> langs = new ArrayList<>();
        langs.add("Fin");
        langs.add("Eng");
        langs.add("Swe");
        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, langs);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(langAdapter);

        // spinner for colour options
        ArrayList<String> colors = new ArrayList<>();
        colors.add("Black");
        colors.add("Red");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Yellow");
        colors.add("White");
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, colors);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settingsVM.getLockState().getValue()) {
            editToggler.setChecked(false);
        } else {
            editToggler.setChecked(true);
        }
        if (settingsVM.getBoldState().getValue()) {
            boldToggle.setChecked(true);
        } else {
            boldToggle.setChecked(false);
        }
        displayText.setText(settingsVM.getDisplay().getValue());
        colorSpinner.setSelection(settingsVM.getColorPos().getValue());
    }
}
