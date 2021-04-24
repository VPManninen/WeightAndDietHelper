package com.example.OlioHT.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.OlioHT.IFragmentOwner;
import com.example.OlioHT.R;
import com.example.OlioHT.SettingsVM;
import com.example.OlioHT.LoginEtRegHandler;
import com.example.OlioHT.User;

public class RegisterFragment extends Fragment {

    /* Fragment used to handle new user registration
           Functionality for user input and listeners for changes
           -> passes most data to LoginEtRegHandler for validation
         */

    private View view;
    private SettingsVM settingsVM;

    // Initializations
    private EditText userEnter;
    private EditText passEnter;
    private EditText passMatch;
    private EditText dateEnter;
    private Button registerButton;
    private boolean passAccepted = false;
    private boolean passConfirmed = false;
    private boolean userAccepted = false;
    private boolean dateAccepted = false;
    private IFragmentOwner fragmentOwner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentOwner) {
            this.fragmentOwner = (IFragmentOwner) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // initializations
        view = inflater.inflate(R.layout.fragment_register, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);

        // initialize view
        userEnter = view.findViewById(R.id.userField);
        passEnter = view.findViewById(R.id.passEdit);
        passMatch = view.findViewById(R.id.passConfirm);
        dateEnter = view.findViewById(R.id.dateField);
        registerButton = view.findViewById(R.id.regButton);

        // checks the entered date
        dateEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String[] dateEntered = dateEnter.getText().toString().split("\\.");
                dateAccepted = LoginEtRegHandler.getInstance().checkEnteredDate(dateEntered);
                if (!dateAccepted) {
                    dateEnter.setError("Invalid date.");
                }
            }
        });

        // Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Checks the entered username
        userEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String username = userEnter.getText().toString();
                // Passes username to validation
                userAccepted = LoginEtRegHandler.getInstance().checkEnteredUser(username);
                if (!userAccepted) {
                    if (username.length() < 5) {
                        // if name is too short.
                        userEnter.setError("Username is too short.");
                    } else {
                        userEnter.setError("Username is taken or contains illegal characters.");
                    }
                }
            }
        });

        // Entered password check
        passEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = passEnter.getText().toString();
                // passes on for validation
                passAccepted = LoginEtRegHandler.getInstance().checkEnteredPass(pass);
                if (!passAccepted) {
                    // error message
                    passEnter.setError("Must be at least 12 characters long and contain one of each: special character, number, capital letter and non-capital letter.");
                }
            }
        });

        // Password confirmation:
        passMatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                passConfirmed = false;
                // if the entered passwords match -> true
                if (passMatch.getText().toString().equals(passEnter.getText().toString())) {
                    passConfirmed = true;
                } else {
                    passMatch.setError("Passwords don't match.");
                }
            }
        });
        return view;
    }

    // register is pressed
    private void register() {
        // if all requirements are met
        if ((passAccepted) && (passConfirmed) && (userAccepted) && (dateAccepted)) {
            // pass the registration forward
            LoginEtRegHandler.getInstance().registerAccount(new User(userEnter.getText().toString(), passMatch.getText().toString(), dateEnter.getText().toString()));
            Toast.makeText(getContext(),"Registration complete.", Toast.LENGTH_SHORT).show();
            fragmentOwner.changeFragment(new LoginFragment(), false , true);
        } else {
            Toast.makeText(getContext(),"Check for errors", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
