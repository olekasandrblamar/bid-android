package com.bid.app.constants;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Global {

    public static boolean isHealthCheckUp = false;
    public static boolean isCheckUpNeeded = false;

    public static String getRestaurantImage(final String photoReference) {
        return "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photoReference + "&sensor=false&maxheight=250&maxwidth=600&key=AIzaSyBgdv6LKF4eOl0Ba1KHugHnkTYf0rn2sDs";
    }
    public static void showComingUpDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Coming up soon!")
                .setMessage("This feature will coming up soon!")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();

    }
}
