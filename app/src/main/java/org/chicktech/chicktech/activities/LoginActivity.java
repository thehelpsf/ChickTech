package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.CTRestClient;

public class LoginActivity extends Activity {
    private String mPhoneNumber;
    private SharedPreferences mSettings;
    EditText etPhoneNumber;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        mSettings = getSharedPreferences("Settings", 0);

        // See if phoneNumber is in Shared Prefs
        mPhoneNumber = mSettings.getString("phoneNumber", "missing");
        if (!mPhoneNumber.equals("missing")) {
            // Right now just fill in EditText and let user poke Get Started button
            etPhoneNumber.setText(mPhoneNumber);
        }

        pb = (ProgressBar) findViewById(R.id.pbWorking);

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

        pb.setVisibility(ProgressBar.VISIBLE);

        // see if user exists on Parse yet.
        CTRestClient.getPersonByPhoneNumber(phoneNumber, new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                pb.setVisibility(ProgressBar.INVISIBLE);
                if (user == null) {
                    signUp(phoneNumber);
                } else {
                    login(phoneNumber);
                }
            }
        });

    }

    private void login (String phoneNumber) {
        pb.setVisibility(ProgressBar.VISIBLE);
        ParseUser.logInInBackground(phoneNumber, phoneNumber, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                pb.setVisibility(ProgressBar.INVISIBLE);
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

        pb.setVisibility(ProgressBar.VISIBLE);

        Person user = new Person();
        user.setUsername(phoneNumber);
        user.setPassword(phoneNumber);
        user.setRole(Person.Role.STUDENT);
        //user.setEmail(phoneNumber + "@example.com");
        // other fields can be set just like with ParseObject
        user.setPhoneNumber(phoneNumber);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                pb.setVisibility(ProgressBar.INVISIBLE);
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                    moveOnToApp();
                } else {
                    Toast.makeText(LoginActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void moveOnToApp() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
