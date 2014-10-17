package org.chicktech.chicktech.utils;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

/**
 * Created by Jing Jin on 10/17/14.
 */
public interface CameraLaunchingListener {
    public void onCameraSuccess(Bitmap image);
    public void onCameraFailure(int resultCode);
}
