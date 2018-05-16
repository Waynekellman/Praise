package com.nyc.praise;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class WritePraiseFragment extends Fragment {

    EditText writePraise;
    Button sendPraise;
    String currentLocation;
    PraiseModel model;

    public WritePraiseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_praise, container, false);
        writePraise = view.findViewById(R.id.write_praise);
        sendPraise = view.findViewById(R.id.send_praise);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        boolean isPraise = bundle.getBoolean("NewPraise");
        if (isPraise) {
            currentLocation = bundle.getString(Constants.LOCATION, "");
            sendPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkUserName()) {
                        if (isLocationFound()) {
                            DatabaseReference updatePost = FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FEED)
                                    .child(currentLocation);
                            String key = updatePost.push().getKey();
                            PraiseModel model = createModel(key);
                            updatePost.child(key).setValue(model);

                        } else {
                            Toast.makeText(getActivity(), "location could not be found", Toast.LENGTH_LONG).show();
                        }
                        getActivity().getSupportFragmentManager().beginTransaction().remove(WritePraiseFragment.this).commit();

                    }
                }
            });
        } else {
            model = new Gson().fromJson(bundle.getString("jsonModel"), PraiseModel.class);
            sendPraise.setText("send reply");
            sendPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference commentsReference = FirebaseDatabase.getInstance().getReference()
                            .child(Constants.FEED)
                            .child(model.getLocation())
                            .child(model.getuId())
                            .child(Constants.COMMENTS);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                    String userName = sharedPreferences.getString(Constants.LOGIN_USERNAME, "");
                    if (!userName.isEmpty()) {
                        CommentModel commentModel = new CommentModel();
                        commentModel.setName(userName);
                        commentModel.setComment(writePraise.getText().toString());
                        commentsReference.push().setValue(commentModel);
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().remove(WritePraiseFragment.this).commit();

                }
            });
        }


    }

    private boolean isLocationFound() {
        return !currentLocation.equals("Not Found");
    }

    private boolean checkUserName() {
        return !writePraise.getText().toString().isEmpty();
    }

    @NonNull
    private PraiseModel createModel(String key) {
        PraiseModel model = new PraiseModel();
        model.setMessage(writePraise.getText().toString());
        model.setDate(System.currentTimeMillis());
        model.setLocation(currentLocation);
        model.setuId(key);
        model.setComments(null);
        model.setLikes(null);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY,Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(Constants.LOGIN_USERNAME, "");
        if (!userName.isEmpty()) model.setUserName(userName);
        return model;
    }
}
