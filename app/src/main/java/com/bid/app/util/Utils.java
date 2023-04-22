package com.bid.app.util;

import static com.bid.app.util.Constants.IMAGE_BIG_THUMB;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bid.app.R;
import com.bid.app.model.Attachment;
import com.bid.app.model.view.LatLngPosition;
import com.bid.app.model.view.RoutePath;
import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getDeviceOSVersion() {
        return Build.VERSION.BASE_OS;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=).{8,}$";
        Pattern pat = Pattern.compile(passwordRegex);
        if (password == null)
            return false;
        return pat.matcher(password).matches();
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static Date getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        return date;
    }

    public static String getDate(String input) {
        return !StringUtils.isEmpty(input) ? input.substring(0, 10) : "";
    }

    public static String getTime(String input) {
        return !StringUtils.isEmpty(input) ? input.substring(12, 19) : "";
    }

    public static void shareContent(AppCompatActivity appCompatActivity) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appCompatActivity.getResources().getString(R.string.app_name));
        sendIntent.setType("text/plain");
        appCompatActivity.startActivity(sendIntent);
    }

    public static final String getMD5Hash(final Attachment attachment) {
        Logger.e(TAG, attachment.getClassName());
        Logger.e(TAG, attachment.getId());
        final String fileName = attachment.getClassName() + attachment.getId() + "jpg" + IMAGE_BIG_THUMB;
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(fileName.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            Logger.e(TAG, hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static final String getMD5Hash(String str) {
        Logger.e(TAG, str);
        final String fileName = str + "jpg" + IMAGE_BIG_THUMB;
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(fileName.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            Logger.e(TAG, hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentDateTime() {
        final Date date = Calendar.getInstance().getTime();
        final SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        final String formattedDate = df.format(date);
        return formattedDate;
    }

    public static String convertTimeTo12HourFormat(String _24HourTime) {
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            return _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void dismissAllDialogs(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();

        if (fragments == null)
            return;

        for (Fragment fragment : fragments) {
            if (fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismissAllowingStateLoss();
            }

            FragmentManager childFragmentManager = fragment.getChildFragmentManager();
            if (childFragmentManager != null)
                dismissAllDialogs(childFragmentManager);
        }
    }

    // ------ Change Date Format
    public static String changeBEDateFormatToFE(String dateStringInyyyyMMdd) {
        String result = "";
        if (dateStringInyyyyMMdd == null || dateStringInyyyyMMdd.isEmpty()) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat formatterNew = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatterOld.parse(dateStringInyyyyMMdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }
    public static double getProjection(double x1, double y1, double x2, double y2, double x3, double y3)
    {
        double px=x2-x1;
        double py=y2-y1;
        double temp=(px*px)+(py*py);
        double u=((x3 - x1) * px + (y3 - y1) * py) / (temp);
        if(u>1){
            u=1;
        }
        else if(u<0){
            u=0;
        }
        return u;
    }
    public static double getProjection(LatLng pt1, LatLng pt2, LatLng pt0) {
        return getProjection(pt1.latitude, pt1.longitude, pt2.latitude, pt2.longitude, pt0.latitude, pt0.longitude);
    }
    public static double shortestDistance(double x1, double y1, double x2, double y2, double x3, double y3)
    {
        double px=x2-x1;
        double py=y2-y1;
        double temp=(px*px)+(py*py);
        double u=((x3 - x1) * px + (y3 - y1) * py) / (temp);
        if(u>1){
            u=1;
        }
        else if(u<0){
            u=0;
        }
        double x = x1 + u * px;
        double y = y1 + u * py;

        double dx = x - x3;
        double dy = y - y3;
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;
    }
    public static double shortestDistance(LatLng pt1, LatLng pt2, LatLng pt0) {
        return shortestDistance(pt1.latitude, pt1.longitude, pt2.latitude, pt2.longitude, pt0.latitude, pt0.longitude);
    }
    public static ProjectionPathInfo getProjectionPoint(ArrayList<LatLng> path_points, LatLng newLatLng) {
        int min_i = 0;
        double min_dist = shortestDistance(path_points.get(0), path_points.get(1), newLatLng);
        for(int i = 1; i < path_points.size() - 1; i++) {
            double dist = shortestDistance(path_points.get(i), path_points.get(i + 1), newLatLng);
            if(dist < min_dist) {
                min_i = i;
                min_dist = dist;
            }
        }
        return new ProjectionPathInfo(min_i, getProjection(path_points.get(min_i), path_points.get(min_i + 1), newLatLng));
    }
    public static LatLng getPosition(LatLng latLng1, LatLng latLng2, double delta) {
        return new LatLng(  latLng1.latitude +  delta * (latLng2.latitude - latLng1.latitude),
                        latLng1.longitude +  delta * (latLng2.longitude - latLng1.longitude));
    }

    public static double getDistance(LatLng latLng1, LatLng latLng2) {
        if(latLng1 == null || latLng2 == null) return 0;
        android.location.Location loc1 = new android.location.Location("");
        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);
        android.location.Location loc2 = new android.location.Location("");
        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);
        return loc1.distanceTo(loc2);
    }
    public static double getDistance(LatLngPosition latLngPosition1, LatLngPosition latLngPosition2) {
        android.location.Location loc1 = new android.location.Location("");
        loc1.setLatitude(new Double(latLngPosition1.getLat()));
        loc1.setLongitude(new Double(latLngPosition1.getLng()));
        android.location.Location loc2 = new android.location.Location("");
        loc2.setLatitude(new Double(latLngPosition2.getLat()));
        loc2.setLongitude(new Double(latLngPosition2.getLng()));
        return loc1.distanceTo(loc2);
    }
    public static double getDistance(ArrayList<LatLng> point_array, ProjectionPathInfo info1, ProjectionPathInfo info2) {
        double distance = getDistance(point_array.get(info1.getPathId()), point_array.get(info1.getPathId() + 1)) * (1 - info1.getDelta());
        distance += getDistance(point_array.get(info2.getPathId()), point_array.get(info2.getPathId() + 1)) *info2.getDelta();

        for(int i = info1.getPathId() + 1; i < info2.getPathId(); i++) {
            distance +=getDistance(point_array.get(i), point_array.get(i + 1));
        }
        return distance;
    }

    public static double getDistance(RoutePath routePath) {
        ArrayList<LatLng> latLngs = getLatLngArray(routePath);
        double distance = 0;
        for(int i = 0; i < latLngs.size() - 1; i++) {
            distance += getDistance(latLngs.get(i), latLngs.get(i + 1));
        }
        return distance;
    }
    public static ArrayList<LatLng> getLatLngArray(RoutePath routePath) {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        for(String pathStr:routePath.getPath()){
            latLngs.addAll(decodePoly(pathStr));
        }
        return latLngs;
    }
    public static LatLng LatLngPosition2LatLng(LatLngPosition latLngPosition) {
        return new LatLng(new Double(latLngPosition.getLat()), new Double(latLngPosition.getLng()));
    }
    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static  String Timedistance(String strTime1) {
        String res ="0 min left";
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        String strTime2;
        Date date = new Date();
        strTime2 = formatter.format(date);

        try {
            Date date1 = formatter.parse(strTime1);
            Date date2 = formatter.parse(strTime2);
            long diff = date2.getTime() - date1.getTime();
            if(diff > 0) {
                res = new Long(diff / (1000 * 60)).toString() + " min left";
            }
            else{
                res = "0 min left";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}

