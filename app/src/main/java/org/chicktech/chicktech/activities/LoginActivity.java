package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.application.CTApplication;
import org.chicktech.chicktech.utils.CTRestClient;
import org.chicktech.chicktech.utils.LoginUtils;

public class LoginActivity extends Activity {
    private String mPhoneNumber;
    EditText etPhoneNumber;
    Button btnSignOn;
    ProgressBar pb;
    RelativeLayout rlMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        btnSignOn = (Button) findViewById(R.id.btnSignOn);

        pb = (ProgressBar) findViewById(R.id.pbWorking);

        mPhoneNumber = LoginUtils.getSavedPhoneNumber(this);
        if (mPhoneNumber != null) {
            etPhoneNumber.setText(mPhoneNumber);
            login(mPhoneNumber);
        } else {
            rlMain.setVisibility(View.VISIBLE);
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
        LoginUtils.setSharedPrefs(this, phoneNumber);

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
        LoginUtils.login(phoneNumber, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                pb.setVisibility(ProgressBar.INVISIBLE);
                if (user != null) {
                    CTApplication.parseUserSetup();
                    moveOnToApp();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signUp (String phoneNumber) {

        pb.setVisibility(ProgressBar.VISIBLE);

        LoginUtils.signUp(phoneNumber, new SignUpCallback() {
            public void done(ParseException e) {
                pb.setVisibility(ProgressBar.INVISIBLE);
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "Welcome to ChickTech!", Toast.LENGTH_LONG).show();
                    CTApplication.parseUserSetup();
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
