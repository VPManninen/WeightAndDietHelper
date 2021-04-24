package com.example.OlioHT;

import java.util.ArrayList;

public class DataParser {
    /* History fragment uses parsed data (well so far only parsed by date.)
    * this class creates the required operations to display the wanted data*/

    private static DataParser parser = new DataParser();
    private ArrayList<String> dataSets;
    private ArrayList<String> timeSets;

    private DataParser() {
        /* ArrayLists the spinners use */
         dataSets = new ArrayList<>();
         dataSets.add("Weight");
         dataSets.add("Height");
         dataSets.add("BMI");
         dataSets.add("Food CO2");
         timeSets = new ArrayList<>();
         timeSets.add("Daily");
    }

    public static DataParser getInstance() {
        return parser;
    }

    // functions for the spinner inits.
    public ArrayList<String> getDataSets() {
        return dataSets;
    }

    public ArrayList<String> getTimeSets() {
        return timeSets;
    }

    public ArrayList<String> parseData(String parse, String parseBy, ArrayList<Data> data) {
        // Parser function - Takes the time frame and pushes the parsing forward
        ArrayList<String> parsedData = new ArrayList<>();
        if (data != null) {
            if (data.isEmpty()) {
                parsedData.add("No user data found");
            } else {
                // if we have data to parse
                switch (parseBy) {
                    // we could add other time frames here... and create the required method
                    case ("Daily"):
                        parsedData = parseDaily(parse, data);
                        break;
                }
            }
            if (data.isEmpty()) {
                parsedData.add("No user data found");
            }
        } else {
            parsedData.add("No user data found");
        }
        return parsedData;
    }

    private ArrayList<String> parseDaily(String parse, ArrayList<Data> data){
        /* This function parses the given data by date and given data set
        * as the data is only inserted daily we have to get all entries into the list */
        ArrayList<String> parsedData = new ArrayList<>();
        for (Data i : data) {
            // switch to check which data we wanted to display
            switch (parse) {
                case("Weight"):
                    parsedData.add(String.format("%s: %.1f kg", i.getDate(), i.getWeight()));
                    break;
                case("Height"):
                    parsedData.add(String.format("%s: %d cm", i.getDate(), i.getHeight()));
                    break;
                case("BMI"):
                    parsedData.add(String.format("%s: %.1f kg/m2", i.getDate(), i.getBmi()));
                    break;
                case("Food CO2"):
                    // NON zero entries
                    if (i.getApi() != 0) {
                        parsedData.add(String.format("%s: %.2f kg/year", i.getDate(), i.getApi()));
                    }
                    break;
            }
        }
        return parsedData;
    }
}
