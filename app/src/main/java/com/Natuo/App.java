package com.Natuo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import net.gotev.uploadservice.UploadServiceConfig;

public class App extends Application {

    private String notificationChannelID = "TestChannel";
    private NotificationChannel channel;
    private  NotificationManager manager;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            channel = new NotificationChannel(
                    notificationChannelID,
                    "TestApp Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        UploadServiceConfig.initialize(
                this,
                notificationChannelID,
                BuildConfig.DEBUG
        );
    }
}
