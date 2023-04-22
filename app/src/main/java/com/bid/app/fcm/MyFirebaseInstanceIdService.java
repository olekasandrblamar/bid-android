package com.bid.app.fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bid.app.MainActivity;
import com.bid.app.R;
import com.bid.app.session.SessionManager;
import com.bid.app.util.Logger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();

    private SessionManager sessionManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Logger.e(TAG, "remoteMessage : " + remoteMessage.getNotification().toString());
        Logger.e(TAG, "remoteMessage : " + remoteMessage.getNotification().getBody());
        notificationDialog(remoteMessage.getNotification().getBody());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Logger.e(TAG, "token : " + s);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setFCMToken(s);


    }

    private void notificationDialog(final String message) {
//        NotificationManagerCompat notificationManager;
//        notificationManager = NotificationManagerCompat.from(this);
//        RemoteViews collapsedView = new RemoteViews(getPackageName(),
//                R.layout.notification_small);
//        RemoteViews expandedView = new RemoteViews(getPackageName(),
//                R.layout.notification_large);
//
//        Intent clickIntent = new Intent(this, MainActivity.class);
//        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
//                0, clickIntent, 0);
//
//        collapsedView.setTextViewText(R.id.text_view_collapsed_1, message);
//        String NOTIFICATION_CHANNEL_ID = getResources().getString(R.string.app_name);
//
//        expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.discover);
//        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);
//        expandedView.setTextViewText(R.id.text_view_expanded, message + "expanded");
//        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setCustomContentView(collapsedView)
//                .setCustomBigContentView(expandedView)
//                //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                .build();
//
//        notificationManager.notify(1, notification);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = getResources().getString(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, message, NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription(message);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText(message);
        notificationManager.notify(1, notificationBuilder.build());

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
    }
}
