package com.nyc.praise;

/**
 * Created by Wayne Kellman on 5/12/18.
 */

public class MainFeedPresenter {
    private GPSHelper gpsHelper;

    private GetLocation locationInterface;


    public MainFeedPresenter(GPSHelper gpsHelper, GetLocation locationInterface) {
        this.gpsHelper = gpsHelper;
        this.locationInterface = locationInterface;
        setGPS();
    }
    interface GetLocation{
        void getLocation(String currentLocation);
    }

    private void setGPS() {
        gpsHelper.getMyLocation();
        gpsHelper.setLocationInterface(getLocationInterface());
    }

    private GPSHelper.LocationInterface getLocationInterface() {
        return currentLocation -> locationInterface.getLocation(currentLocation);

    }

    public void disconnect(){
        gpsHelper.disconnect();
    }
}
