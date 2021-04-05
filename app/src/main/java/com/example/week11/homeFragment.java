package com.example.week11;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class homeFragment extends Fragment {

    View view;
    private TextView testText;
    private SettingsVM settingsVM;
    private EditText editText;
    private TextView nameView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        testText = view.findViewById(R.id.textView);
        editText = view.findViewById(R.id.editField);
        nameView = view.findViewById(R.id.nameView);

        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);

        // gets the font size setting and updates it
        settingsVM.getFontSize().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                testText.setTextSize(integer);
            }
        });

        // gets the new amount of lines when changed
        settingsVM.getLines().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                testText.setLines(integer);
            }
        });

        // gets the new lock state when changed
        settingsVM.getLockState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                editText.setEnabled(state);
                if (state) {
                    testText.setText(editText.getText().toString());
                }
            }
        });

        // gets the new bold state when changed
        settingsVM.getBoldState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if (state) {
                    testText.setTypeface(null, Typeface.BOLD);
                } else {
                    testText.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        // gets the new text from the settings menu
        settingsVM.getDisplay().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String text) {
                nameView.setText(text);
            }
        });

        // checks for color changes
        settingsVM.getColor().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer value) {
                testText.setTextColor(value);
            }
        });

        // saves the editable field as a string
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                settingsVM.changeString(editText.getText().toString());
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        editText.setText(settingsVM.getString().getValue());
        if (settingsVM.getLockState().getValue() == false) {
            testText.setText(editText.getText().toString());
        }
    }
}
