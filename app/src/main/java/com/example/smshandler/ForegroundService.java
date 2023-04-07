package com.example.smshandler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.pengrad.telegrambot.TelegramBot;

public class ForegroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int counter = 0;
                        TelegramBot bot = TelegramBotService.GetBot();
                        TelegramBotService.SendMessage("Health check", bot);
                        while (true) {
                            Log.e("Service", "Service is running...");
                            try {
                                int sleepTime = 5000;

                                Thread.sleep(sleepTime);

                                counter += sleepTime;

                                //30 minutes
                                if(counter > 1.8e+6){
                                    TelegramBotService.SendMessage("Health check", bot);
                                    counter = 0;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
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
        }
        try{
            startForeground(1001, notification.build());
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