package com.example.OlioHT;

public class BMIHandler {
    /* BMI Calculations
    * Singleton class
    * Used mainly in BMICalculator */

    private static BMIHandler handler = new BMIHandler();

    private BMIHandler() {}

    public static BMIHandler getInstance() {return handler;}

    public String getBMIText(double BMI) {
        // gets the respective text value for given BMI
        String text = "";
        if (BMI < 16) {
            text = "Severely underweight";
        } else if (BMI < 17) {
            text = "Moderately underweight";
        } else if (BMI < 18.5) {
            text = "Slightly underweight";
        } else if (BMI < 25) {
            text = "Normal";
        } else if (BMI < 30) {
            text = "Overweight";
        } else if (BMI < 35) {
            text = "Slightly obese";
        } else if (BMI < 40) {
            text = "Moderately obese";
        } else {
            text = "Severely obese";
        }
        return text;
    }

    // Functions for the seek bar values @ BMI calc

    public double getSeekWeight(double weight, int height, double bmi, double dbmi) {
        // gets the users weight, height and bmi + the selected BMI
        double dweight = 0;
        // calculates the weight value required for the selected bmi
        dweight = height * height * (dbmi - bmi) / 10000 + weight;
        return dweight;
    }

    public double getSeekDiff(int height, double bmi, double dbmi) {
        // takes user's height and bmi + selected bmi
        double dchange = 0;
        // calculates the required weight change to achieve the selected BMI
        dchange = height * height * (dbmi - bmi) / 10000;
        return dchange;
    }
}
