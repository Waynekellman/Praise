package com.nyc.praise;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding2.view.RxView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFeed extends Fragment {

    private static final String TAG = "MainFeed";


    private PraiseFireBaseRecyclerAdapter praiseFireBaseRecyclerAdapter;
    private DatabaseReference databaseReferenceFeed;
    String currentLocation;
    private View view;

    private TextView currentLocationTextView;
    private ImageView sendPraise;
    private String USER_NAME;

    // Declare the RecyclerView and the LinearLayoutManager first
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private MainFeedPresenter presenter;
    private int position = 0;
    boolean wentThroughOrientationChange = false;
    String ORIENTATION_STATE = "orientation state";


    public MainFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null){
            wentThroughOrientationChange = savedInstanceState.getBoolean(ORIENTATION_STATE, false);
        }
        this.view = view;
        currentLocationTextView = view.findViewById(R.id.current_location);
        sendPraise = view.findViewById(R.id.write_praise);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        USER_NAME = sharedPreferences.getString(Constants.LOGIN_USERNAME, "No User");

    }

    @Override
    public void onResume() {
        super.onResume();

        PraiseAppComponent component =((PraiseApp) getActivity().getApplication()).component();
        presenter = new MainFeedPresenter(component.provideGps(), this::connectMainFeed);
    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.disconnect();
    }

    private void initWritePraiseFrag() {
        WritePraiseFragment fragment = new WritePraiseFragment();
        Bundle newPraiseBundle = new Bundle();
        newPraiseBundle.putBoolean("NewPraise", true);
        newPraiseBundle.putString(Constants.LOCATION, currentLocation);
        fragment.setArguments(newPraiseBundle);
        switchContent(fragment);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ORIENTATION_STATE, true);
    }

    public void connectMainFeed(final String currentLocation) {
        this.currentLocation = currentLocation;

        RxView.clicks(sendPraise).subscribe(x -> initWritePraiseFrag());

        currentLocationTextView.setText(currentLocation.replace(" County", ""));
        databaseReferenceFeed = FirebaseDatabase.getInstance().getReference();

        praiseFireBaseRecyclerAdapter = getPraiseFireBaseRecyclerAdapter(currentLocation);
        praiseFireBaseRecyclerAdapter.setUSER_NAME(USER_NAME);
        praiseFireBaseRecyclerAdapter.setMainFeedContext(this);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.main_feed_recycler);
        recyclerView.setAdapter(praiseFireBaseRecyclerAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        if (!wentThroughOrientationChange) {
            recyclerView.scrollToPosition(position);
        }
    }

    @NonNull
    private PraiseFireBaseRecyclerAdapter getPraiseFireBaseRecyclerAdapter(String currentLocation) {
        return new PraiseFireBaseRecyclerAdapter(
                PraiseModel.class,
                R.layout.main_feed_itemview, FireBaseViewHolder.class,
                databaseReferenceFeed.child(Constants.FEED).child(currentLocation));
    }


    public void switchContent(Fragment fragment) {

        if (this.getActivity() instanceof MainScreenActivity) {
            FragmentNavigator homeActivity = (FragmentNavigator) this.getActivity();
            homeActivity.SwitchFragment(fragment);
        }

    }

}
