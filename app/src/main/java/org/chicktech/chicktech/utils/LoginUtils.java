package org.chicktech.chicktech.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.chicktech.chicktech.activities.LoginActivity;
import org.chicktech.chicktech.models.Person;

/**
 * Created by paul on 10/21/14.
 */
public class LoginUtils {

    final private static String MISSING = "missing";
    final private static String KEY_PHONE_NUMBER = "phoneNumber";
    final private static String PREFS_SETTINGS = "Settings";


    public static void signUp (String phoneNumber, SignUpCallback callback) {
        Person user = new Person();
        user.setUsername(phoneNumber);
        user.setPassword(phoneNumber);
        user.setRole(Person.Role.STUDENT);
        user.setPhoneNumber(phoneNumber);

        user.signUpInBackground(callback);
    }


    public static void login (String phoneNumber, LogInCallback callback) {
        ParseUser.logInInBackground(phoneNumber, phoneNumber, callback);
    }


    public static void setSharedPrefs(Context context, String phoneNumber) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }


    public static String getSavedPhoneNumber(Context context) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_SETTINGS, 0);
        String phoneNumber = settings.getString(KEY_PHONE_NUMBER, MISSING);
        if (!phoneNumber.equals(MISSING)) {
            return phoneNumber;
        }
        return null;
    }


    public static void logout (Context context) {
        ParseUser.logOut();
        clearSharedPrefs(context);
        returnToLoginActivity(context);
    }


    public static void clearSharedPrefs(Context context) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(KEY_PHONE_NUMBER);
        editor.commit();
    }


    private static void returnToLoginActivity(Context context) {
        // Go back to LoginActivity and clear stack.
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish(); // kill the current activity
    }

}
