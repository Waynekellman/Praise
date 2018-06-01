package com.nyc.praise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private Button login;
    private EditText userName;
    SharedPreferences sharedPreferences;
    private RadioGroup iconGroup, colorGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        login = findViewById(R.id.login);
        iconGroup = findViewById(R.id.icon_group);
        colorGroup = findViewById(R.id.color_group);
        login.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, MODE_PRIVATE);
        Log.d(TAG, "onCreate: ran");
        userName = findViewById(R.id.user_name);
        // set the username for the app.
        if (!sharedPreferences.getString(Constants.LOGIN_USERNAME, "").trim().isEmpty() && sharedPreferences.getInt(Constants.ICON, -1) != -1 && sharedPreferences.getInt(Constants.COLOR, -1) != -1){
            Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
            startActivity(intent);
            Log.d(TAG, "onClick: ran");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login:
                if (checkUserName() && checkIcon() && checkColor()) {
                    saveIconAndColor();
                    setUsernameInSharedPrefs();
                    Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "onClick: ran");
                    break;
                } else {
                    if (checkUserName()) {
                        Toast.makeText(LoginActivity.this, "User name must be 6 characters or more", Toast.LENGTH_SHORT).show();
                    }
                    if (checkIcon()) {
                        Toast.makeText(LoginActivity.this, "Please Select an Icon", Toast.LENGTH_SHORT).show();
                    }
                    if (checkColor()) {
                        Toast.makeText(LoginActivity.this, "Please Select a color", Toast.LENGTH_SHORT).show();

                    }
                }
        }
    }

    private boolean checkColor() {

        return colorGroup.getCheckedRadioButtonId() != -1;
    }

    private boolean checkIcon() {
        return iconGroup.getCheckedRadioButtonId() != -1;
    }

    private void saveIconAndColor() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LOGIN_USERNAME, userName.getText().toString());
        if (iconGroup.getCheckedRadioButtonId() != -1 && colorGroup.getCheckedRadioButtonId() != -1) {
             switch (iconGroup.getCheckedRadioButtonId()) {
                 case R.id.fire_icon:
                     editor.putInt(Constants.ICON, R.drawable.fire_icon);
                     break;
                 case R.id.flower_icon:
                     editor.putInt(Constants.ICON, R.drawable.flower_icon);
                     break;
                 case R.id.heart_icon:
                     editor.putInt(Constants.ICON, R.drawable.heart_icon);
                     break;
                 case R.id.money_icon:
                     editor.putInt(Constants.ICON, R.drawable.money_icon);
                     break;
                 case R.id.music_note_icon:
                     editor.putInt(Constants.ICON, R.drawable.music_note_icon);
                     break;
             }
             switch (colorGroup.getCheckedRadioButtonId()) {
                 case R.id.red:
                     editor.putInt(Constants.COLOR, R.color.red);
                     break;
                 case R.id.white:
                     editor.putInt(Constants.COLOR, R.color.white);
                     break;
                 case R.id.black:
                     editor.putInt(Constants.COLOR, R.color.black);
                     break;
                 case R.id.blue:
                     editor.putInt(Constants.COLOR, R.color.black);
                     break;
                 case R.id.purple:
                     editor.putInt(Constants.COLOR, R.color.purple);
                     break;
             }
             editor.commit();
        }

    }

    private boolean checkUserName() {
        return userName.getText().toString().length() > 5;
    }

    private void setUsernameInSharedPrefs() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LOGIN_USERNAME, userName.getText().toString());
        editor.commit();
    }
}
