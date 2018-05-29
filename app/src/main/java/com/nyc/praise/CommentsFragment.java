package com.nyc.praise;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment {


    private static final String TAG = "CommentsFragment";
    FirebaseListAdapter<CommentModel> commentListAdapter;
    PraiseModel model;
    TextView praise;
    ListView comments;
    DatabaseReference commentsReference;
    TextView reply;

    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        Bundle bundle = getArguments();
        model = new Gson().fromJson(bundle.getString("jsonModel"), PraiseModel.class);
        praise = view.findViewById(R.id.praise_message_comment);
        comments = view.findViewById(R.id.replies_to_comment);
        reply = view.findViewById(R.id.reply_text);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        praise.setText(model.getMessage());
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WritePraiseFragment newComment = new WritePraiseFragment();
                String modelJsonString = new Gson().toJson(model);
                Bundle bundle = new Bundle();
                bundle.putString("jsonModel", modelJsonString);
                newComment.setArguments(bundle);
                switchContent(newComment);
            }
        });
        Log.d(TAG, "onViewCreated: ran");
        Log.d(TAG, "onViewCreated: location " + model.getLocation());

        commentsReference = FirebaseDatabase.getInstance().getReference();
        commentListAdapter = new FirebaseListAdapter<CommentModel>(
                this.getActivity(),
                CommentModel.class,
                R.layout.comments_itemview,
                commentsReference.child(Constants.FEED).child(model.getLocation()).child(model.getuId()).child(Constants.COMMENTS)) {
            @Override
            protected void populateView(View v, CommentModel model, int position) {
                TextView commentLeft = v.findViewById(R.id.comment);
                TextView commentNameLeft = v.findViewById(R.id.comment_name);
                String name = model.getName();
                String comment = model.getComment();

                Log.d(TAG, "populateView: " + position);
                commentLeft.setText(comment);
                commentNameLeft.setText(name);

            }
        };

        comments.setAdapter(commentListAdapter);


    }

    public void switchContent(Fragment fragment) {

        if (this.getActivity() instanceof MainScreenActivity) {
            FragmentNavigater homeActivity = (FragmentNavigater) this.getActivity();
            homeActivity.SwitchFragment(fragment);
        }

    }
}