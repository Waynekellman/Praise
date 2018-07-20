package com.nyc.praise;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class WritePraiseFragment extends Fragment implements ToneValidator {

    EditText writePraise;
    Button sendPraise;
    String currentLocation;
    PraiseModel model;
    private static final String TAG = "WritePraiseFragment";
    boolean isPraise;
    private String SAVED_DRAFT;
    private WritePraisePresenter presentner;

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
        if (savedInstanceState != null) {
            writePraise.setText(savedInstanceState.getString(SAVED_DRAFT, ""));
        }

        isPraise = bundle.getBoolean("NewPraise");
        presentner = new WritePraisePresenter(this);

        if (isPraise) {
            currentLocation = bundle.getString(Constants.LOCATION, "");
            sendPraise.setOnClickListener(view1 -> {
                sendPraise.setEnabled(false);
                presentner.analyzePraiseTone(writePraise.getText().toString(), currentLocation);
            });
        } else {
            model = new Gson().fromJson(bundle.getString("jsonModel"), PraiseModel.class);
            sendPraise.setText(R.string.reply);
            sendPraise.setOnClickListener(view12 -> {
                sendPraise.setEnabled(false);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                String userName = sharedPreferences.getString(Constants.LOGIN_USERNAME, "");
                presentner.analyzeCommentTone(model,writePraise.getText().toString(), userName);
            });
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!writePraise.getText().toString().isEmpty()) {
            SAVED_DRAFT = "SavedDraft";
            outState.putString(SAVED_DRAFT, writePraise.getText().toString());
        }
    }

    @Override
    public void locationFailed() {

        getActivity().runOnUiThread(() ->
                Toast.makeText(getActivity(), "No Location", Toast.LENGTH_LONG).show());
    }

    @Override
    public void textIsEmpty() {

        getActivity().runOnUiThread(() ->
                Toast.makeText(getActivity(), "Write something nice!", Toast.LENGTH_LONG).show());
    }

    @Override
    public void toneValid() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(WritePraiseFragment.this)
                .commit();

    }

    @Override
    public void toneInvalid() {
        getActivity().runOnUiThread(() ->
                Toast.makeText(getActivity(), "Try to make to comment nicer", Toast.LENGTH_LONG).show());
    }

    @Override
    public PraiseModel getPraiseModel(String key) {

        PraiseModel model = new PraiseModel();
        model.setMessage(writePraise.getText().toString());
        model.setDate(SystemClock.elapsedRealtime());
        model.setLocation(currentLocation);
        model.setuId(key);
        model.setComments(null);
        model.setLikes(null);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(Constants.LOGIN_USERNAME, "");
        int iconRec = sharedPreferences.getInt(Constants.ICON, -1);
        int colorRec = sharedPreferences.getInt(Constants.COLOR, -1);
        if (!userName.isEmpty()) model.setUserName(userName);
        if (iconRec != -1) model.setIconResource(iconRec);
        if (colorRec != -1) model.setColorResource(colorRec);
        return model;
    }
}
