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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wayne Kellman on 5/12/18.
 */

public class MainFeedPresenter {
    MainFeed mainFeedContext;


    private FirebaseRecyclerAdapter<PraiseModel, FireBaseViewHolder> praiseFireBaseRecyclerAdapter;
    private DatabaseReference databaseReferenceFeed;
    String currentLocation;
    private final String TAG = "MainFeedPresenter";
    private View view;

    private TextView currentLocationTextView;
    private ImageView sendPraise;
    private String USER_NAME;

    // Declare the RecyclerView and the LinearLayoutManager first
    private RecyclerView listView;
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
        sendPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WritePraiseFragment fragment = new WritePraiseFragment();
                Bundle newPraiseBundle = new Bundle();
                newPraiseBundle.putBoolean("NewPraise", true);
                newPraiseBundle.putString(Constants.LOCATION, currentLocation);
                fragment.setArguments(newPraiseBundle);
                switchContent(fragment);
            }
        });
    }

    private void setGPS(GPSHelper gpsHelper) {
        gpsHelper.getMyLocation();
        gpsHelper.setLocationInterface(getLocationInterface());
    }

    @NonNull
    private GPSHelper.LocationInterface getLocationInterface() {
        return new GPSHelper.LocationInterface() {
            @Override
            public void onSuccessLocation(String currentLocation) {
                Log.d(TAG, "onSuccessLocation: ran");
                MainFeedPresenter.this.currentLocation = currentLocation;
                connectMainFeed(currentLocation);
            }
        };
    }

    public void connectMainFeed(final String currentLocation) {
        Log.d(TAG, "connectMainFeed: ran");
        currentLocationTextView.setText(currentLocation.replace(" County", ""));
        databaseReferenceFeed = FirebaseDatabase.getInstance().getReference();
        praiseFireBaseRecyclerAdapter = new FirebaseRecyclerAdapter<PraiseModel, FireBaseViewHolder>(
                PraiseModel.class, R.layout.main_feed_itemview, FireBaseViewHolder.class,
                databaseReferenceFeed.child(Constants.FEED).child(currentLocation)) {
            @Override
            protected void populateViewHolder(FireBaseViewHolder viewHolder, final PraiseModel model, int position) {

                viewHolder.OnBind(model);
                ValueEventListener listenerForLikes = getLikeValueEventListener(model, currentLocation, viewHolder);

                databaseReferenceFeed.child(Constants.FEED).child(currentLocation).child(model.getuId()).child(Constants.LIKES).addValueEventListener(listenerForLikes);

                viewHolder.commentImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommentsFragment fragment = new CommentsFragment();
                        String modelJsonString = new Gson().toJson(model);
                        Bundle bundle = new Bundle();
                        bundle.putString("jsonModel", modelJsonString);
                        fragment.setArguments(bundle);
                        switchContent(fragment);
                    }
                });
            }
        };

        mLayoutManager = new LinearLayoutManager(mainFeedContext.getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        listView = view.findViewById(R.id.main_feed_recycler);
        listView.setAdapter(praiseFireBaseRecyclerAdapter);
        listView.setLayoutManager(mLayoutManager);
    }

    @NonNull
    private ValueEventListener getLikeValueEventListener(final PraiseModel model, final String currentLocation, final FireBaseViewHolder viewHolder) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String likeCountSnapshot = dataSnapshot.getChildrenCount() + "";
                Log.d(TAG, "onDataChange: likes count " + likeCountSnapshot);
                Map<String, String> UsersWhoLiked = createMapOfLikesBYUser(dataSnapshot);

                setLikeButtonEnabledStatus(UsersWhoLiked, currentLocation, model, viewHolder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void setLikeButtonEnabledStatus(Map<String, String> usersWhoLiked, final String currentLocation, final PraiseModel model, final FireBaseViewHolder viewHolder) {
        Log.d(TAG, "setLikeButtonEnabledStatus: model " + model.getMessage());
        Log.d(TAG, "setLikeButtonEnabledStatus: ran");
        if (usersWhoLiked.containsKey(USER_NAME)) {
            for (String namesInMap : usersWhoLiked.keySet()) {
                Log.d(TAG, "setLikeButtonEnabledStatus: " + namesInMap);
            }
            Log.d(TAG, "setLikeButtonEnabledStatus: " + USER_NAME);
            viewHolder.likeImg.setEnabled(false);
        } else {
            viewHolder.likeImg.setEnabled(true);

            viewHolder.likeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: likeImg ran");
                    addLikeToDatabase(new HashMap<String, Object>(), currentLocation, model);
                    viewHolder.likeImg.setEnabled(false);
                }
            });
        }
    }

    private void addLikeToDatabase(Map<String, Object> userLike, String currentLocation, PraiseModel model) {
        userLike.put(USER_NAME, String.valueOf(System.currentTimeMillis()));
        databaseReferenceFeed.child(Constants.FEED).child(currentLocation).child(model.getuId()).child(Constants.LIKES).updateChildren(userLike);
    }

    @NonNull
    private Map<String, String> createMapOfLikesBYUser(DataSnapshot dataSnapshot) {
        Log.d(TAG, "createMapOfLikesBYUser: ran");
        Map<String, String> UsersWhoLiked = new HashMap<>();
        for (DataSnapshot likeFromDB : dataSnapshot.getChildren()) {
            UsersWhoLiked.put(likeFromDB.getKey(), likeFromDB.getValue().toString());
        }
        return UsersWhoLiked;
    }

    public void switchContent(Fragment fragment) {


        if (mainFeedContext.getActivity() instanceof MainScreenActivity) {
            FragmentNavigater homeActivity = (FragmentNavigater) this.mainFeedContext.getActivity();
            homeActivity.SwitchFragment(fragment);
        }

    }
}
