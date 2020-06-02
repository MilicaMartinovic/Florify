package com.example.florify.helpers;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.florify.ConnectionRequestActivity;
import com.example.florify.R;

public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_1_ID = "channel1";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    public NotificationManager getManager() {
        if(mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_1_ID,
                "channel 1",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("notification channel");
        getManager().createNotificationChannel(channel);
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message, String id) {
        Intent resultIntent = new Intent(this, ConnectionRequestActivity.class);
        resultIntent.putExtra("newConnection", id);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_leaf)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }
}
