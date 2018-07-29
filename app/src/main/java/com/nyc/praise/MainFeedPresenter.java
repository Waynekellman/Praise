package com.nyc.praise;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Wayne Kellman on 5/12/18.
 */

public class MainFeedPresenter {
    private GPSHelper gpsHelper;
    private IMainFeed mainFeed;
    private String currentLocation;
    Set<String> locations;
    private String TAG = MainFeedPresenter.class.getSimpleName();


    public MainFeedPresenter(GPSHelper gpsHelper, IMainFeed mainFeed) {
        this.gpsHelper = gpsHelper;
        this.mainFeed = mainFeed;
    }

    public void addThisLocation() {
        if (locations == null){
            locations = new HashSet<>();
        }
        if (currentLocation != null && !locations.contains(currentLocation)){
            locations.add(currentLocation);
            for (String locale : locations) {
                Log.d(TAG, "location - presenter " + locale);
            }
            mainFeed.locationAdded(currentLocation);
            mainFeed.addSetToPreferences(locations);
        }
    }

    public void setLocation(Set<String> locations) {
        this.locations = locations;
    }

    public interface GetLocation{
        void getLocation(String currentLocation);
    }

    public void setGPS(GetLocation locationInterface) {
        gpsHelper.setLocationInterface(currentLocation -> {
            MainFeedPresenter.this.currentLocation = currentLocation;
            locationInterface.getLocation(currentLocation);
            disconnect();
        });
        gpsHelper.startGoogleApiClient();
    }

    public String getCurrentLocation(){
        if (currentLocation!= null) {
            return currentLocation;
        }
        return Constants.NOT_FOUND;
    }

    public void disconnect(){
        gpsHelper.disconnect();
    }
}
