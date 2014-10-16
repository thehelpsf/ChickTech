package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.chicktech.chicktech.R;

public class LoginActivity extends Activity {
    private String mPhoneNumber;
    private SharedPreferences mSettings;
    EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        mPhoneNumber = getPhoneNumberFromSharedPrefs();
        if (!mPhoneNumber.equals("missing")) {
            etPhoneNumber.setText(mPhoneNumber);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickSignOn(View button) {

        // get phone number from EditText and put into SharedPrefs
        final String phoneNumber = etPhoneNumber.getText().toString();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.commit();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("phoneNumber", phoneNumber);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                if (user == null) {
                    signUp(phoneNumber);
                } else {
                    login(phoneNumber);
                }
            }
        });

    }

    private void login (String phoneNumber) {
        ParseUser.logInInBackground(phoneNumber, phoneNumber, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    moveOnToApp();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signUp (String phoneNumber) {
        ParseUser user = new ParseUser();
        user.setUsername(phoneNumber);
        user.setPassword(phoneNumber);
        //user.setEmail(phoneNumber + "@example.com");

        // other fields can be set just like with ParseObject
        user.put("phoneNumber", phoneNumber);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                    moveOnToApp();
                } else {
                    Toast.makeText(LoginActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getPhoneNumberFromSharedPrefs() {
        mSettings = getSharedPreferences("Settings", 0);
        return mSettings.getString("phoneNumber", "missing");
    }

    private void moveOnToApp() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
