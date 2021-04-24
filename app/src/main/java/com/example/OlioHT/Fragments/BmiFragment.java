package com.example.OlioHT.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.OlioHT.BMIHandler;
import com.example.OlioHT.Data;
import com.example.OlioHT.DataHandler;
import com.example.OlioHT.IFragmentOwner;
import com.example.OlioHT.R;
import com.example.OlioHT.SettingsVM;

public class BmiFragment extends Fragment {

    /* This fragment contains all required methods and variables to run the BMI-calculator
    Most of the calculations are done in "BMIHandler", but this one displays the information.
    */

    private View view;
    private SettingsVM settingsVM;
    private IFragmentOwner fragmentOwner;

    // Initializations
    private TextView bmiText;
    private TextView calcText;
    private TextView bmiVal;
    private SeekBar bmiSeek;
    private TextView wordval;
    private TextView seekValue;
    private TextView seekResult;
    private TextView seekChangeResult;

    private double seekBmiValue;
    private int progress;

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
        view = inflater.inflate(R.layout.fragment_bmi, container, false);

        // Initializations
        bmiSeek = view.findViewById(R.id.bmiSeek);
        calcText = view.findViewById(R.id.changeText);
        bmiText = view.findViewById(R.id.bmiText);
        bmiVal = view.findViewById(R.id.BMIValue);
        wordval = view.findViewById(R.id.thisMeanField);
        seekValue = view.findViewById(R.id.seekValue);
        seekResult = view.findViewById(R.id.seekResult);
        seekChangeResult = view.findViewById(R.id.seekChangeres);
        // Sets the SeekBar increment to 5.
        bmiSeek.incrementProgressBy(5);

        // ViewModel just in case you need something from there
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);

        // Gets the current data for displays
        Data currentData = DataHandler.getInstance().getLatestData();

        progress = 0;
        if (currentData == null) {
            // if there's no today's data.
            wordval.setText("Please go give data for the application to use.");
            bmiVal.setText("*Error*");
        } else {
            // if data was found -> display it
            wordval.setText(String.format("Which means you are: %s", BMIHandler.getInstance().getBMIText(DataHandler.getInstance().getLatestData().getBmi())));
            bmiVal.setText(String.format("%.1f", DataHandler.getInstance().getLatestData().getBmi()));
            // used on the SeekBar
            progress = (int) DataHandler.getInstance().getLatestData().getBmi() * 100;
        }

        // When comparison BMI is changed -> Change displayed information
        bmiSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // SeekBar value converted into double (Progress must be an integer)
                seekBmiValue = (double) bmiSeek.getProgress() / 100;
                // Sets the comparison BMI to TextView
                seekValue.setText(String.format("%.2f", seekBmiValue));
                // Checks if we have data to compare to
                if (currentData != null) {
                    // Calculations and text conversion for required weight and change
                    seekResult.setText(String.format("Weight: %.1f",
                            BMIHandler.getInstance().getSeekWeight(currentData.getWeight(), currentData.getHeight(), currentData.getBmi(), seekBmiValue)));
                    double change = BMIHandler.getInstance().getSeekDiff(currentData.getHeight(), currentData.getBmi(), seekBmiValue);
                    // if you need to increase or decrease your weight change the color accordingly
                    if (change < 0) {
                        seekChangeResult.setText(String.format("%.1f", change));
                        seekChangeResult.setTextColor(Color.GREEN);
                    } else {
                        seekChangeResult.setText(String.format("+%.1f", change));
                        seekChangeResult.setTextColor(Color.RED);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        // apply the given default progress
        bmiSeek.setProgress(progress);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
