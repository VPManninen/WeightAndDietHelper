package com.example.OlioHT.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.OlioHT.DataHandler;
import com.example.OlioHT.IFragmentOwner;
import com.example.OlioHT.R;
import com.example.OlioHT.SettingsVM;
import com.example.OlioHT.LoginEtRegHandler;
import com.example.OlioHT.SqlManager;
import com.example.OlioHT.User;

public class LoginFragment extends Fragment {

    /* Fragment used as the first fragment when application is opened
    * Contains methods to run the "login" functionality of the application */

    // initializations
    private View view;
    private SettingsVM settingsVM;
    private Button loginB;
    private Button registerB;
    private Button forgotB;
    private CheckBox rememberBox;
    private EditText userEnter;
    private EditText passwordEnter;
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
        view = inflater.inflate(R.layout.fragment_login, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);
        loginB = view.findViewById(R.id.logB);
        registerB = view.findViewById(R.id.regB);
        userEnter = view.findViewById(R.id.usernameField);
        passwordEnter = view.findViewById(R.id.passwordField);
        forgotB = view.findViewById(R.id.forgotPW);
        rememberBox = view.findViewById(R.id.rememberBox);

        settingsVM.changeCurrentUser(null);
        DataHandler.getInstance().clearUserData();

        // When entering a password and enter is pressed -> login()
        passwordEnter.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == event.KEYCODE_ENTER)) {
                    login();
                }
                return false;
            }
        });

        // Button functionality
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPressed();
            }
        });

        forgotB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPressed();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /* takes the login information given and passes on to respective classes for login handling */
    private void login() {
        // gets the entered information
        String userEntered = userEnter.getText().toString();
        String passEntered = passwordEnter.getText().toString();

        // sets login to false (default)
        Boolean loginState = false;
        if ((!userEntered.isEmpty()) && (!passEntered.isEmpty())) {
            // non-empty inputs go through a "login check"
            loginState = LoginEtRegHandler.getInstance().checkLogin(userEntered, passEntered);
        }
        if (loginState) {
            /* correct login info -> application proceeds to home
             saves the current user into VM */
            settingsVM.changeCurrentUser(SqlManager.getInstance().getByUsername(userEntered));
            // Initializes the values stored in DataHandler
            DataHandler.getInstance().getUserData(settingsVM.getUser());
            // adds a new data instance for the day
            DataHandler.getInstance().addDailyData(settingsVM.getUser());

            // saves login information and automatic login if wanted to 1 = saved, 0 = not
            if (rememberBox.isChecked()) {
                SqlManager.getInstance().updateSaveState(settingsVM.getUser(), 1);
            } else {
                SqlManager.getInstance().updateSaveState(settingsVM.getUser(), 0);
            }
            fragmentOwner.changeFragment(new HomeFragment(), false, false);
        } else {
            // if login was unsuccessful
            Toast.makeText(getContext(),"Invalid username or password.", Toast.LENGTH_SHORT).show();
        }
        return;
    }

    public void registerPressed() {
        fragmentOwner.changeFragment(new RegisterFragment(), true, true);
        return;
    }

    public void forgotPressed() {
        fragmentOwner.changeFragment(new ForgotFragment(), true, true);
        return;
    }
}
