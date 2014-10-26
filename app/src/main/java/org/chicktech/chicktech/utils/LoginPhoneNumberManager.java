package org.chicktech.chicktech.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.views.SinglePhoneNumberField;

import java.util.ArrayList;

/**
 * Created by Jing Jin on 10/25/14.
 */
public class LoginPhoneNumberManager implements SinglePhoneNumberField.KeyboardListener {
    public static final char DIGIT_FORMAT_SYMBOL = 'd';
    public static final char SPACE_FORMAT_SYMBOL = 's';
    public static final String FORMAT_US = "dddsdddsdddd";

    private ArrayList<SinglePhoneNumberField> etPhoneFields;
    private View.OnTouchListener fieldTouchListener;

    public LoginPhoneNumberManager(final Activity activity, ViewGroup parent, String format) {
        etPhoneFields = new ArrayList<SinglePhoneNumberField>();

        fieldTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.requestFocus();
                ((SinglePhoneNumberField) view).selectAll();
                // Need to manually pull up keyboard (if it's not visible) since we're not letting the system handle this event.
                ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput
                        (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                return true;
            }
        };

        LayoutInflater inflater = LayoutInflater.from(activity);
        for (int i = 0; i < format.length(); i++) {
            switch(format.charAt(i)) {
                case DIGIT_FORMAT_SYMBOL:
                    SinglePhoneNumberField field = (SinglePhoneNumberField)inflater.inflate(R.layout.item_phone_digit, parent, false);
                    field.setListener(this);
                    field.setOnTouchListener(fieldTouchListener);
                    etPhoneFields.add(field);
                    parent.addView(field);
                    break;
                case SPACE_FORMAT_SYMBOL:
                    inflater.inflate(R.layout.item_phone_space, parent);
                    break;
            }
        }
    }

    public void requestFocus() {
        etPhoneFields.get(0).requestFocus();
    }

    public boolean validate() {
        for (int i = 0; i < etPhoneFields.size(); i++) {
            String s = etPhoneFields.get(i).getText().toString();
            if (s.length() != 1 ) {
                return false;
            }
            char c = s.charAt(0);
            if (c < '0' || c > '9') { // Must be a digit between 0 and 9
                return false;
            }
        }

        return true;
    }

    public String getPhoneNumber() {
        String phone = "";
        for (int i = 0; i < etPhoneFields.size(); i++) {
            phone += etPhoneFields.get(i).getText().toString();
        }
        return phone;
    }

    @Override
    public void onTextChanged(SinglePhoneNumberField view, CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (lengthAfter == 0) {
            return;
        }

        int index = etPhoneFields.indexOf(view);
        if (index == -1) {
            Log.d("Login", "Error: Couldn't find index of view that fired onTextChanged event");
        }else if (index != etPhoneFields.size() - 1) { // Proceed to the next field
            SinglePhoneNumberField field = etPhoneFields.get(index + 1);
            field.requestFocus();
            field.selectAll();
        } else { // do nothing, maybe auto-login later?

        }
    }

    @Override
    public void onDelete(SinglePhoneNumberField view) {
        int index = etPhoneFields.indexOf(view);
        if (index == -1) {
            Log.d("Login", "Error: Couldn't find index of view that fired onDelete event");
        } else if (index > 0) {
            SinglePhoneNumberField field = etPhoneFields.get(index - 1);
            field.requestFocus();
            field.selectAll();
        }
    }
}
