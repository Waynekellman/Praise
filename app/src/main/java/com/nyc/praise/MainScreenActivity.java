package com.nyc.praise;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class MainScreenActivity extends AppCompatActivity implements FragmentNavigator {

    private FragmentTransaction transaction;
    private MainFeed mainFeed;
    @Inject Set<String> locations;
    private String TAG = MainScreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, MODE_PRIVATE);
        locations = sharedPreferences.getStringSet(Constants.LOCATIONS_KEY, new HashSet<>());

        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        mainFeed = new MainFeed();
        transaction.replace(R.id.fragment_feed_container, mainFeed);
        transaction.commit();

    }

    @Override
    public void SwitchFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_feed_container, fragment);
        ft.addToBackStack("");
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (locations!= null && !locations.isEmpty()){
            int count = 0;
            for (String location : locations) {
                menu.add(Menu.NONE,count,Menu.NONE, location);
                count++;
            }
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int itemID = item.getItemId();
        if (itemID == R.id.current_location_menu) {
            mainFeed.setCurrentLocation();
            return true;
        }else if ( itemID == R.id.add_location) {
            if (mainFeed != null) {
                mainFeed.addThisLocation();
            }
            return true;
        }else if (locations.contains(item.getTitle().toString())){
            if (mainFeed!= null){
                mainFeed.connectMainFeed(item.getTitle().toString());
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
