package com.example.week11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener{

    private NavigationView navMenu;
    private DrawerLayout mainDrawer;
    private Toolbar actionBar;
    private FrameLayout fragmentView;
    private FragmentManager fragManager;
    private boolean drawerState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inits
        setContentView(R.layout.activity_main);
        mainDrawer = findViewById(R.id.main_drawer);
        actionBar = findViewById(R.id.actionbar);
        navMenu = findViewById(R.id.navigation);
        fragmentView = findViewById(R.id.fragment_container);
        fragManager = getSupportFragmentManager();
        new ViewModelProvider(this).get(SettingsVM.class).initSettings();

        // actionbar and navigation menu inits
        setSupportActionBar(actionBar);
        ActionBarDrawerToggle toggler = new ActionBarDrawerToggle(this, mainDrawer, actionBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawer.addDrawerListener(toggler);
        mainDrawer.addDrawerListener(this);
        toggler.syncState();

        navMenu.setNavigationItemSelectedListener(this);

        changeFragment(new homeFragment());
    }

    // navigation menu item selections
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                changeFragment(new homeFragment());
                break;
            case R.id.settings:
                changeFragment(new settingsFragment());
                break;
        }
        mainDrawer.closeDrawer(Gravity.LEFT);
        return true;
    }

    // function for fragment change
    private void changeFragment(@NonNull Fragment newFragment) {
        FragmentTransaction fragmentChanger = fragManager.beginTransaction();
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

}