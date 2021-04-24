package com.example.OlioHT;

import android.widget.Toast;

import com.example.OlioHT.Fragments.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LoginEtRegHandler {
    /* Brains for user login and registration
    * Singleton */

    // Allowed special characters
    private static final String SPECIAL_CHARS = " \"\\''!#$%&()*+-/<=>?@[]{|}_";
    private static LoginEtRegHandler handler = new LoginEtRegHandler();
    private String currentDayString;
    private String[] currentDay;
    private int dateVal;
    private ArrayList<Integer> monthsWithLessDays;

    private LoginEtRegHandler() {
        // initialize date comparison sets
        currentDayString = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
        currentDay = currentDayString.split("\\.");
        dateVal = Integer.parseInt(String.format("%s%s%s", currentDay[2], currentDay[1], currentDay[0]));
        monthsWithLessDays = new ArrayList<>();
        monthsWithLessDays.add(2);
        monthsWithLessDays.add(4);
        monthsWithLessDays.add(6);
        monthsWithLessDays.add(9);
        monthsWithLessDays.add(11);
    }

    public static LoginEtRegHandler getInstance() {
        return handler;
    }

    // function which checks if given login info was correct
    public Boolean checkLogin(String userEntered, String passEntered) {
        User checkUser = SqlManager.getInstance().getByUsername(userEntered);
        if (checkUser != null) {
            if (checkUser.getPassword().equals(passEntered)) {
                return true;
            }
        }
        return false;
    }

    public void registerAccount(User user) {
        SqlManager.getInstance().addUser(user);
    }

    // function for date checking - whether the date exists or not.
    public boolean checkEnteredDate(String[] dateEntered) {
        try {
            int dateEnteredVal = Integer.parseInt(String.format("%s%s%s", dateEntered[2], dateEntered[1], dateEntered[0]));
            if ((dateEnteredVal > dateVal) || (Integer.parseInt(dateEntered[0]) > 31) || (Integer.parseInt(dateEntered[1]) > 12) || (Integer.parseInt(dateEntered[1]) < 1) || (Integer.parseInt(dateEntered[0]) < 1)) {
                return false;
                // this takes all entered dates in the future, and restricts the values entered
            } else if ((Integer.parseInt(dateEntered[0]) > 30) && (monthsWithLessDays.contains(Integer.parseInt(dateEntered[1])))) {
                return false;
                // takes all months with 30 days when > 30 entered
            } else if ((Integer.parseInt(dateEntered[0]) > 29) && (Integer.parseInt(dateEntered[1]) == 2)) {
                return false;
                // checks for dates in February
            } else if ((Integer.parseInt(dateEntered[0]) == 29) && (Integer.parseInt(dateEntered[1]) == 2) && !(Integer.parseInt(dateEntered[2]) % 4 == 0)) {
                return false;
                // checks if 29.02 is entered on a non leap year
            } else {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkEnteredPass(String pass) {
        /* checks if the password is secure enough
        * - longer than 12
        * - contains a digit
        * - contains a special character
        * - contains a capital letter
        * - contains a letter */
        if (pass.length() < 12) {
            return false;
        } else {
            boolean special = false;
            boolean number = false;
            boolean capital = false;
            boolean letter = false;
            // cycle through the password
            for (int i = 0; i < pass.length(); i++) {
                char c = pass.charAt(i);
                // special char
                if (SPECIAL_CHARS.contains(Character.toString(c))) {
                    special = true;
                } else if (Character.isDigit(c)) {
                    // digit
                    number = true;
                } else if (Character.isUpperCase(c)) {
                    // CAPS
                    capital = true;
                } else if (Character.isLowerCase(c)) {
                    // non CAPS
                    letter = true;
                }
            }
            // if all requirements are met: return true
            if (special && number && capital && letter) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean checkEnteredUser(String user) {
        // checks the entered username
        if (user.length() < 5) {
            // length
            return false;
        } else if (user.contains(SPECIAL_CHARS)) {
            // if it contains illegal characters
            return false;
        } else {
            // if a user by name exists already
            User checkUser = SqlManager.getInstance().getByUsername(user);
            if (checkUser != null) {
                return false;
            }
        }
        return true;
    }

    public boolean confirmPWChange(User testUser) {
        // password change confirmation
        User checkUser = SqlManager.getInstance().getByUsername(testUser.getUsername());
        /* compares the testUser's and existing user's dates of birth (and user name SQLite)
        * if they match change the password (not here...)*/
        if (checkUser.getDateOfBirth().equals(testUser.getDateOfBirth())) {
            return true;
        } else {
            return false;
        }
    }
}
