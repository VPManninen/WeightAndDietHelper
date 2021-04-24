package com.example.OlioHT.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.OlioHT.DataHandler;
import com.example.OlioHT.DataParser;
import com.example.OlioHT.IFragmentOwner;
import com.example.OlioHT.R;
import com.example.OlioHT.SettingsVM;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    /* This fragment contains all required methods and variables to run the History-view
        History view uses a ListView component and 2 spinners to show selected data
        This Fragment is somewhat of a "User-data Log" with currently only a way to show EVERY
        Data entry currently inside the database.
        Planned functionality:
        -  Weekly + Monthly + Yearly : data logging.

        ... had to stop somewhere due to time restrictions with other courses etc.
    */

    private View view;
    private SettingsVM settingsVM;
    private IFragmentOwner fragmentOwner;

    // initializations
    private ListView displayData;
    private Spinner timeSpinner;
    private Spinner dataSpinner;
    private Button getDataButton;

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
        view = inflater.inflate(R.layout.fragment_history, container, false);

        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);

        // initializations
        timeSpinner = view.findViewById(R.id.timeSpinner);
        dataSpinner = view.findViewById(R.id.dataSpinner);
        displayData = view.findViewById(R.id.displayView);
        getDataButton = view.findViewById(R.id.getDataB);

        // filling the spinners with required items
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, DataParser.getInstance().getDataSets());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataSpinner.setAdapter(dataAdapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, DataParser.getInstance().getTimeSets());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        // button functionality
        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataByCriteria();
            }
        });

        return view;
    }

    public void getDataByCriteria() {
        try {
            // Fills the ListView with data
            ArrayList<String> dataList; // list

            // checks what is selected in the spinners
            String parseBy = timeSpinner.getSelectedItem().toString();
            String parse = dataSpinner.getSelectedItem().toString();

            // gets the parsed data
            dataList = DataParser.getInstance().parseData(parse, parseBy, DataHandler.getInstance().getData());

            // applies the data into the ListView
            ArrayAdapter<String> recyclerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
            displayData.setAdapter(recyclerAdapter);

        } catch (NullPointerException e) {
            Toast.makeText(getContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
