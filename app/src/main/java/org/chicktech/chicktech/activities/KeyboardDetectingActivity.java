package org.chicktech.chicktech.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

/**
 * Created by Jing Jin on 11/7/14.
 */
public abstract class KeyboardDetectingActivity extends ActionBarActivity {
    private static final int HEIGHT_TO_CHECK_DP = 100;
    private boolean isKeyboardVisible;

    protected abstract View getRootView();

    protected void startKeyboardDetection() {
        final View rootView = getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                final float scale = getResources().getDisplayMetrics().density;
                if (heightDiff > HEIGHT_TO_CHECK_DP * scale) { // if more than this, its probably a keyboard...
                    isKeyboardVisible = true;
                } else {
                    isKeyboardVisible = false;
                }
            }
        });
    }

    public boolean isKeyboardVisible() {
        return isKeyboardVisible;
    }

}
