package com.example.kevzzsk.dengueradar;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;


public class NotificationInterface {
    Context mContext;
    String channelId = "channel1";

    public NotificationInterface(Context mContext) {
        this.mContext = mContext;
    }

//    public static final int THRESHOLD = 3;

//    public boolean isAboveThreshold(int num) {
//        if(num > THRESHOLD) {
//            sendNotification(num);
//            return true;
//        }
//        else {
//            return false;
//        }
//    }

    public void sendNotification() {
        createNotificationChannel();
        createNotification();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Warning notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setDescription("Dengue warning notification");
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification() {
        Intent intent = new Intent(mContext, MenuInterface.class);
        intent.putExtra("action","goToTipPage");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        String title = "You are near to a dengue cluster!";
        String message = "Click to view tips on how to prevent dengue.";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                .setSmallIcon(R.drawable.alert)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent);
                //.setVisibility(VISIBILITY_PUBLIC);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }
}
