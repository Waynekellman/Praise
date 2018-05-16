package com.nyc.praise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private Button login;
    private EditText userName;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, MODE_PRIVATE);
        Log.d(TAG, "onCreate: ran");
        userName = findViewById(R.id.user_name);
        // set the username for the app.
        if (!sharedPreferences.getString(Constants.LOGIN_USERNAME, "").trim().isEmpty()){
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
                if (checkUserName()) {
                    setUsernameInSharedPrefs();
                    Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "onClick: ran");
                    break;
                }
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
