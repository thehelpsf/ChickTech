package org.chicktech.chicktech.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import org.chicktech.chicktech.R;

/**
 * Created by Jing Jin on 10/25/14.
 */
public class SinglePhoneNumberField extends EditText {

    public interface KeyboardListener {
        void onTextChanged(SinglePhoneNumberField view, CharSequence text, int start, int lengthBefore, int lengthAfter);
        void onDelete(SinglePhoneNumberField view);
    }

    private KeyboardListener listener;

    public SinglePhoneNumberField(Context context) {
        super(context);
    }

    public SinglePhoneNumberField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SinglePhoneNumberField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (lengthAfter == 0) {
            setBackgroundResource(R.drawable.login_textfield_selector);
        } else {
            setBackgroundResource(R.drawable.login_textfield_filled_selector);
        }

        if (listener != null) {
            listener.onTextChanged(this, text, start, lengthBefore, lengthAfter);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean v = super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (listener != null) {
                listener.onDelete(this);
                return true;
            }
        }
        return v;
    }

    public void setListener(KeyboardListener listener) {
        this.listener = listener;
    }

    public KeyboardListener getListener() {
        return listener;
    }
}
