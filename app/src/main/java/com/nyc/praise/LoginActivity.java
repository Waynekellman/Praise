package com.nyc.praise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginView{

    private static final String TAG = "LoginActivity";
    private Button login;
    private EditText userName;
    SharedPreferences sharedPreferences;
    private RadioGroup iconGroup, colorGroup;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        login = findViewById(R.id.login);
        iconGroup = findViewById(R.id.icon_group);
        colorGroup = findViewById(R.id.color_group);
        login.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(Constants.LOGIN_SHARED_PREFS_KEY, MODE_PRIVATE);
        userName = findViewById(R.id.user_name);
        // set the username for the app.
        if (!sharedPreferences.getString(Constants.LOGIN_USERNAME, "").trim().isEmpty() && sharedPreferences.getInt(Constants.ICON, -1) != -1 && sharedPreferences.getInt(Constants.COLOR, -1) != -1){
            Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
            startActivity(intent);
        }
        presenter = new LoginPresenter(this, new LoginValidator());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login:
                int colorButtonID = colorGroup.getCheckedRadioButtonId();
                int iconButtonID = iconGroup.getCheckedRadioButtonId();
                String userNameString = userName.getText().toString();
                presenter.checkLogin(colorButtonID, iconButtonID, userNameString);
        }
    }

    private void clearLogin() {
        colorGroup.clearCheck();
        iconGroup.clearCheck();
        userName.setText("");
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

    private void setUsernameInSharedPrefs() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LOGIN_USERNAME, userName.getText().toString());
        editor.commit();
    }

    @Override
    public void navigateToMainScreen() {

        saveIconAndColor();
        setUsernameInSharedPrefs();
        clearLogin();
        Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
        startActivity(intent);
    }

    @Override
    public void failName() {
        Toast.makeText(LoginActivity.this, "User name must be 6 characters or more", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void failColor() {
        Toast.makeText(LoginActivity.this, "Please Select a color", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void failIcon() {
        Toast.makeText(LoginActivity.this, "Please Select an Icon", Toast.LENGTH_SHORT).show();

    }
}
