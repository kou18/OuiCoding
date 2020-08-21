package com.example.ouicoding.Service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID="notif_fin_contrat";



    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationsChannels();
    }

    private void createNotificationsChannels(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(
                    CHANNEL_ID,
                    "Alerte",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("notif");
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
