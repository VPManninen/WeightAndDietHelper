package com.example.OlioHT.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.OlioHT.Data;
import com.example.OlioHT.DataHandler;
import com.example.OlioHT.IFragmentOwner;
import com.example.OlioHT.R;
import com.example.OlioHT.SettingsVM;

public class HomeFragment extends Fragment {

    /* Home page fragment:
    * Contains variables for multiple TextView components and a couple if - else structures
    * to display different types of data differently */

    private View view;
    private SettingsVM settingsVM;
    private IFragmentOwner fragmentOwner;

    // initializations
    private TextView weightC;
    private TextView weightP;
    private TextView weightF;

    private TextView bmiC;
    private TextView bmiP;
    private TextView bmiF;

    private TextView foodC;
    private TextView foodP;
    private TextView foodF;

    private TextView currentText;
    private TextView prevText;
    private TextView firstText;

    private TextView userText;

    private TextView welcomeText;

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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);

        // initializations
        weightC = view.findViewById(R.id.weightCur);
        weightP = view.findViewById(R.id.weightPrev);
        weightF = view.findViewById(R.id.weightFirst);

        bmiC = view.findViewById(R.id.bmiCur);
        bmiP = view.findViewById(R.id.bmiPrev);
        bmiF = view.findViewById(R.id.bmiFirst);

        foodC = view.findViewById(R.id.foodCur);
        foodP = view.findViewById(R.id.foodPrev);
        foodF = view.findViewById(R.id.foodFirst);

        currentText = view.findViewById(R.id.compCurr);
        prevText = view.findViewById(R.id.compPrev);
        firstText = view.findViewById(R.id.compFirst);

        userText = view.findViewById(R.id.userText);
        welcomeText = view.findViewById(R.id.welcomeText);

        userText.setText(String.format("Logged in as: %s", settingsVM.getUser().getUsername()));
        currentText.setText(String.format("Current (%s):", settingsVM.getDate()));

        Data latest = DataHandler.getInstance().getLatestData();
        Data first = DataHandler.getInstance().getFirstData();
        Data previous = DataHandler.getInstance().getPrevData();

        if (latest == null) {
            welcomeText.setText("To start using the application: \n follow the below steps to insert base comparison data.");
        } else {
            weightC.setText(String.format("%.1f kg", latest.getWeight()));
            bmiC.setText(String.format("%.1f kg/m^2", latest.getBmi()));
            if (latest.getApi() != 0) {
                foodC.setText(String.format("%.2f kg/year", latest.getApi()));
            } else {
                foodC.setText(String.format("Today's data missing", latest.getApi()));
            }
        }

        if (first != null) {
            double weightValF = latest.getWeight() - first.getWeight();
            double bmiValF = latest.getBmi() - first.getBmi();
            double foodValF = 0;
            firstText.setText(String.format("Compared to first (%s):", first.getDate()));
            weightF.setText(String.format("%.1f kg", weightValF));
            bmiF.setText(String.format("%.1f kg/m^2", bmiValF));
            if (latest.getApi() != 0) {
                foodValF = latest.getApi() - DataHandler.getInstance().getFirstFood().getApi();
                foodF.setText(String.format("%.2f kg/year", foodValF));
            } else {
                foodF.setText("Today's data missing");
            }
            if (weightValF > 0) {
                weightF.setTextColor(Color.RED);
            } else {
                weightF.setTextColor(Color.GREEN);
            }
            if (bmiValF > 0) {
                bmiF.setTextColor(Color.RED);
            } else {
                bmiF.setTextColor(Color.GREEN);
            }
            if (foodValF > 0) {
                foodF.setTextColor(Color.RED);
            } else {
                bmiF.setTextColor(Color.GREEN);
            }
        }

        if (previous != null) {
            double weightValP = latest.getWeight() - previous.getWeight();
            double bmiValP = latest.getBmi() - previous.getBmi();
            double foodValP = 0;
            prevText.setText(String.format("Compared to previous (%s):", previous.getDate()));
            weightP.setText(String.format("%.1f kg", weightValP));
            bmiP.setText(String.format("%.1f kg/m^2", bmiValP));
            if (latest.getApi() != 0) {
                foodValP = latest.getApi() - DataHandler.getInstance().getPrevFood().getApi();
                foodP.setText(String.format("%.2f kg/year", foodValP));
            } else {
                foodP.setText("Today's data missing");
            }
            if (weightValP > 0) {
                weightP.setTextColor(Color.RED);
            } else {
                weightP.setTextColor(Color.GREEN);
            }
            if (bmiValP > 0) {
                bmiP.setTextColor(Color.RED);
            } else {
                bmiP.setTextColor(Color.GREEN);
            }
            if (foodValP > 0) {
                foodP.setTextColor(Color.RED);
            } else {
                bmiP.setTextColor(Color.GREEN);
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
