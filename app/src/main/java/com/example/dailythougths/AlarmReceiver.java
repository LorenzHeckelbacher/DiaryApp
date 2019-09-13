package com.example.dailythougths;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String CHANNEL_1_ID = "default_channel";
    private NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannels();



        Intent notificationIntent = new Intent (context,NotificationActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, "CHANNEL_1_ID")
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.ic_menu_today)
                .setContentTitle("Notification Title")
                .setContentText("Notification Text")
                .setAutoCancel(true)
                .build();

        if(intent.getAction().equals(("STRING"))) {
            notificationManager.notify(100, notification);
        }
    }


    //current Versions require at least one channel to be defined, which is defined here
    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("This is the default channel");

            notificationManager.createNotificationChannel(channel1);
        }
    }
}
