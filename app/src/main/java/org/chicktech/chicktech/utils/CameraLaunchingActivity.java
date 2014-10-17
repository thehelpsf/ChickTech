package org.chicktech.chicktech.utils;

import android.support.v4.app.Fragment;

/**
 * Created by Jing Jin on 10/17/14.
 */
public interface CameraLaunchingActivity {
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public void launchCamera(CameraLaunchingListener listener);
}
