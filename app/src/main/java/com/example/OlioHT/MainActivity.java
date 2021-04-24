package com.example.OlioHT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.OlioHT.Fragments.AboutFragment;
import com.example.OlioHT.Fragments.ApiFragment;
import com.example.OlioHT.Fragments.BmiFragment;
import com.example.OlioHT.Fragments.HistoryFragment;
import com.example.OlioHT.Fragments.HomeFragment;
import com.example.OlioHT.Fragments.LoginFragment;
import com.example.OlioHT.Fragments.ProfileDataFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener, IFragmentOwner {
    /* MainActivity for Weight and Diet helper
    * Ville-Petteri Manninen - 24.04.2020*/

    // intis
    private NavigationView navMenu;
    private DrawerLayout mainDrawer;
    private Toolbar actionBar;
    private FrameLayout fragmentView;
    private FragmentManager fragManager;
    private boolean drawerState = false;
    private SettingsVM settingsVM;
    private Context context;
    private SqlManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the main activity items
        setContentView(R.layout.activity_main);
        mainDrawer = findViewById(R.id.main_drawer);
        actionBar = findViewById(R.id.actionbar);
        navMenu = findViewById(R.id.navigation);
        fragmentView = findViewById(R.id.fragment_container);
        fragManager = getSupportFragmentManager();
        // Initialize ViewModel
        settingsVM = new ViewModelProvider(this).get(SettingsVM.class);
        settingsVM.initSettings();
        // Initialize DataManager
        context = this;
        dataManager = SqlManager.getInstance();
        dataManager.initializeDataManager(context);
        // Initialize custom actionbar and drawer
        setSupportActionBar(actionBar);
        ActionBarDrawerToggle toggler = new ActionBarDrawerToggle(this, mainDrawer, actionBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawer.addDrawerListener(toggler);
        mainDrawer.addDrawerListener(this);
        toggler.syncState();

        // navigation menu listener
        navMenu.setNavigationItemSelectedListener(this);

        // checks if we have remembered user and initializes the application accordingly
        User remembered = SqlManager.getInstance().getSavedUser();
        if ((remembered != null)) {
            // remembered
            settingsVM.changeCurrentUser(remembered);
            DataHandler.getInstance().getUserData(settingsVM.getUser());
            DataHandler.getInstance().addDailyData(settingsVM.getUser());
            changeFragment(new HomeFragment(), false, false);
        } else {
            // not remembered
            changeFragment(new LoginFragment(), false, true);
        }
    }

    // navigation menu item selections by implementation to MainActivity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                changeFragment(new HomeFragment(), true, false);
                break;
            case R.id.profileData:
                changeFragment(new ProfileDataFragment(), true, false);
                break;
            case R.id.logout:
                SqlManager.getInstance().updateSaveState(settingsVM.getUser(), 0);
                changeFragment(new LoginFragment(), false, true);
                break;
            case R.id.api:
                changeFragment(new ApiFragment(), true, false);
                break;
            case R.id.bmi:
                changeFragment(new BmiFragment(), true, false);
                break;
            case R.id.history:
                changeFragment(new HistoryFragment(), true, false);
                break;
            case R.id.about:
                changeFragment(new AboutFragment(), true, false);
                break;
        }
        mainDrawer.closeDrawer(Gravity.LEFT);
        return true;
    }

    // function for fragment change as an implemented method through an interface.
    @Override
    public void changeFragment(@NonNull Fragment newFragment, boolean addToBackStack, boolean lockDrawer) {
        FragmentTransaction fragmentChanger = fragManager.beginTransaction();
        if (addToBackStack) {
            fragmentChanger.addToBackStack(null);
        }
        // locks the drawer when not allowed to use it
        if (lockDrawer) {
            mainDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            mainDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        if (newFragment instanceof HomeFragment) {
            navMenu.setCheckedItem(R.id.home);
        }
        fragmentChanger.replace(R.id.fragment_container, newFragment);
        fragmentChanger.commit();
    }

    // closes the drawer when back is pressed
    @Override
    public void onBackPressed() {
        if (drawerState) {
            mainDrawer.closeDrawer(Gravity.LEFT);
            return;
        }
        super.onBackPressed();
    }

    // extra overrides
    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    // change the drawer state boolean when drawer is open/closed
    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        drawerState = true;
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        drawerState = false;
    }

    // extra overrides
    @Override
    public void onDrawerStateChanged(int newState) {
    }

    @Override
    public void onDestroy() {
        dataManager.closeDatabase();
        super.onDestroy();
    }
}