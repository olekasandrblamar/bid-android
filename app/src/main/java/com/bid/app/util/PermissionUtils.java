package com.bid.app.util;

import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    public static boolean checkPermission(final AppCompatActivity appCompatActivity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(appCompatActivity, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(appCompatActivity, new String[]{permission}, requestCode);
            return false;
        } else {
            AlertUtils.showAlerterWarning(appCompatActivity, "Permission already granted");
            return true;
        }
    }
}
