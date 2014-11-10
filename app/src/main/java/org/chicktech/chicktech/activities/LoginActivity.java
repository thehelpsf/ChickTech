package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.application.CTApplication;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.CTRestClient;
import org.chicktech.chicktech.utils.LoginPhoneNumberManager;
import org.chicktech.chicktech.utils.LoginUtils;

public class LoginActivity extends Activity {
    private String mPhoneNumber;
    LinearLayout llPhoneContainer;
    LoginPhoneNumberManager phoneNumberManager;
    Button btnSignOn;
    ProgressBar pb;
    RelativeLayout rlMain;
    TextView tvLearnMore;
    String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        eventID = i.getStringExtra("id");

        setContentView(R.layout.activity_login);
        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        llPhoneContainer = (LinearLayout)findViewById(R.id.llPhoneContainer);
        phoneNumberManager = new LoginPhoneNumberManager(this, llPhoneContainer, LoginPhoneNumberManager.FORMAT_US);
        btnSignOn = (Button) findViewById(R.id.btnSignOn);
        tvLearnMore = (TextView)findViewById(R.id.tvLearnMore);
        pb = (ProgressBar) findViewById(R.id.pbWorking);

        tvLearnMore.setText(Html.fromHtml("Learn how to participate at <a href=\"http://chicktech.org\">chicktech.org</a>"));
        tvLearnMore.setMovementMethod(LinkMovementMethod.getInstance());

        mPhoneNumber = LoginUtils.getSavedPhoneNumber(this);
        if (mPhoneNumber != null) {
            // Hide the number since we're just gonna log them in anyways
            llPhoneContainer.setVisibility(View.INVISIBLE);
            login(mPhoneNumber);
        } else {
            rlMain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        phoneNumberManager.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        if (!phoneNumberManager.validate()) {
            Toast.makeText(this, "Please enter full phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // get phone number from EditText and put into SharedPrefs
        final String phoneNumber = phoneNumberManager.getPhoneNumber();
        LoginUtils.setSharedPrefs(this, phoneNumber);

        showProgress();

        // see if user exists on Parse yet.
        CTRestClient.getPersonByPhoneNumber(phoneNumber, new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                hideProgress(false);
                if (user == null) {
                    signUp(phoneNumber);
                } else {
                    login(phoneNumber);
                }
            }
        });

    }

    private void subscribeToBroadcastChannel(ParseUser user) {
        final Person person = (Person) user;
        final String role;
        if (person != null) {
            role = person.getRoleString();
            ParsePush.subscribeInBackground(role, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
//                    Log.d("debug", "successfully subscribed to the student channel.");
//                        Toast.makeText(LoginActivity.this, "successfully subscribed to the " + role + " channel.", Toast.LENGTH_SHORT).show();
                    } else {
//                    Log.d("debug", "successfully subscribed to the student channel.");
                        Toast.makeText(LoginActivity.this, "did not subscribe to " + role + " channel", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void login (String phoneNumber) {
        showProgress();
        LoginUtils.login(phoneNumber, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    hideProgress(false);
                    CTApplication.parseUserSetup();
                    subscribeToBroadcastChannel(user);
                    moveOnToApp();
                } else {
                    hideProgress(true);
                    // Show the phone numbers b/c we might have hid them when auto-logging in
                    llPhoneContainer.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signUp (String phoneNumber) {

        showProgress();

        LoginUtils.signUp(phoneNumber, new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    hideProgress(false);
                    Toast.makeText(LoginActivity.this, "Welcome to ChickTech!", Toast.LENGTH_LONG).show();
                    CTApplication.parseUserSetup();
                    moveOnToApp();
                } else {
                    hideProgress(true);
                    Toast.makeText(LoginActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void moveOnToApp() {
        Intent i;
        i = new Intent(this, MainActivity.class);
        if (eventID != null) {
            i.putExtra("id", eventID);
        }
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void showProgress() {
        btnSignOn.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
    }

    private void hideProgress(boolean showLoginButton) {
        if (showLoginButton) {
            btnSignOn.setVisibility(View.VISIBLE);
        }
        pb.setVisibility(View.INVISIBLE);
    }
}
