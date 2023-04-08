package com.example.smshandler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            SmsHandlerJob job = new SmsHandlerJob();
                            job.RunJob();
                        }
                    }
            ).start();

            final String CHANNELID = "Foreground Service ID";

            NotificationChannel channel = null;
            Notification.Builder notification = null;


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channel = new NotificationChannel(
                        CHANNELID,
                        CHANNELID,
                        NotificationManager.IMPORTANCE_LOW
                );
                getSystemService(NotificationManager.class).createNotificationChannel(channel);
                notification = new Notification.Builder(this, CHANNELID)
                        .setContentText("Activity Recognition is running....")
                        .setContentTitle("Obesity Point");
                startForeground(1001, notification.build());
            }
            else{
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.app_name))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                Notification notificationOld = builder.build();

                startForeground(1, notificationOld);
            }
        }
        catch (Exception ex){
            Log.e("ForegroundService", "Can't run foreground service");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}