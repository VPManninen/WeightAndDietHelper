package com.example.OlioHT.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.OlioHT.IFragmentOwner;
import com.example.OlioHT.R;

public class AboutFragment extends Fragment {

    /* This fragment contains all code required to show information on "About" -view
    * String constant for "TextView" component is all there is */

    private View view;
    private IFragmentOwner fragmentOwner;

    private TextView aboutText;

    private static final String ABOUT = "Olio-ohjelmointi Harjoitustyö: Weight and Diet helper \n" +
            "Ville-Petteri Manninen 25.04.2021 \n \n" +
            "Application collects following user input data: \n" +
            "\t - name \n" +
            "\t - weight \n" +
            "\t - height \n" +
            "\t - body mass index (BMI) \n" +
            "\t - Food consumption habits (CO2) \n" +
            "All data collected is stored into a local SQLite database. \n" +
            "If you wish to get remove all data stored: \n" +
            "Delete: Data/Data/com.example.OlioHT/databases \n \n" +
            "Food consumption is calculated using a public API \n" +
            "Provided by: SYKE Climate Diet public API 1.0 \n" +
            "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/swagger/ui/index#!/ConsumptionCalculator/ConsumptionCalculator_Get";

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
        view = inflater.inflate(R.layout.fragment_about, container, false);
        aboutText = view.findViewById(R.id.aboutText);
        aboutText.setText(ABOUT);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
