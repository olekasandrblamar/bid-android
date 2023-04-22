package com.bid.app.util;

import org.apache.commons.lang3.StringUtils;

public class Logger {

    public static void e(String TAG, String message) {
        if (!StringUtils.isEmpty(message)) {
            int maxLogSize = 5000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                android.util.Log.e(TAG, message.substring(start, end));
            }
        } else {
            android.util.Log.e(TAG, "");
        }
    }

    public static void printStackTrace(Exception e) {
        e.printStackTrace();
    }
}
