package org.chicktech.chicktech.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.chicktech.chicktech.application.CTApplication;

import java.io.File;

/**
 * Created by Jing Jin on 10/17/14.
 */
public class FileSystemUtils {
    // Returns the Uri for a photo stored on disk given the fileName
    public static Uri getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), CTApplication.APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(CTApplication.APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
    }
}
