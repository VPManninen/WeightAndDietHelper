package com.example.OlioHT;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public final class SqlManager {
    /* SQLite database for application data storage
    * Database contains 3 tables:
    * - Data table
    * - User table
    * - Saved login table
    * Each has its own scheme (below) and is initialized when application is first opened
    * User table contains all user entries (username, password, date of birth) (1)
    * Data table contains data entries for all users and its contents can be viewed here (2)
    * Save table is used for saved login storage when application is closed. (3)*/

    private static SqlManager dataManager = new SqlManager();
    private SQLiteHelper sqlHelper;
    private SQLiteDatabase database;

    // Table information for saving users (1):
    private static final String USER_TABLE_NAME = "user_table";
    private static final String USER_NAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_DOB = "date_of_birth";

    // Table information for saving user's data (2):
    private static final String USER_DATA_TABLE_NAME = "user_data_table";
    private static final String USER_DATA_DATE = "d_data";
    private static final String USER_DATA_PER_NAME = "name";
    private static final String USER_DATA_WEIGHT = "weight";
    private static final String USER_DATA_HEIGHT = "height";
    private static final String USER_DATA_BMI = "bmi";
    private static final String USER_DATA_API = "api";
    private static final String USER_DATA_USER_ID = "user_id";

    // Table information for saving login (3):
    private static final String SAVE_TABLE_NAME = "save_table";
    private static final String SAVE_USER_ID = "saved_user";
    private static final String SAVE_STATE = "state";

    // SQLite table creation schemas stored as a string
    private static final String CREATE_USER_TABLE = String.format("create table %s (ID integer primary key autoincrement, %s TEXT NOT NULL, %s TEXT NOT NULL, %s STRING NOT NULL);", USER_TABLE_NAME, USER_NAME, USER_PASSWORD, USER_DOB);
    private static final String CREATE_USER_DATA_TABLE = String.format("create table %s (ID integer primary key autoincrement, %s TEXT NOT NULL, %s TEXT NOT NULL, %s DECIMAL(3,1) NOT NULL, %s INTEGER NOT NULL, %s DECIMAL(3,1) NOT NULL, %s STRING NOT NULL, %s DECIMAL(4,2) NOT NULL, foreign key (%s) references %s (ID));", USER_DATA_TABLE_NAME, USER_DATA_DATE, USER_DATA_PER_NAME, USER_DATA_WEIGHT, USER_DATA_HEIGHT, USER_DATA_BMI, USER_DATA_API, USER_DATA_USER_ID, USER_DATA_USER_ID, USER_TABLE_NAME);
    private static final String CREATE_SAVE_TABLE = String.format("create table %s (ID integer primary key autoincrement, %s INTEGER NOT NULL, %s INTEGER NOT NULL);", SAVE_TABLE_NAME, SAVE_USER_ID, SAVE_STATE);

    private SqlManager() {
        sqlHelper = null;
        database = null;
    }

    // SQLiteHelper class which is used to create the database and manages it.
    private final class SQLiteHelper extends SQLiteOpenHelper {
        public static final int DB_VERSION = 1;
        public static final String DB_NAME = "database.db";

        public SQLiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // creating the 3 different tables
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_USER_DATA_TABLE);
            db.execSQL(CREATE_SAVE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    // singleton "getInstance()" function
    public static SqlManager getInstance() {
        return dataManager;
    }

    // Checks if the database is usable and opened
    private boolean databaseUsable() {
        if (database == null) {
            if (sqlHelper == null) {
                System.out.println("helper = null");
                return false;
            }
            database = sqlHelper.getWritableDatabase();
        }
        // initializes the saved user with 0's which then can be updated when remembering users
        database.execSQL(String.format("INSERT INTO %s (%s, %s) VALUES (%d, %d)", SAVE_TABLE_NAME, SAVE_USER_ID, SAVE_STATE, 0, 0));
        return true;
    }

    // function to add a new user to the database
    public boolean addUser(@NonNull User user) {
        if (!databaseUsable()) {
            return false;
        }
        database.execSQL(String.format("INSERT INTO %s (%s, %s, %s) VALUES (\"%s\", \"%s\", \"%s\")", USER_TABLE_NAME, USER_NAME, USER_PASSWORD, USER_DOB, user.getUsername(), user.getPassword(), user.getDateOfBirth()));
        return true;
    }

    // adding a data entry for the current user
    public boolean addData(@NonNull Data data) {
        if (!databaseUsable()) {
            return false;
        }
        database.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (\"%s\", \"%s\", %.1f, %d, %.1f, %.2f, %d)", USER_DATA_TABLE_NAME, USER_DATA_DATE, USER_DATA_PER_NAME, USER_DATA_WEIGHT, USER_DATA_HEIGHT, USER_DATA_BMI, USER_DATA_API, USER_DATA_USER_ID, data.getDate(), data.getName(), data.getWeight(), data.getHeight(), data.getBmi(), data.getApi(), data.getUserId()));
        return true;
    }

    // function to get all data from one user
    public ArrayList<Data> getDataByUser(User user) {
        if (!databaseUsable()) {
            return null;
        }
        ArrayList<Data> dataList = new ArrayList<Data>();
        // selects all the data for the user
        Cursor selection = database.rawQuery(String.format("SELECT * FROM %s WHERE %s = %d;", USER_DATA_TABLE_NAME, USER_DATA_USER_ID, user.getUserID()), null);
        if (selection != null) {
            dataList = new ArrayList<>();
            while (selection.moveToNext()) {
                // saves the Data entry into a list
                dataList.add(new Data(selection.getInt(selection.getColumnIndex("ID")),
                        selection.getString(selection.getColumnIndex(USER_DATA_DATE)),
                        selection.getString(selection.getColumnIndex(USER_DATA_PER_NAME)),
                        selection.getDouble(selection.getColumnIndex(USER_DATA_WEIGHT)),
                        selection.getInt(selection.getColumnIndex(USER_DATA_HEIGHT)),
                        selection.getDouble(selection.getColumnIndex(USER_DATA_BMI)),
                        selection.getInt(selection.getColumnIndex(USER_DATA_API)),
                        selection.getInt(selection.getColumnIndex(USER_DATA_USER_ID))));
            }
            if (dataList.size() == 0) {
                return null;
            }
        }
        return dataList;
    }

    // function for updating name, weight, height, bmi and api data from today's entry.
    public boolean updateUserData(Data data) {
        if (!databaseUsable()) {
            return false;
        }
        database.execSQL(String.format("UPDATE %s SET %s = \"%s\", %s = %.1f, %s = %d, %s = %.1f, %s = %.2f WHERE %s = %d AND %s = \"%s\";", USER_DATA_TABLE_NAME, USER_DATA_PER_NAME, data.getName(), USER_DATA_WEIGHT, data.getWeight(), USER_DATA_HEIGHT, data.getHeight(), USER_DATA_BMI, data.getBmi(), USER_DATA_API, data.getApi(), USER_DATA_USER_ID, data.getUserId(), USER_DATA_DATE, data.getDate()));
        return true;
    }

    // returns a User from the table with given username
    public User getByUsername(String nameEntered) {
        if (!databaseUsable()) {
            return null;
        }
        User findUser = null;
        Cursor selection = database.rawQuery(String.format("SELECT * FROM %s WHERE %s = \"%s\";", USER_TABLE_NAME, USER_NAME, nameEntered), null);
        if (selection != null) {
            if (selection.moveToFirst()) {
                findUser = new User(selection.getInt(selection.getColumnIndex("ID")),
                        selection.getString(selection.getColumnIndex(USER_NAME)),
                        selection.getString(selection.getColumnIndex(USER_PASSWORD)),
                        selection.getString(selection.getColumnIndex(USER_DOB)));
                selection.close();
            }
        }
        return findUser;
    }

    // updates the "saved user" table with a new user and remember state (if set to 1 logs the user in when application is opened)
    public boolean updateSaveState(User user, int state) {
        if (!databaseUsable()) {
            return false;
        }
        database.execSQL(String.format("UPDATE %s SET %s = %d, %s = %d WHERE ID = 1", SAVE_TABLE_NAME, SAVE_USER_ID, user.getUserID(), SAVE_STATE, state));
        return true;
    }

    // Function for checking if login was remembered
    public User getSavedUser() {
        if (!databaseUsable()) {
            return null;
        }
        // initializes the values
        int userID = 0;
        int saveState = 0;
        // first we check if there is a login that is saved
        Cursor selectionSave = database.rawQuery(String.format("SELECT * FROM %s;", SAVE_TABLE_NAME), null);
        if (selectionSave != null) {
            if (selectionSave.moveToFirst()) {
                userID = selectionSave.getInt(selectionSave.getColumnIndex(SAVE_USER_ID));
                saveState = selectionSave.getInt(selectionSave.getColumnIndex(SAVE_STATE));
                selectionSave.close();
            }
        }
        User findUser = null;
        // then if we have found a positive user id and it was set to be remembered we get that user from the user table
        if ((userID > 0) && (saveState == 1)) {
            Cursor selectionUser = database.rawQuery(String.format("SELECT * FROM %s WHERE ID = %d;", USER_TABLE_NAME, userID), null);
            if (selectionUser != null) {
                if (selectionUser.moveToFirst()) {
                    findUser = new User(selectionUser.getInt(selectionUser.getColumnIndex("ID")),
                            selectionUser.getString(selectionUser.getColumnIndex(USER_NAME)),
                            selectionUser.getString(selectionUser.getColumnIndex(USER_PASSWORD)),
                            selectionUser.getString(selectionUser.getColumnIndex(USER_DOB)));
                    selectionUser.close();
                }
            }
        }
        // returns null if we received a negative result for remembered login
        // if we have a remembered login then it returns the user
        return findUser;
    }

    // function for changing a user's password if username and date of birth entered correctly
    public boolean changeUserPassword(User user) {
        if (!databaseUsable()) {
            return false;
        }
        database.execSQL(String.format("UPDATE %s SET %s = \"%s\" WHERE %s = \"%s\";", USER_TABLE_NAME, USER_PASSWORD, user.getPassword(), USER_NAME, user.getUsername()));
        return true;
    }

    // function for closing the database
    public void closeDatabase() {
        if (database != null) {
            database.close();
        }
    }

    // helper initializer
    public void initializeDataManager(@NonNull Context context) {
        sqlHelper = new SQLiteHelper(context);
    }
}
