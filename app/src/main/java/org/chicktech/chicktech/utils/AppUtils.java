package org.chicktech.chicktech.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by paul on 10/26/14.
 */
public class AppUtils {

    public static boolean isThisAppInForeground(Context context) {
        boolean isUs = false;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // The first in the list of RunningTasks is always the foreground task.
        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);

        String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
        isUs = (foregroundTaskPackageName.toLowerCase().contains("org.chicktech.chicktech"));

//        Toast.makeText(context, foregroundTaskPackageName, Toast.LENGTH_LONG).show();
//
//        PackageManager pm = context.getPackageManager();
//        PackageInfo foregroundAppPackageInfo = null;
//        try {
//            foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();

        return isUs;
    }
}
