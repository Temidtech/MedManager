package com.swiftsynq.medmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.swiftsynq.medmanager.Fragment.HomeFragment;
import com.swiftsynq.medmanager.data.MedManagerPreferences;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment=HomeFragment.newInstance();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment=HomeFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment=HomeFragment.newInstance();
                    break;
            }
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.main_container,selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toast.makeText(this, MedManagerPreferences.getUserDetails(this).getEmail(),Toast.LENGTH_LONG).show();
        getSupportActionBar().setTitle(MedManagerPreferences.getUserDetails(this).getDisplayName());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_container,HomeFragment.newInstance()).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionable_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_medication:
                Intent intent = new Intent(this, MedicationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            case R.id.action_logout:
                MedManagerPreferences.resetUserDetails(getBaseContext());
                Intent logout = new Intent(this, MainActivity.class);
                startActivity(logout);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
