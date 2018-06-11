package com.nyc.praise;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wayne Kellman on 6/3/18.
 */

public class PraiseFireBaseRecyclerAdapter extends FirebaseRecyclerAdapter<PraiseModel, FireBaseViewHolder> {


    private static final String TAG = "PraiseFireBaseAdapter";
    private String USER_NAME;

    private MainFeed mainFeedContext;

    private DatabaseReference databaseReferenceFeed;

    public PraiseFireBaseRecyclerAdapter(Class<PraiseModel> modelClass, int modelLayout, Class<FireBaseViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.databaseReferenceFeed = ref;
    }

    @Override
    protected void populateViewHolder(FireBaseViewHolder viewHolder, final PraiseModel model, int position) {

        viewHolder.OnBind(model);
        ValueEventListener listenerForLikes = getLikeValueEventListener(model, viewHolder);
        if (model.getuId() != null) {
            databaseReferenceFeed.child(model.getuId()).child(Constants.LIKES).addValueEventListener(listenerForLikes);
        }
        else {
            Toast.makeText(mainFeedContext.getActivity(),"modelID is null", Toast.LENGTH_LONG).show();
        }
        viewHolder.commentImg.setOnClickListener(v -> initCommentsFrag(model));
        viewHolder.likeImg.setOnClickListener(v -> userClickedLikeEvent(viewHolder, model));
    }

    private void userClickedLikeEvent(FireBaseViewHolder viewHolder, PraiseModel model) {
        if( viewHolder.likeImg.isEnabled()) {
            addLikeToDatabase(new HashMap<>(),model);
            viewHolder.likeImg.setEnabled(false);
        }
    }

    private void initCommentsFrag(PraiseModel model) {
        CommentsFragment fragment = new CommentsFragment();
        String modelJsonString = new Gson().toJson(model);
        Bundle bundle = new Bundle();
        bundle.putString("jsonModel", modelJsonString);
        fragment.setArguments(bundle);
        switchContent(fragment);
    }

    public void setUSER_NAME(String user_name) {
        this.USER_NAME = user_name;
    }

    public void setMainFeedContext(MainFeed mainFeedContext) {
        this.mainFeedContext = mainFeedContext;
    }

    @NonNull
    private ValueEventListener getLikeValueEventListener(final PraiseModel model, final FireBaseViewHolder viewHolder) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String likeCountSnapshot = dataSnapshot.getChildrenCount() + "";
                Map<String, String> UsersWhoLiked = createMapOfLikesBYUser(dataSnapshot);

                setLikeButtonEnabledStatus(UsersWhoLiked, model, viewHolder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void setLikeButtonEnabledStatus(Map<String, String> usersWhoLiked, final PraiseModel model, final FireBaseViewHolder viewHolder) {
        if (usersWhoLiked.containsKey(USER_NAME)) {
            viewHolder.likeImg.setEnabled(false);
        } else {
            viewHolder.likeImg.setEnabled(true);

        }
    }

    private void addLikeToDatabase(Map<String, Object> userLike, PraiseModel model) {
        userLike.put(USER_NAME, String.valueOf(System.currentTimeMillis()));
        databaseReferenceFeed.child(model.getuId()).child(Constants.LIKES).updateChildren(userLike);
    }

    @NonNull
    private Map<String, String> createMapOfLikesBYUser(DataSnapshot dataSnapshot) {
        Map<String, String> UsersWhoLiked = new HashMap<>();
        for (DataSnapshot likeFromDB : dataSnapshot.getChildren()) {
            UsersWhoLiked.put(likeFromDB.getKey(), likeFromDB.getValue().toString());
        }
        return UsersWhoLiked;
    }

    public void switchContent(Fragment fragment) {


        if (mainFeedContext.getActivity() instanceof MainScreenActivity) {
            FragmentNavigator homeActivity = (FragmentNavigator) this.mainFeedContext.getActivity();
            homeActivity.SwitchFragment(fragment);
        }

    }

}
