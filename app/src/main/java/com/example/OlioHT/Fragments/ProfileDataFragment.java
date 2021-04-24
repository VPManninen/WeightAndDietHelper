package com.example.OlioHT.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.OlioHT.Data;
import com.example.OlioHT.DataHandler;
import com.example.OlioHT.R;
import com.example.OlioHT.SettingsVM;

public class ProfileDataFragment extends Fragment {

    private View view;
    private SettingsVM settingsVM;

    // Initializations
    private EditText weightField;
    private EditText heightField;
    private EditText nameField;
    private TextView ageField;
    private Switch editSwitch;
    private Button submitB;

    private boolean weightAccepted;
    private boolean heightAccepted;
    private boolean nameAccepted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // initializations
        view = inflater.inflate(R.layout.fragment_profiledata, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);

        weightField = view.findViewById(R.id.editWeight);
        heightField = view.findViewById(R.id.editHeight);
        nameField = view.findViewById(R.id.personName);
        ageField = view.findViewById(R.id.textView3);
        editSwitch = view.findViewById(R.id.editMode);
        submitB = view.findViewById(R.id.submitB);
        editSwitch.setChecked(false); // default state

        // allows editing when checked. and disables it when not checked
        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                weightField.setEnabled(isChecked);
                heightField.setEnabled(isChecked);
                nameField.setEnabled(isChecked);
                editSwitch.setEnabled(!isChecked);
                submitB.setEnabled(isChecked); // submit button is only enabled when editing is allowed
            }
        });

        // buttons
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        // Weight input checker
        weightField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Checks if weight is empty
                if (!weightField.getText().toString().isEmpty()) {
                    // converts input into a double
                    double weight = Double.parseDouble(weightField.getText().toString());
                    weightAccepted = false;
                    // accepts a weight between 0 - 350
                    if ((weight > 0) && (weight < 350)) {
                        weightAccepted = true;
                    } else {
                        weightField.setError("Invalid weight. (0, 350] kg");
                    }
                }
            }
        });

        // Height checker
        heightField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                // if input is empty
                if (!heightField.getText().toString().isEmpty()) {
                    // converts height into an integer
                    int height = Integer.parseInt(heightField.getText().toString());
                    heightAccepted = false;
                    // accepts height between 0.1 - 3 meters
                    if ((height > 10) && (height < 300)) {
                        heightAccepted = true;
                    } else {
                        heightField.setError("Invalid height. [10, 300] cm");
                    }
                }
            }
        });

        // Name checker
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                /* Checks if the name entered is longer than 4 characters and accepts it
                 So not many restrictions here... */
                nameAccepted = false;
                if (nameField.getText().toString().length() > 4) {
                    nameAccepted = true;
                } else {
                    nameField.setError("Name is too short");
                }
            }
        });

        // gets the current data from DataHandler
        Data currentData = DataHandler.getInstance().getLatestData();

        // calculates the age of the user and displays it
        String[] bday = settingsVM.getUser().getDateOfBirth().split("\\.");
        String[] date = settingsVM.getDate().split("\\.");
        int age = Integer.parseInt(date[2]) - Integer.parseInt(bday[2]);
        ageField.setText(String.format("Age: %d", age));

        // If the user has previous data - fetch it and apply the values as "default"
        if (currentData != null) {
            editSwitch.setChecked(false);
            nameField.setText(currentData.getName());
            weightField.setText(String.format("%.1f", currentData.getWeight()));
            heightField.setText(String.format("%d",currentData.getHeight()));
        } else {
            // If no data was found encourage inputting it
            editSwitch.setChecked(true);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void submit() {
        // checks if we have inputs
        if ((!nameField.getText().toString().isEmpty()) && (!weightField.getText().toString().isEmpty()) && (!heightField.getText().toString().isEmpty())) {
            // checks if the inputs are accepted
            if ((nameAccepted) && (weightAccepted) && (heightAccepted)) {

                // pass the new data forward and disable editing
                editSwitch.setChecked(false);
                String name = nameField.getText().toString();
                int height = Integer.parseInt(heightField.getText().toString());
                double weight = Double.parseDouble(weightField.getText().toString());
                DataHandler.getInstance().updateData(settingsVM.getUser(), settingsVM.getDate(), name, height, weight);
            } else {
                Toast.makeText(getContext(),"Check for errors", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(),"One or more inputs are empty", Toast.LENGTH_SHORT).show();
        }
    }
}
