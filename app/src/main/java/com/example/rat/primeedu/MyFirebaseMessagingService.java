package com.example.rat.primeedu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        System.out.println("From: " + remoteMessage.getFrom());

        if(remoteMessage.getData()!=null){
            notify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        }
    }

    private void notifications(RemoteMessage remoteMessage) {

    }

    private void sendNotificationToApi26(RemoteMessage remoteMessage) {
    }

    private void notify(String title, String body) {


        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANEL_ID = "com.example.rat.primeedu";

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Chanel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,NOTIFICATION_CHANEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(),builder.build());
    }


//ccWHWg8G-bo:APA91bHliqqeQZMK8Z7lQ-U5sH9MOXJPGUNn8IhtCNXqLCy9u8CPuuG8zdsbNPC51su1FnNUazzCp3QZuxr8wF0wzCH7Pkh8pSKdewYD7Fgd16OBKOgOpCqUk0tfLezWa5lJLu2mdqKT
}
