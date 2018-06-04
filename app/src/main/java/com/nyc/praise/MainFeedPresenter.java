package com.nyc.praise;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Wayne Kellman on 5/12/18.
 */

public class MainFeedPresenter {
    MainFeed mainFeedContext;


    private PraiseFireBaseRecyclerAdapter praiseFireBaseRecyclerAdapter;
    private DatabaseReference databaseReferenceFeed;
    String currentLocation;
    private final String TAG = "MainFeedPresenter";
    private View view;

    private TextView currentLocationTextView;
    private ImageView sendPraise;
    private String USER_NAME;

    // Declare the RecyclerView and the LinearLayoutManager first
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;


    public MainFeedPresenter(MainFeed mainFeedContext, View v) {
        this.mainFeedContext = mainFeedContext;
        this.view = v;
        SharedPreferences sharedPreferences = mainFeedContext.getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        USER_NAME = sharedPreferences.getString(Constants.LOGIN_USERNAME, "No User");
    }

    public void start() {
        setGPS(new GPSHelper(mainFeedContext.getActivity()));

        currentLocationTextView = view.findViewById(R.id.current_location);
        sendPraise = view.findViewById(R.id.write_praise);
        sendPraise.setOnClickListener(view -> initWritePraiseFrag());
    }

    private void initWritePraiseFrag() {
        WritePraiseFragment fragment = new WritePraiseFragment();
        Bundle newPraiseBundle = new Bundle();
        newPraiseBundle.putBoolean("NewPraise", true);
        newPraiseBundle.putString(Constants.LOCATION, currentLocation);
        fragment.setArguments(newPraiseBundle);
        switchContent(fragment);
    }

    private void setGPS(GPSHelper gpsHelper) {
        gpsHelper.getMyLocation();
        gpsHelper.setLocationInterface(getLocationInterface());
    }

    @NonNull
    private GPSHelper.LocationInterface getLocationInterface() {
        return currentLocation -> {
            Log.d(TAG, "onSuccessLocation: ran");
            MainFeedPresenter.this.currentLocation = currentLocation;
            connectMainFeed(currentLocation);
        };
    }

    public void connectMainFeed(final String currentLocation) {
        Log.d(TAG, "connectMainFeed: ran");
        currentLocationTextView.setText(currentLocation.replace(" County", ""));
        databaseReferenceFeed = FirebaseDatabase.getInstance().getReference();

        praiseFireBaseRecyclerAdapter = getPraiseFireBaseRecyclerAdapter(currentLocation);
        praiseFireBaseRecyclerAdapter.setUSER_NAME(USER_NAME);
        praiseFireBaseRecyclerAdapter.setMainFeedContext(mainFeedContext);

        mLayoutManager = new LinearLayoutManager(mainFeedContext.getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.main_feed_recycler);
        recyclerView.setAdapter(praiseFireBaseRecyclerAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @NonNull
    private PraiseFireBaseRecyclerAdapter getPraiseFireBaseRecyclerAdapter(String currentLocation) {
        return new PraiseFireBaseRecyclerAdapter(
                PraiseModel.class,
                R.layout.main_feed_itemview, FireBaseViewHolder.class,
                databaseReferenceFeed.child(Constants.FEED).child(currentLocation));
    }


    public void switchContent(Fragment fragment) {

        if (mainFeedContext.getActivity() instanceof MainScreenActivity) {
            FragmentNavigator homeActivity = (FragmentNavigator) this.mainFeedContext.getActivity();
            homeActivity.SwitchFragment(fragment);
        }

    }
}
