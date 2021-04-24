package com.example.OlioHT.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.OlioHT.ApiHandler;
import com.example.OlioHT.DataHandler;
import com.example.OlioHT.IFragmentOwner;
import com.example.OlioHT.R;
import com.example.OlioHT.ResponseCallback;
import com.example.OlioHT.SettingsVM;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiFragment extends Fragment implements ResponseCallback {

    /* This fragment contains all required methods and variables to run the FoodCalculator API
    * Extends ResponseCallback to handle API requests which are ran through Volley*/

    private View view;
    private SettingsVM settingsVM;
    private IFragmentOwner fragmentOwner;

    // CheckBox variables for all the possible choices
    private CheckBox beef5;
    private CheckBox beef20;
    private CheckBox beef25;
    private CheckBox beef50;
    private CheckBox beef100;

    private CheckBox fish5;
    private CheckBox fish20;
    private CheckBox fish25;
    private CheckBox fish50;
    private CheckBox fish100;

    private CheckBox dairy5;
    private CheckBox dairy20;
    private CheckBox dairy25;
    private CheckBox dairy50;
    private CheckBox dairy100;

    private CheckBox salad5;
    private CheckBox salad20;
    private CheckBox salad25;
    private CheckBox salad50;
    private CheckBox salad100;

    private CheckBox rice5;
    private CheckBox rice20;
    private CheckBox rice25;
    private CheckBox rice50;
    private CheckBox rice100;

    // Button and TextView components
    private Button submitB;
    private TextView resultView;

    // Volley RequestQueue
    private RequestQueue queue;

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
        view = inflater.inflate(R.layout.fragment_api, container, false);
        // initialize all CheckBoxes
        beef5 = view.findViewById(R.id.beef5);
        beef20 = view.findViewById(R.id.beef20);
        beef25 = view.findViewById(R.id.beef25);
        beef50 = view.findViewById(R.id.beef50);
        beef100 = view.findViewById(R.id.beef100);

        fish5 = view.findViewById(R.id.fish5);
        fish20 = view.findViewById(R.id.fish20);
        fish25 = view.findViewById(R.id.fish25);
        fish50 = view.findViewById(R.id.fish50);
        fish100 = view.findViewById(R.id.fish100);

        dairy5 = view.findViewById(R.id.dairy5);
        dairy20 = view.findViewById(R.id.dairy20);
        dairy25 = view.findViewById(R.id.dairy25);
        dairy50 = view.findViewById(R.id.dairy50);
        dairy100 = view.findViewById(R.id.dairy100);

        salad5 = view.findViewById(R.id.salad5);
        salad20 = view.findViewById(R.id.salad20);
        salad25 = view.findViewById(R.id.salad25);
        salad50 = view.findViewById(R.id.salad50);
        salad100 = view.findViewById(R.id.salad100);

        rice5 = view.findViewById(R.id.rice5);
        rice20 = view.findViewById(R.id.rice20);
        rice25 = view.findViewById(R.id.rice25);
        rice50 = view.findViewById(R.id.rice50);
        rice100 = view.findViewById(R.id.rice100);

        // initialize the button and TextView
        submitB = view.findViewById(R.id.submitButton);
        resultView = view.findViewById(R.id.resultBox);

        // initialize request queue
        queue = Volley.newRequestQueue(getContext());
        // initialize ViewModel if needed
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsVM.class);

        // Submit button's functionality
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        return view;
    }

    public void submit() {
        // getting the sum of checked values for each category.
        int beefVal = ApiHandler.getInstance().getPercentage(beef5.isChecked(), beef20.isChecked(), beef25.isChecked(), beef50.isChecked(), beef100.isChecked());
        int fishVal = ApiHandler.getInstance().getPercentage(fish5.isChecked(), fish20.isChecked(), fish25.isChecked(), fish50.isChecked(), fish100.isChecked());
        int dairyVal = ApiHandler.getInstance().getPercentage(dairy5.isChecked(), dairy20.isChecked(), dairy25.isChecked(), dairy50.isChecked(), dairy100.isChecked());
        int saladVal = ApiHandler.getInstance().getPercentage(salad5.isChecked(), salad20.isChecked(), salad25.isChecked(), salad50.isChecked(), salad100.isChecked());
        int riceVal = ApiHandler.getInstance().getPercentage(rice5.isChecked(), rice20.isChecked(), rice25.isChecked(), rice50.isChecked(), rice100.isChecked());
        // above values are passed forward to the ApiHandler class for the request
        ApiHandler.getInstance().getTotalConsumption(beefVal, fishVal, dairyVal, saladVal, riceVal, this, queue, "Swagger_UI_FoodCalculator");
        return;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onResponseFinished(@NonNull JSONObject response) {
        // when request is finished
        try {
            // Gets the wanted value from the JSON-object
            double total = response.getDouble("Total");
            // sets the result into the TextView component
            resultView.setText(String.format("Result: %.2f kg/year (CO2)", total));
            /* Tries to save the calculated value and returns 'true' if able to
            * and false if there's no today's data present */
            boolean saved = DataHandler.getInstance().updateLatestAPIEntry(total);
            if (!saved) {
                // informs the user to go give the application data to use.
                Toast.makeText(getContext(), "Please go fill default profile data before trying to save CO2 data.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // when there's a problem with the JSON object:
            Toast.makeText(getContext(), "Error with return data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(@NonNull VolleyError error) {
        // if request fails
        Toast.makeText(getContext(), "Error while fetching data.", Toast.LENGTH_SHORT).show();
    }
}
