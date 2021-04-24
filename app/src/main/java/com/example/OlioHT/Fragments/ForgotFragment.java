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
import com.example.OlioHT.SqlManager;

public class ForgotFragment extends Fragment {
    private View view;
    private SettingsVM settingsVM;
    private IFragmentOwner fragmentOwner;

    // initializations
    private EditText userEnter;
    private EditText passEnter;
    private EditText passMatch;
    private EditText dateEnter;
    private Button registerButton;
    private boolean passAccepted = false;
    private boolean passConfirmed = false;
    private boolean userAccepted = false;
    private boolean dateAccepted = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentOwner) {
            this.fragmentOwner = (IFragmentOwner) context;
        }
    }

    /* This fragment is only a slightly refactored version of "register fragment" to fit its different purpose */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // initializations
        view = inflater.inflate(R.layout.fragment_forgot, container, false);
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
                // if valid - invalid
                String[] dateEntered = dateEnter.getText().toString().split("\\.");
                dateAccepted = LoginEtRegHandler.getInstance().checkEnteredDate(dateEntered);
                if (!dateAccepted) {
                    dateEnter.setError("Invalid date.");
                }
            }
        });

        // button functionality
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmChange();
            }
        });

        userEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String username = userEnter.getText().toString();
                // This time we need to check if user by name exists
                userAccepted = !LoginEtRegHandler.getInstance().checkEnteredUser(username);
            }
        });

        passEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                // same conditions apply to new password
                String pass = passEnter.getText().toString();
                passAccepted = LoginEtRegHandler.getInstance().checkEnteredPass(pass);
                if (!passAccepted) {
                    passEnter.setError("Must be at least 12 characters long and contain one of each: special character, number, capital letter and non-capital letter.");
                }
            }
        });

        passMatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // checks if the two passwords match or not
                passConfirmed = false;
                if (passMatch.getText().toString().equals(passEnter.getText().toString())) {
                    passConfirmed = true;
                } else {
                    passMatch.setError("Passwords don't match.");
                }
            }
        });
        return view;
    }

    private void confirmChange() {
        if (!userEnter.getText().toString().isEmpty()) {
            // creates a "test" User for comparison
            User testUser = new User(userEnter.getText().toString(), passMatch.getText().toString(), dateEnter.getText().toString());

            // checks if information is correct
            boolean correctInfo = LoginEtRegHandler.getInstance().confirmPWChange(testUser);

            // if all conditions are true -> proceed with password change
            if ((passAccepted) && (passConfirmed) && (userAccepted) && (dateAccepted) && (correctInfo)) {
                // changes the password in database
                SqlManager.getInstance().changeUserPassword(testUser);

                Toast.makeText(getContext(),"Password changed.", Toast.LENGTH_SHORT).show();
                fragmentOwner.changeFragment(new LoginFragment(), false , true);
            } else {
                Toast.makeText(getContext(),"Check for errors", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(),"Check for errors", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
